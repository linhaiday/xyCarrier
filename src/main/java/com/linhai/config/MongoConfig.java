package com.linhai.config;

/**
 * Created by linhai on 2018/12/9.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Configuration
public class MongoConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${mongodb.hostports}")
    private String hostports;

    @Value("${mongodb.maxConnect}")
    private String maxConnect;
    @Value("${mongodb.maxWaitThread}")
    private String maxWaitThread;
    @Value("${mongodb.maxTimeOut}")
    private String maxTimeOut;
    @Value("${mongodb.maxWaitTime}")
    private String maxWaitTime;

    @Value("${mongodb.username}")
    private String username;
    @Value("${mongodb.password}")
    private String password;
    @Value("${mongodb.database}")
    private String database;
    @Value("${mongodb.collection")
    private String collection;


    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = null;

        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        build.connectionsPerHost(Integer.valueOf(maxConnect));
        build.threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(maxWaitThread));
        build.connectTimeout(Integer.valueOf(maxTimeOut) * 1000);
        build.maxWaitTime(Integer.valueOf(maxWaitTime) * 1000);
        MongoClientOptions options = build.build();

        try {
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            for (String hostport : hostports.split(", *")) {
                if (StringUtils.isBlank(hostport)) {
                    continue;
                }
                hostport = hostport.trim();

                ServerAddress serverAddress = new ServerAddress(hostport.split(":")[0],Integer.valueOf(hostport.split(":")[1]));
                addrs.add(serverAddress);
            }

            MongoCredential credential = MongoCredential.createScramSha1Credential(username, database, password.toCharArray());
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            credentials.add(credential);

            mongoClient = new MongoClient(addrs,credentials, options);

            LOGGER.info("【mongodb client】: mongodb客户端创建成功");
        } catch (Exception e) {
            LOGGER.error("【mongodb client】: mongodb客户端创建成功");
            e.printStackTrace();
        }
        return mongoClient;
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        return mongoDatabase;
    }

    @Bean
    public MongoCollection<Document> mongoCollection(MongoDatabase mongoDatabase) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        return mongoCollection;
    }

}