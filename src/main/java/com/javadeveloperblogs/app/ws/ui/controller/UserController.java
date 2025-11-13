package com.javadeveloperblogs.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.*;

import com.javadeveloperblogs.app.ws.exceptions.UserServiceException;
import com.javadeveloperblogs.app.ws.service.EmailService;
import com.javadeveloperblogs.app.ws.ui.model.request.PasswordResetRequestModel;
import com.javadeveloperblogs.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javadeveloperblogs.app.ws.service.AddressService;
import com.javadeveloperblogs.app.ws.service.UserService;
import com.javadeveloperblogs.app.ws.shared.dto.AddressDTO;
import com.javadeveloperblogs.app.ws.shared.dto.UserDto;
import com.javadeveloperblogs.app.ws.ui.model.request.UserDetailsRequestModel;
import com.google.common.reflect.TypeToken;

//Author  Nasim_Sarwar

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	AddressService addressService;

	@Autowired
	private EmailService service;
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {

         ModelMapper mapper = new ModelMapper();
		UserDto userDto = userService.getUserByUserId(id);
		UserRest returnValue  =  mapper.map(userDto, UserRest.class);
		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
		UserRest returnValue = new UserRest();
		if(userDetails.getFirstName().isEmpty()||userDetails.getLastName().isEmpty()||userDetails.getEmail().isEmpty()
				||userDetails.getPassword().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
	//	userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));

		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
		
	}


	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id , @RequestBody UserDetailsRequestModel userDetails)
	{
		UserRest returnValue = new UserRest();
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.updateUser(id, userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {

		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		 userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;


	}
	
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "2") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);
		
		Type listType = new TypeToken<List<UserRest>>() {
		}.getType();
		returnValue = new ModelMapper().map(users, listType);

		/*for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}*/

		return returnValue;
	}
	
	// http://localhost:8080/mobile-app-ws/users/jfhdjeufhdhdj/addressses
		//@ApiImplicitParams({
		//	@ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
		//})
		@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
				MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
		public List<AddressesRest> getUserAddresses(@PathVariable String id) {
			List<AddressesRest> addressesListRestModel = new ArrayList<>();

			List<AddressDTO> addressesDTO = addressService.getAddresses(id);

			if (addressesDTO != null && !addressesDTO.isEmpty()) {
				Type listType = new TypeToken<List<AddressesRest>>() {
				}.getType();
				addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

				/*
				 * for (AddressesRest addressRest : addressesListRestModel) { Link addressLink =
				 * linkTo(methodOn(UserController.class).getUserAddress(id,
				 * addressRest.getAddressId())) .withSelfRel(); addressRest.add(addressLink);
				 * 
				 * Link userLink =
				 * linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
				 * addressRest.add(userLink); }
				 */
			}

			return addressesListRestModel;
		}

		/*
		 * // @ApiImplicitParams({ // @ApiImplicitParam(name="authorization",
		 * value="${userController.authorizationHeader.description}",
		 * paramType="header") // })
		 * 
		 * @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {
		 * MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
		 * "application/hal+json" }) public Resource<AddressesRest>
		 * getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		 * 
		 * AddressDTO addressesDto = addressService.getAddress(addressId);
		 * 
		 * ModelMapper modelMapper = new ModelMapper(); Link addressLink =
		 * linkTo(methodOn(UserController.class).getUserAddress(userId,
		 * addressId)).withSelfRel(); Link userLink =
		 * linkTo(UserController.class).slash(userId).withRel("user"); Link
		 * addressesLink =
		 * linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel(
		 * "addresses");
		 * 
		 * AddressesRest addressesRestModel = modelMapper.map(addressesDto,
		 * AddressesRest.class);
		 * 
		 * addressesRestModel.add(addressLink); addressesRestModel.add(userLink);
		 * addressesRestModel.add(addressesLink);
		 * 
		 * return new Resource<>(addressesRestModel); }
		 */

	/*
	 * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
	 * */
	@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

		boolean isVerified = userService.verifyEmailToken(token);

		if(isVerified)
		{
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		} else {
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
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

		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

		if(operationResult)
		{
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}

		return returnValue;
	}



/*
	@PostMapping(path ="/sendingEmail")
	public MailResponse sendEmail(@RequestBody MailRequest request) {
		Map<String, Object> model = new HashMap<>();
		model.put("Name", request.getName());
		model.put("location", "Bangalore,India");
		return service.sendEmail(request, model);

	}*/

}
