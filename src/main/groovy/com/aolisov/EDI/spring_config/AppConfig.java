package com.aolisov.EDI.spring_config;

import com.aolisov.EDI.processor.ResponseProcessor;
import com.aolisov.EDI.processor.SimpleJsonResponseProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.aolisov.EDI")
public class AppConfig {

    @Bean
    @Scope("singleton")
    public ResponseProcessor responseProcessor() {
        return new SimpleJsonResponseProcessor();
    }
}
