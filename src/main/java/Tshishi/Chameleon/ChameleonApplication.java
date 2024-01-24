package Tshishi.Chameleon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class ChameleonApplication {

	private final static Logger logger = Logger.getLogger(ChameleonApplication.class.getSimpleName());

	public static void main(String[] args) {

		SpringApplication.run(ChameleonApplication.class, args);
		logger.info("This Chameleons apps started :). BY TSHISHI");
	}

}
