package com.pieceofcake.alert_service.alert.infrastructure;

import com.pieceofcake.alert_service.alert.entity.Alert;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AlertMongoRepository extends ReactiveMongoRepository<Alert, String> {
    @Tailable
    @Query("{ 'memberUuid' : ?0 }")
    Flux<Alert> findByMemberUuid(String memberUuid);

    @Tailable
    @Query("{ 'commonAlert' : true }")
    Flux<Alert> findByCommonAlertTrue();
}


