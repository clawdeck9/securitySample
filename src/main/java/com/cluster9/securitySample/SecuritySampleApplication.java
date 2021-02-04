package com.cluster9.securitySample;

import com.cluster9.securitySample.persistence.AppRoleRepository;
import com.cluster9.securitySample.persistence.AppUserRepository;
import com.cluster9.securitySample.services.AccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecuritySampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuritySampleApplication.class, args);
	}

	@Bean
	public CommandLineRunner start(AccountServiceImpl accountService){
		return args -> {
			accountService.addAppRole("ADMIN");
			accountService.addAppRole("USER");
			accountService.addAppRole("BILLS");
			accountService.addAppRole("PRODUCTS");

			accountService.addAppUser("user", "user");
			accountService.addAppUser("admin", "admin");
			accountService.addAppUser("bill", "user");
			accountService.addAppUser("product", "user");

			accountService.addRoleToUser("user", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("bill", "BILLS");
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return  new BCryptPasswordEncoder();
	}

}
