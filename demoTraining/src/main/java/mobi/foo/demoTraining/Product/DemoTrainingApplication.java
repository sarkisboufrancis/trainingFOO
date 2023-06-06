package mobi.foo.demoTraining.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoTrainingApplication {
	private static final Logger logger = LoggerFactory.getLogger(DemoTrainingApplication.class);

	public static void main(String[] args) {
		logger.trace("This is a trace message");
		logger.debug("This is a debug message");
		logger.info("This is an info message");
		logger.warn("This is a warning message");
		logger.error("This is an error message");

		SpringApplication.run(DemoTrainingApplication.class, args);
//
//		try {
//			int result = 10 / 0; // Simulating an exception
//		} catch (Exception e) {
//			logger.error("An error occurred", e);
//		}
	}
}