package com.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dao.UserRepository;
import com.app.entities.User;

public class UserDetailsServiceImplementation implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user from database
		User name = userRepository.getUserByUserName(username);
		if(name == null) {
			throw new UsernameNotFoundException("Could not find user in database!!!");
		}
		AppUserDetails userDetails = new AppUserDetails(name); 
		return userDetails;
	}

}
