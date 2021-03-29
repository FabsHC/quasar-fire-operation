package com.quasarfireoperation.configuration.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"com.quasarfireoperation.gateways.mongodb.repository"})
public class MongoDbConfiguration {}

