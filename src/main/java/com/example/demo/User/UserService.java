package com.example.demo.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	
	  public User createUser(User user) {
	        return userRepository.save(user);
	  }

	    public List<User> getAllUsers() {
	       return userRepository.findAll();
	    }

	 
	    public User getUserById(int id) {
	        return userRepository.findById(id).orElse(null);
	    }

	    public boolean deleteUser(int id) {
	    	if(userRepository.existsById(id)) {
		        userRepository.deleteById(id);
		        return true;
	    	}else {
	    		return false;
	    	}
	    }
}
