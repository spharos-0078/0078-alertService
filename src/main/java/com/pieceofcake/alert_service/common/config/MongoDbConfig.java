package com.pieceofcake.alert_service.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableReactiveMongoRepositories(basePackages = "com.pieceofcake.alert_service.alert.infrastructure")
public class MongoDbConfig {

    @Bean
    CommandLineRunner init(MongoTemplate mongoTemplate) {
        return args -> {
            String collectionName = "alert_message_entity";

            // 기존 컬렉션이 있으면 삭제
            if (mongoTemplate.collectionExists(collectionName)) {
                mongoTemplate.dropCollection(collectionName);
                System.out.println("기존 컬렉션 삭제: " + collectionName);
            }

            // Capped Collection 생성
            CollectionOptions options = CollectionOptions.empty()
                    .capped()
                    .size(10 * 1024 * 1024) // 10MB
                    .maxDocuments(1000);    // 최대 1000개 문서

            mongoTemplate.createCollection(collectionName, options);
            System.out.println("Capped Collection 생성 완료: " + collectionName);
        };
    }
}
