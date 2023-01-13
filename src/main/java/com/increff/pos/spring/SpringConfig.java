package com.increff.pos.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.increff.pos")
@EnableTransactionManagement
@PropertySources({ //
        @PropertySource(value = "file:./pos.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {

}
