package net.orderzone.idcard;

import net.orderzone.idcard.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    ApplicationRunner seedDefaults(TemplateService templateService) {
        return args -> {
            templateService.seedDefaultTemplates();
            log.info("Default templates seeded successfully.");
        };
    }
}
