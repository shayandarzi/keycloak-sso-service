package ir.kavoshgaran.keycloakauth;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableRabbit
//@EnableEurekaClient
public class KeycloakAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycloakAuthApplication.class, args);
    }

}
