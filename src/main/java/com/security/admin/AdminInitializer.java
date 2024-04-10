package com.security.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.security.entity.User;
import com.security.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class AdminInitializer implements ApplicationRunner {

	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${admin.username}")
	private String adminusername;
	
	@Value("${admin.password}")
	private String password;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		User admin=new User();
		admin.setFirstName("suman kumar jha");
		admin.setLastName("jha");
		admin.setEmail(adminusername);
	 	admin.setPassword(encoder.encode(password));
	 	admin.setConfirmPassword(encoder.encode(password));
	 	admin.setRole("ROLE_ADMIN");
	 	admin.setAccountNonLocked(true);
	 	admin.setEnabled(true);
	 	
	 	
	 	
	 	 if (!repo.existsByEmail(adminusername)) {
	            admin.setPassword(encoder.encode(password));
	            repo.save(admin);
	            log.info("Admin user created successfully");
	            
	            System.out.print("user register success fully");
	        } else {
	          
	        	  System.out.print("user already exists ");
	        	log.info("Admin user already exists");
	        }
		
		
	}

}
