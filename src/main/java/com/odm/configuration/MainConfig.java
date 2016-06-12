package com.odm.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mohamed on 6/9/16.
 */
@Configuration
@Import(value = {BeanConfig.class,PropertyConfig.class})
@ComponentScan(basePackages = {"com.odm"})
public class MainConfig {
}
