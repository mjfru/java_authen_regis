package com.codingdojo.authentication.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.authentication.models.LoginUser;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;
    
    public User findById(Long id) {
    	Optional<User> existingUser = userRepo.findById(id);
    	if(existingUser.isPresent()) {
    		return existingUser.get();
    	}
    	return null;
    }    
    // TO-DO: Write register and login methods. This method will be called from the controller whenever a user submits a registration form.
    
    public User register(User newUser, BindingResult result) {
    	// Reject if email is taken (present in database)
    	Optional<User> existingUser = userRepo.findByEmail(newUser.getEmail());
    	if(existingUser.isPresent()) {
    		result.rejectValue("email", "Matches", "This email has already been registered.");
    	}
    	// TO-DO - Reject values or register if no errors:
        // Reject if password doesn't match confirmation
    	if(!newUser.getPassword().equals(newUser.getConfirm())) {
    	    result.rejectValue("confirm", "Matches", "Passwords must match!");
    	}
        // Return null if result has errors
    	if(result.hasErrors()) {
    		return null;
    	}  	
        // Hash and set password, save user to database
    	String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
    	newUser.setPassword(hashed);
        return userRepo.save(newUser);
    }
    
    // This method will be called from the controller
    // whenever a user submits a login form.
        public User login(LoginUser newLogin, BindingResult result) {
        	// Find user in the DB by email
            // Reject if NOT present
        	Optional<User> existingUser = userRepo.findByEmail(newLogin.getEmail());
        	if(!existingUser.isPresent()) {
        		result.rejectValue("email", "Matches", "Invalid Email - User Not Found");
        		return null;
        	}
        	
        	User user = existingUser.get();
            // Reject if BCrypt password match fails
        	if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
        	    result.rejectValue("password", "Matches", "Invalid Password");
        	}
            // Return null if result has errors
        	if(result.hasErrors()) {
        		return null;
        	}
        	// Otherwise, return the user object
        	return user;
    }
}