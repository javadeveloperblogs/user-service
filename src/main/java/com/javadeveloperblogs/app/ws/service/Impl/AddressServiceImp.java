package com.javadeveloperblogs.app.ws.service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.javadeveloperblogs.app.ws.io.entites.AddressEntity;
import com.javadeveloperblogs.app.ws.io.entites.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.javadeveloperblogs.app.ws.io.repository.AddressRepository;
import com.javadeveloperblogs.app.ws.io.repository.UserRepository;
import com.javadeveloperblogs.app.ws.service.AddressService;
import com.javadeveloperblogs.app.ws.shared.dto.AddressDTO;

@Service
public class AddressServiceImp implements AddressService {

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<AddressDTO> getAddresses(String userId) {

		List<AddressDTO> returnValue = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> listAddressEntities = addressRepository.findAllByUserDetails(userEntity);
		
		for(AddressEntity addressEntity : listAddressEntities) {
			returnValue.add(mapper.map(addressEntity, AddressDTO.class));
		}

		return returnValue;
	}

	@Override
	public AddressDTO getAddress(String addressId) {
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		ModelMapper mapper = new ModelMapper();
		AddressDTO returnValue = mapper.map(addressEntity, AddressDTO.class);
		return returnValue;
	}

}
