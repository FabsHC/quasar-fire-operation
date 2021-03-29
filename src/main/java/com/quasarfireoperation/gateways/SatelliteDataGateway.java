package com.quasarfireoperation.gateways;

import com.quasarfireoperation.domains.Satellite;

import java.util.Optional;

public interface SatelliteDataGateway {
    Optional<Satellite> findBySatelliteName(String name);
    Satellite save (Satellite satellite);
}
