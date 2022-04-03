package io.github.hylexus.jt808.samples.client.debug;

import io.github.hylexus.jt808.samples.client.debug.client1.ClientProps;
import io.github.hylexus.jt808.samples.client.debug.client1.SimpleClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({ClientProps.class})
public class Jt808ClientSampleDebugApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jt808ClientSampleDebugApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ClientProps clientProps) {
        return arg -> SimpleClient.start(clientProps);
    }

}
