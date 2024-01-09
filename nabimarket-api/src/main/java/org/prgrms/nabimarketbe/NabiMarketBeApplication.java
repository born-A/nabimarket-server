package org.prgrms.nabimarketbe;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NabiMarketBeApplication {
    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(NabiMarketBeApplication.class);

        application.addListeners(new ApplicationPidFileWriter());    // jar 배포 용이하게 하기 위한 PID 기록
        application.run(args);
    }

}
