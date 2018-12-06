package io.seyon.apigateway.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.common.TokenGenerator;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.model.CompanyModel;
import io.seyon.apigateway.model.Success;
import io.seyon.apigateway.service.LoginService;
import io.seyon.apigateway.service.UserService;

@Controller
public class UserController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	SeyonGwProperties properties;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/")
	public String successLogin(@ModelAttribute User user, Model model, HttpServletRequest request) {
		return "success";
	}
	
/*	@GetMapping("/reset-password")
	public String resetPassword(@ModelAttribute User user, Model model, HttpServletRequest request) {
		model.addAttribute("error", false);
		String token = TokenGenerator.generateToken("LT");
		request.getSession().setAttribute("LT", token);
		user.setLtToken(token);
		return "forgetPassword";
	}
	
	
	@PostMapping("/reset-password")
	public String resetPasswordAction(@ModelAttribute User user, Model model, HttpServletRequest request) {
		String lttoken= (String) request.getSession().getAttribute("LT");
		if(!lttoken.equals(user.getLtToken())) {
			model.addAttribute("error", true);
			model.addAttribute("exception", "Invalid Session token");
			return "forgetPassword";
		}
		log.info("Resetting password for email :{}",user.getEmail());
		
		Success success = userService.resetPassword(user.getEmail());
		model.addAttribute("success", true);
		model.addAttribute("message", success.getMessage());
		
		return "forgetPassword";
	}*/
	
	
	@GetMapping("/signup")
	public String signUp(@ModelAttribute CompanyModel companyModel, Model model, HttpServletRequest request,Authentication authentication) {
		
		//check the authenticated user is registered in our system
		OAuth2Authentication auth= (OAuth2Authentication) authentication;
		UsernamePasswordAuthenticationToken userDetails=(UsernamePasswordAuthenticationToken) auth.getUserAuthentication();
		Map<String, String> detailsMap = new LinkedHashMap<>();
		
		detailsMap = (Map<String, String>) userDetails.getDetails();
		String userEmail = detailsMap.get("email");
		String name = detailsMap.get("name");
		io.seyon.apigateway.model.User userinfo=companyModel.getUserInfo();
		userinfo.setEmail(userEmail);
		userinfo.setName(name);
		
		String token = TokenGenerator.generateToken("LT");
		request.getSession().setAttribute("LT", token);
		companyModel.setLtToken(token);
		
		return "signUp";
	}
	
	@GetMapping("/userNotFound")
	public String userNotFound() {
		log.info("User Not Registered");
		return "userNotFound";
	}
	
	@PostMapping("/signup")
	public String signUpCompany(@ModelAttribute("companyModel") CompanyModel companyModel, Model model, HttpServletRequest request) {
		
		String lttoken= (String) request.getSession().getAttribute("LT");
		if(!lttoken.equals(companyModel.getLtToken())) {
			model.addAttribute("error", true);
			model.addAttribute("exception", "Invalid Session token");
			return "signUp";
		}
		log.info("Creating Company {}",companyModel);
		try{
			Success success=userService.createCompany(companyModel);
			if(success.getCode()!=0) {
				model.addAttribute("error", true);
				model.addAttribute("exception", success.getMessage());
				return "signUp";
			}
				
		}catch(Exception e) {
			model.addAttribute("error", true);
			model.addAttribute("exception", "Some thing went wrong please contact administrator");
			return "signUp";
		}
		return "success";
	}
}
