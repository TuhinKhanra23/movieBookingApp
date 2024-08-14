package com.cts.fse.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Configuration
public class MongoConnectionConfig {
    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    public MongoDatabase establishConnection() {
        System.out.println(connectionString);
//        String uri = System.getProperty(connectionString);
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient.getDatabase("MovieBookingApp");

    }
}
