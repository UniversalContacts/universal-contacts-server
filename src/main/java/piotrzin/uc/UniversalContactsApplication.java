package piotrzin.uc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import piotrzin.uc.model.User;
import piotrzin.uc.service.UserService;

@SpringBootApplication
public class UniversalContactsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversalContactsApplication.class, args);
	}
}
