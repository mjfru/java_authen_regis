package com.codingdojo.authentication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingdojo.authentication.models.LoginUser;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MainController {
	
	// Add once service is implemented:
    @Autowired
    private UserService userService;
	
    // RENDER INDEX / REGISTRATION PAGE
    @GetMapping("/")	
    // Model model will act as a container to hold whatever attributes we need on the page
    public String index(Model model) {
        // Bind empty User and LoginUser objects to the JSP to capture the form input
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "index.jsp";
    }
    
    // RENDER HOMEPAGE
    @GetMapping("/home")
    public String homePage(Model model, HttpSession session) {
    	// Need to type cast this, caused a long hang-up for me
    	Long userId = (Long) session.getAttribute("userId");
    	// If there's no userId logged in, redirect back to the home page.
    	if (userId == null) {
    		return "redirect:/";
    	}
    	// Otherwise, create a user by using the service.
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
    	return "home.jsp";
    }
    
    // CREATE NEW USER
    @PostMapping("/register")
    // Need to use session to carry over the new user to the home page and log out later
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {      
        // TO-DO Later -- call a register method in the service to do some extra validations and create a new user!
    	userService.register(newUser, result);
        // If there's errors, take the user back via return (index.jsp) to display the errors instead of redirecting
        if(result.hasErrors()) {
            // Be sure to send in the empty LoginUser before re-rendering the page.
            model.addAttribute("newLogin", new LoginUser());
            return "index.jsp";
        }
        // If everything's good to go, save the user in session and redirect them to the home / dash page.
        session.setAttribute("userId", newUser.getId());        
        return "redirect:/home";
    }
    
    // LOGIN EXISTING USER
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
            BindingResult result, Model model, HttpSession session) {        
        // Add once service is implemented:
        User user = userService.login(newLogin, result);        
        // Almost identical to what happens at this step in the "/register" post method.
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "index.jsp";
        // No errors! 
        // TO-DO Later: Store their ID from the DB in session, in other words, log them in.
        // Again, almost the same as above in the "/register" post method.
        } else {
        	session.setAttribute("userId", user.getId());
            return "redirect:/home";
        }
    }
    
    // LOG OUT USER
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	// Sets the session attribute to nothing...null and redirects back to the first page.
    	session.setAttribute("userId", null);
    	return "redirect:/";
    }
    
}
