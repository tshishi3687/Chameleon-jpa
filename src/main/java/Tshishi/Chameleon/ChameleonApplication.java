package Tshishi.Chameleon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.logging.Logger;

@SpringBootApplication
public class ChameleonApplication {

	private final static Logger logger = Logger.getLogger(ChameleonApplication.class.getSimpleName());

	public static void main(String[] args) {

		SpringApplication.run(ChameleonApplication.class, args);
		logger.info("This Chameleons apps started :). BY TSHISHI");
	}

	@Bean
	BCryptPasswordEncoder getBCPE(){
		return new BCryptPasswordEncoder();
	}
}
