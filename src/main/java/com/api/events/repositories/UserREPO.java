package com.api.events.repositories;


import com.api.events.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserREPO extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
