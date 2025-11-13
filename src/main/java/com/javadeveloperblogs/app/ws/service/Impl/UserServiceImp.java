package com.javadeveloperblogs.app.ws.service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.javadeveloperblogs.app.ws.exceptions.UserServiceException;
import com.javadeveloperblogs.app.ws.io.entites.PasswordResetTokenEntity;
import com.javadeveloperblogs.app.ws.io.entites.UserEntity;
import com.javadeveloperblogs.app.ws.io.repository.PasswordResetTokenRepository;
import com.javadeveloperblogs.app.ws.service.EmailService;
import com.javadeveloperblogs.app.ws.shared.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.javadeveloperblogs.app.ws.io.repository.UserRepository;
import com.javadeveloperblogs.app.ws.service.UserService;
import com.javadeveloperblogs.app.ws.shared.dto.UserDto;
import com.javadeveloperblogs.app.ws.ui.model.response.ErrorMessages;

//Author Nasim_Sarwar
@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRespository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
     @Autowired
     PasswordResetTokenRepository passwordResetTokenRepository;


    @Autowired
    private Configuration config;

    @Autowired
    private EmailService service;
    @Override
    public UserDto createUser(UserDto user) {
        ModelMapper mapper = new ModelMapper();
        if (userRespository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("Record already exists");
        user.getAddresses().forEach(e -> e.setAddressId(utils.generateAddressId(20)));
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        //userEntity.setAddresses(listAddressEntities);
        userEntity.setUserId(utils.generateUserId(20));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserEntity userStoredDetails = userRespository.save(userEntity);
        UserDto returnValue = mapper.map(userStoredDetails, UserDto.class);
        return returnValue;


    }

    /**
     * Retrieves user details based on the provided email address.
     *
     * @param email the email address of the user to retrieve
     * @return UserDto containing user details mapped from the entity
     */
    @Override
    public UserDto getUser(String email) {
        // Fetch the user entity from the database using the repository and email as a key
        UserEntity userEntity = userRespository.findByEmail(email);

        // Create a new ModelMapper instance to map entity to DTO
        ModelMapper mapper = new ModelMapper();

        // Map the retrieved UserEntity object to a UserDto for returning to the caller
        UserDto returnValue = mapper.map(userEntity, UserDto.class);

        // Return the mapped UserDto object
        return returnValue;
    }


    /**
     * Retrieves user details based on the provided userId.
     *
     * @param userId the unique identifier of the user to retrieve
     * @return UserDto containing user details mapped from the entity
     */
    @Override
    public UserDto getUserByUserId(String userId) {
        // Fetch the user entity from the database using the repository and userId
        UserEntity userEntity = userRespository.findByUserId(userId);

        // Initialize a ModelMapper instance to convert entity objects to DTOs
        ModelMapper mapper = new ModelMapper();

        // Map the UserEntity to a UserDto to hide entity details from the outside world
        UserDto returnValue = mapper.map(userEntity, UserDto.class);

        // Return the mapped UserDto object
        return returnValue;
    }


    /**
     * Updates an existing user's details using the provided userId and UserDto data.
     *
     * @param userId   the unique identifier of the user to update
     * @param userDto  the new user details to be updated
     * @return UserDto containing the updated user details
     * @throws UserServiceException if no user is found with the given userId
     */
    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        // Fetch the existing user entity from the database using the repository and userId
        UserEntity userEntity = userRespository.findByUserId(userId);

        // Initialize a ModelMapper instance to convert entities to DTOs
        ModelMapper mapper = new ModelMapper();

        // If no user entity is found, throw a custom exception
        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        // Update the entity's fields with new data from the provided DTO
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        // Save the updated entity back to the database
        UserEntity storedUserEntity = userRespository.save(userEntity);

        // Map the updated entity to a DTO to return to the caller
        UserDto updatedUserDetail = mapper.map(storedUserEntity, UserDto.class);

        // Return the updated user details
        return updatedUserDetail;
    }


    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRespository.findByUserId(userId);
        userRespository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if (page > 0)
            page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRespository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRespository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRespository.save(userEntity);
                returnValue = true;
            }
        }
        return returnValue;

    }

    @Override
    public boolean requestPasswordReset(String email) {


        boolean returnValue = false;

        UserEntity userEntity = userRespository.findByEmail(email);
        Map<String, Object> model = new HashMap<>();

        if (userEntity == null) {
            return returnValue;
        }

        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
//
//        MailRequest request = new MailRequest();
//        request.setFirstName(userEntity.getFirstName());
//        request.setLastName(userEntity.getLastName());
//        request.setTo(userEntity.getEmail());
//        request.setFrom("atoztraders39@gmail.com");
//        request.setSubject("PASSWORD RESET ");


        model.put("firstName",userEntity.getFirstName() );
        model.put("lastName",userEntity.getFirstName() );
        model.put("location", "Bangalore,India");
        model.put("TOKEN", token);
        try{

            Template t = config.getTemplate("password-reset-template.ftl");

//            service.sendEmail(request, model,t );
//            Template tem = config.getTemplate("email-template.ftl");
//            request.setSubject("Login_System");
//            service.sendEmail(request, model,tem );
        }
        catch (IOException t ){
          throw  new RuntimeException("Mail Sending failure : "+t.getMessage());
           // response.setStatus(Boolean.FALSE);
        }



       /**
        * Sending mail to User for Passoword reset
        * returnValue = new AmazonSES().sendPasswordResetRequest(
                userEntity.getFirstName(),
                userEntity.getEmail(),
                token);*/

        return returnValue;


    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if (Utils.hasTokenExpired(token)) {
            return returnValue;
        }

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRespository.save(userEntity);

        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRespository.findByEmail(username);
        if (userEntity == null)
            throw new UsernameNotFoundException("use is not exists");
        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
