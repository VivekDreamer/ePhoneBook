package com.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.dao.ContactRepository;
import com.app.dao.UserRepository;
import com.app.entities.Contact;
import com.app.entities.User;

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRespoRepository;
	@Autowired
	private ContactRepository contactRepository;
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
		System.out.println(query);
		User user = this.userRespoRepository.getUserByUserName(principal.getName());
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
