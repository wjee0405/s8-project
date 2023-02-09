package net.springboot.synpulse8challenges;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Synpulse8ChallengesApplication {

	public static void main(String[] args) {
		SpringApplication.run(Synpulse8ChallengesApplication.class, args);
	}

}
