package org.una.programmingIII.Assignment_Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.una.programmingIII.Assignment_Manager.Service.EmailService;
import org.una.programmingIII.Assignment_Manager.Service.PasswordEncryptionService;

@SpringBootApplication
public class AssignmentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentManagerApplication.class, args);
	}
}
