package com.cts.fse.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnectionConfig {

    public MongoCollection<Document> getConnection(String collectionName) {
        String uri = System.getProperty("spring.data.mongodb.uri");
        try(MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase database = mongoClient.getDatabase("MovieBookingApp");
            return database.getCollection(collectionName);
        }
    }
}
