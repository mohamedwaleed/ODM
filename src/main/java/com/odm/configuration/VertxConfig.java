package com.odm.configuration;

import com.odm.messeging.MyVertical;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by mohamed on 6/26/16.
 */
@Component
public class VertxConfig {
    @Autowired
    private MyVertical myVertical;

    @PostConstruct
    public void deployVerticle() {
        Vertx.vertx().deployVerticle(myVertical);
    }
}
