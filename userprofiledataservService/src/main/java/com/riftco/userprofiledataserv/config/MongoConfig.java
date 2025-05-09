package com.riftco.userprofiledataserv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.riftco.userprofiledataserv.adapter.persistence")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected @org.springframework.lang.NonNull String getDatabaseName() {
        return "userprofiledb";
    }
    
    // You can customize MongoDB connection options here as needed
    // For example, to use a custom connection string:
    /*
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/userprofiledb");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        return MongoClients.create(mongoClientSettings);
    }
    */
}
