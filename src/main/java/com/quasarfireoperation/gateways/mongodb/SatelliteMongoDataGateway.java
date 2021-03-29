package com.quasarfireoperation.gateways.mongodb;

import com.quasarfireoperation.domains.Satellite;
import com.quasarfireoperation.gateways.SatelliteDataGateway;
import com.quasarfireoperation.gateways.mongodb.document.SatelliteDocument;
import com.quasarfireoperation.gateways.mongodb.repository.SatelliteMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SatelliteMongoDataGateway implements SatelliteDataGateway {

    private final SatelliteMongoRepository repository;

    @Override
    public Satellite findBySatelliteName(final String name) {
        return repository.findByNameIgnoreCase(name)
                .map(SatelliteDocument::toDomain)
                .orElse(null);
    }

    @Override
    public Satellite save(final Satellite satellite) {
        return repository.save(new SatelliteDocument(satellite)).toDomain();
    }
}
