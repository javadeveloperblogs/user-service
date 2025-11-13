package com.javadeveloperblogs.app.ws.io.repository;

import com.javadeveloperblogs.app.ws.io.entites.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;



public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
	PasswordResetTokenEntity findByToken(String token);

    void delete(PasswordResetTokenEntity passwordResetTokenEntity);


}
