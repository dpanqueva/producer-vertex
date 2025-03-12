package com.dieva.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.util.HashMap;
import java.util.Map;

public class KafkaProducerVerticle extends AbstractVerticle {

    private KafkaProducer<String, String> producer;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        /**
         * My producer
         * */
        producer = KafkaProducer.create(vertx, config);

        /**
         * Start API REST
         * */
        vertx.createHttpServer().requestHandler(req -> {
                    if (req.method().name().equals("POST") && req.path().equals("/send")) {
                        req.bodyHandler(body -> {
                            String profession = body.toString();
                            sendToKafka(profession);
                            req.response().end("Mensaje enviado a Kafka: " + profession);
                        });
                    } else {
                        req.response().setStatusCode(404).end();
                    }
                })
                .listen(8063, http -> {
                    if (http.succeeded()) {
                        System.out.println("Servidor HTTP iniciado en http://localhost:8063");
                        startPromise.complete();
                    } else {
                        startPromise.fail(http.cause());
                    }
                });

    }
    private void sendToKafka(String profession) {
        KafkaProducerRecord<String, String> record =
                KafkaProducerRecord.create("profession", profession);
        producer.send(record, result -> {
            if (result.succeeded()) {
                System.out.println("Mensaje enviado a Kafka: " + profession);
            } else {
                System.err.println("Error enviando mensaje a Kafka: " + result.cause().getMessage());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        producer.close();
    }
}
