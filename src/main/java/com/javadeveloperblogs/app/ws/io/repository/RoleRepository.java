package com.javadeveloperblogs.app.ws.io.repository;

import com.javadeveloperblogs.app.ws.io.entites.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	RoleEntity findByName(String name);
}
