package com.javadeveloperblogs.app.ws.ui.controller;

import com.javadeveloperblogs.app.ws.shared.dto.UserDto;
import com.javadeveloperblogs.app.ws.ui.model.request.PasswordResetModel;
import com.javadeveloperblogs.app.ws.ui.model.request.PasswordResetRequestModel;
import com.javadeveloperblogs.app.ws.ui.model.request.UserDetailsRequestModel;
import com.javadeveloperblogs.app.ws.ui.model.response.AddressesRest;
import com.javadeveloperblogs.app.ws.ui.model.response.OperationStatusModel;
import com.javadeveloperblogs.app.ws.ui.model.response.UserRest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
//@CrossOrigin(origins= {"http://localhost:8083", "http://localhost:8084"})
public class UserController {

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

       /* UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserRest.class);*/

        return null;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

      /*  // UserDto userDto = new UserDto();
        // BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);
*/
        return returnValue;
    }


    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
    })
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
      //  userDto = new ModelMapper().map(userDetails, UserDto.class);

       // UserDto updateUser = userService.updateUser(id, userDto);
        //returnValue = new ModelMapper().map(updateUser, UserRest.class);

        return returnValue;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    //@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    //@Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
    })
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
       // returnValue.setOperationName(RequestOperationName.DELETE.name());

      //  userService.deleteUser(id);

       // returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
    })
    @GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

      //  List<UserDto> users = userService.getUsers(page, limit);

        Type listType = new TypeToken<List<UserRest>>() {
        }.getType();
      //  returnValue = new ModelMapper().map(users, listType);

		/*for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}*/

        return returnValue;
    }

    // http://localhost:8080/mobile-app-ws/users/jfhdjeufhdhdj/addressses
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
    })
    @GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
    public List<AddressesRest> getUserAddresses(@PathVariable String id) {
        List<AddressesRest> addressesListRestModel = new ArrayList<>();

       // List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

        /*if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {
            }.getType();
            addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

            for (AddressesRest addressRest : addressesListRestModel) {
                Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
                        .withSelfRel();
                addressRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
                addressRest.add(userLink);
            }
        }*/

        return addressesListRestModel;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
    })
    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE, "application/hal+json" })
    public AddressesRest getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

        /*AddressDTO addressesDto = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

        AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);

        addressesRestModel.add(addressLink);
        addressesRestModel.add(userLink);
        addressesRestModel.add(addressesLink);*/

        return new AddressesRest();
    }

    /*
     * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
     * */
    @GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        //returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

       // boolean isVerified = userService.verifyEmailToken(token);

      //  if(isVerified)
        {
        //    returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
      //  } else {
        //    returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    /*
     * http://localhost:8080/mobile-app-ws/users/password-reset-request
     * */
    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

     //   boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

      //  returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        //returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

       // if(operationResult)
        {
       //     returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }



    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

      /*  boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());*/

      //  if(operationResult)
        {
           // returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }


}
