package com.javadeveloperblogs.app.ws.io.repository;

import java.util.List;

import com.javadeveloperblogs.app.ws.io.entites.AddressEntity;
import com.javadeveloperblogs.app.ws.io.entites.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId);
}
