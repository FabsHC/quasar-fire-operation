package com.quasarfireoperation.gateways;

import com.quasarfireoperation.domains.Satellite;

public interface SatelliteDataGateway {
    Satellite findBySatelliteName(String name);
    Satellite save (Satellite satellite);
}
