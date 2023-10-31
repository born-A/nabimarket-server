package org.prgrms.nabimarketbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NabiMarketBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NabiMarketBeApplication.class, args);
	}

}
