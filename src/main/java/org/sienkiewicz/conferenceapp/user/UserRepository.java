package org.sienkiewicz.conferenceapp.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByLogin(String login);
	
	@Modifying
	@Query("update User u set u.email = ?2  where u.login = ?1")
	void setUserEmailByLogin(String login, String email);
	
}
