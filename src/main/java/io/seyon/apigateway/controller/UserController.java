package io.seyon.apigateway.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.common.TokenGenerator;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.model.CompanyModel;
import io.seyon.apigateway.model.CompanyRole;
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
	
	@Autowired
	LoginService loginService;
	
	
	@GetMapping("/")
	public String successLogin(Model model, HttpServletRequest request,Authentication authentication) {
		OAuth2Authentication auth= (OAuth2Authentication) authentication;
		UsernamePasswordAuthenticationToken userDetails=(UsernamePasswordAuthenticationToken) auth.getUserAuthentication();
		Map<String, String> detailsMap = new LinkedHashMap<>();
		
		detailsMap = (Map<String, String>) userDetails.getDetails();
		String userEmail = detailsMap.get("email");
		User user = loginService.findUserByEmail(userEmail);
		List<CompanyRole> companyRoles=userService.getCompaniesAndRoleForUser(userEmail);
	    model.addAttribute("companyRoles", companyRoles);
	    model.addAttribute("user", user);
		return "chooseYourCompanyView";
	}
	
	
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
				log.error(success.getMessage());
				model.addAttribute("error", true);
				model.addAttribute("exception", "Error While creating Company");
				return "signUp";
			}
				
		}catch(Exception e) {
			model.addAttribute("error", true);
			model.addAttribute("exception", "Some thing went wrong please contact administrator");
			return "signUp";
		}
		
		String userEmail =companyModel.getUserInfo().getEmail();
		User user = loginService.findUserByEmail(userEmail);
		List<CompanyRole> companyRoles=userService.getCompaniesAndRoleForUser(userEmail);
	    model.addAttribute("companyRoles", companyRoles); 
	    model.addAttribute("user", user);
		return "chooseYourCompanyView";

	}
	
	@GetMapping("/selectedCompany")
	public String selectedCompany(@RequestParam Long id, Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		Cookie cookie=new Cookie(Constants.USER_PREFERENCE_COOKIE, String.valueOf(id));
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		return "success";
	}
}
