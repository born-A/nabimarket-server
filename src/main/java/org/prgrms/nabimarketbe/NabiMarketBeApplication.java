package org.prgrms.nabimarketbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NabiMarketBeApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(NabiMarketBeApplication.class);

		application.addListeners(new ApplicationPidFileWriter());	// jar 배포 용이하게 하기 위한 PID 기록
		application.run(args);
	}

}
