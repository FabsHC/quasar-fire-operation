package com.quasarfireoperation.gateways.mongodb.repository;

import com.quasarfireoperation.gateways.mongodb.document.SatelliteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SatelliteMongoRepository extends MongoRepository<SatelliteDocument, String> {
    Optional<SatelliteDocument> findByNameIgnoreCase(String name);
}
