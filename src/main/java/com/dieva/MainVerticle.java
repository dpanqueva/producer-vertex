package com.dieva;

import com.dieva.producer.KafkaProducerVerticle;
import io.vertx.core.Vertx;

public class MainVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new KafkaProducerVerticle());
    }
}
