package com.cts.fse.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MongoConnectionConfig {
    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    public MongoCollection<Document> getConnection(String collectionName) {
        System.out.println(connectionString);
//        String uri = System.getProperty(connectionString);
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            MongoDatabase database = mongoClient.getDatabase("MovieBookingApp");
            return database.getCollection(collectionName);
        }
    }
}
