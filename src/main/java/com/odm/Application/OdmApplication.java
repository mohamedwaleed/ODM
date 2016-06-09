package com.odm.Application;

import com.odm.configuration.MainConfig;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by mohamed on 6/9/16.
 */
public class OdmApplication {
    public static void main(String []args){
        SpringApplicationBuilder springApplication = new SpringApplicationBuilder();
        springApplication.logStartupInfo(false);
        springApplication.showBanner(false);
        springApplication.sources(MainConfig.class);
        springApplication.run(args);
    }
}
