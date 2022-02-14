package com.app.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entities.Contact;
import com.app.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	//implementing pagination method 
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId,Pageable pageable);
	//pageable contains current page and content per page
	
	//search predefined method
	public List<Contact> findByNameContainingAndUser(String keyword,User user);
}
