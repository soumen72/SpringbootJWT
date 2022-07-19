package com.api.events.controller;

import com.api.events.Exceptions.RuntimeExceptionHandle;
import com.api.events.entities.User;
import com.api.events.repositories.UserREPO;
import com.api.events.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

	private Logger logmsg = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserREPO userREPO;





	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userREPO.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userREPO.findByUsername(username);
		return (user == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) throws RuntimeExceptionHandle {

		System.out.println("finding inside");
		User exists = userREPO.findByUsername(createUserRequest.getUsername());
		if (exists != null) {
			logmsg.error("[Fail] (username already exist ) for user : " + createUserRequest.getUsername() +", log error msg : invalid password" );
			throw  new RuntimeExceptionHandle("username already exist, enter new username") ;
		}
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		logmsg.debug("[working] Username set ", createUserRequest.getUsername());

		if(createUserRequest.getPass().length() < 4 || !createUserRequest.getPass().equals(createUserRequest.getConfirmPass())) {
			logmsg.error(" [Fail] Error with user password. Cannot create user (length not correct   )", createUserRequest.getUsername());
			//model.addAttribute("signupError", "error");

			throw  new RuntimeExceptionHandle("Error with user password") ;

		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPass()));

		userREPO.save(user);


		logmsg.info("[Success] Created user Successful ", user.getUsername());
		return ResponseEntity.ok(user);
	}



	@GetMapping("/print-all-headers")
	public void getAllheaders(@RequestHeader Map<String,String> headers){
		headers.forEach((key,value) ->{
			System.out.println("Header Name: "+key+" Header Value: "+value);
		});
	}





//	@PostMapping("/login")
//	public ResponseEntity<User> checkUser(@RequestParam("user") String username, @RequestParam("password") String pwd) throws RuntimeExceptionHandle {
//
//		System.out.println("finding inside");
//		User exists = userREPO.findByUsername(username);
//		if (exists != null) {
//			logmsg.error("[Fail] (username already exist ) for user : " + username +", log error msg : invalid password" );
//			throw  new RuntimeExceptionHandle("username already exist, enter new username") ;
//		}
//		User user = new User();
//
//		if(user.getUsername()==username && user.getPassword()==pwd ){
//
//			//
//		}
//
//		return ResponseEntity.ok(user);
//	}
//
//	private String getJWTToken(String username) {
//		String secretKey = "mySecretKey";
//		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//				.commaSeparatedStringToAuthorityList("ROLE_USER");
//
//		String token = Jwts
//				.builder()
//				.setId("softtekJWT")
//				.setSubject(username)
//				.claim("authorities",
//						grantedAuthorities.stream()
//								.map(GrantedAuthority::getAuthority)
//								.collect(Collectors.toList()))
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + 600000))
//				.signWith(SignatureAlgorithm.HS512,
//						secretKey.getBytes()).compact();
//
//		return "Bearer " + token;
//	}
	
}
