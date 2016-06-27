package com.odm.messeging;

import com.odm.downloader.DownloadStarter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamed on 4/30/16.
 */
@Component
public class MyVertical extends AbstractVerticle {

    private HttpServer httpServer;

    @Autowired
    private DownloadStarter downloadStarter;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println("MyVerticle started!");
        Router router = Router.router(vertx);

        router.get("/download").blockingHandler(ctx -> {
            String url = ctx.request().getParam("url");
            try {
                new URL(url);
                downloadStarter.start(url);
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null, "Invalid url", "Error", JOptionPane.OK_OPTION);
            }
            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().setStatusCode(200);
            ctx.response().end("");
        });
        vertx.createHttpServer().requestHandler(router::accept).listen(45154);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        System.out.println("MyVerticle stopped!");
        httpServer.close();
    }
}
