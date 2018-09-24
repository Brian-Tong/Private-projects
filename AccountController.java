package com.brian.controllers;

import java.security.NoSuchAlgorithmException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.brian.entity.Users;
import com.brian.iservice.IUsersService;
import com.brian.util.HashMD5;

@Controller
@Repository("accountController")
@RequestMapping("/Account")
public class AccountController {
	private IUsersService usersService;
	public AccountController(){
		
	}
	
public AccountController(IUsersService usersService){
		System.out.println("UsersController构造方法");
		this.usersService = usersService;
	}
	
	public void setUsersService(IUsersService usersService){
		System.out.println("UsersController Setter注入");
		this.usersService = usersService;
	}
			
	@RequestMapping(value = "/login",method =RequestMethod.GET)
	public String login(){		
		return "/Account/login";
	}

	@RequestMapping(value = "/login",method =RequestMethod.POST)
	public String login(String username,String password,HttpServletRequest request,HttpServletResponse response) throws NoSuchAlgorithmException{

		String hashpass = HashMD5.getMD5Hex(password);
		System.out.println("222222222222");
		Users user =usersService.findUser(username, hashpass);
		System.out.println("333333333333333333");
		if(user!=null){
			System.out.println("11111111111111");

				request.getSession().setAttribute("UserName", username);
				request.getSession().setAttribute("ImageURL", user.getImageUrl());
				Cookie cookieUserName = new Cookie("UserName", username);
				Cookie cookiePassword = new Cookie("Password", hashpass);

				response.addCookie(cookieUserName);
				response.addCookie(cookiePassword);
			
		return "redirect:/Home/index";
	} else {
		return "redirect:/Account/login";
	}
	}
	
	@RequestMapping(value = "/register",method =RequestMethod.GET)
	public String register(){		
		return "/Account/register";
	}
	
	@RequestMapping(value = "/register",method =RequestMethod.POST)
	public String register(String username,String password,String address) throws NoSuchAlgorithmException{			
		
		Users user=new Users();
		user.setName(username);
		user.setAddress(address);

		String hashpass = HashMD5.getMD5Hex(password);
		user.setPassword(hashpass);		

		usersService.addUsers(user);
		return "redirect:/Account/login";
	}
	@RequestMapping(value = "/logout",method =RequestMethod.GET)
	public String logout(HttpServletRequest request){	


		request.getSession().removeAttribute("UserName");
		request.getSession().removeAttribute("ImageURL");
		return "redirect:../Home/index";
	}
}
