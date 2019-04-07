package com.github.kaysoro.kaellybot.portal.util;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class PortalConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create("mongodb://" + host + ":" + port);
    }
}
