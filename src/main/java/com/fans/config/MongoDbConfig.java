package com.fans.config;


import com.fans.conditionals.MongoDbConditional;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * className: MongoDbConfig
 *
 * @author k
 * @version 1.0
 * @description mongodb配置
 * @date 2020-10-05 15:10
 **/
@Configuration
@Conditional(MongoDbConditional.class)
public class MongoDbConfig {

    @Value(value = "${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return GridFSBuckets.create(database);
    }
}
