package com.quasarfireoperation.usecase;

import com.quasarfireoperation.domains.Satellite;
import com.quasarfireoperation.domains.exception.SatelliteNotFoundException;
import com.quasarfireoperation.gateways.SatelliteDataGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

import static java.util.Locale.getDefault;

@Component
@RequiredArgsConstructor
public class FindSatelliteByName {

    private static final String NOT_ENOUGH_INFORMATION_SATELLITE = "not.enough.information.satellite";
    private final SatelliteDataGateway satelliteDataGateway;
    private final MessageSource messageSource;

    public Satellite execute(@NotBlank final String name) {
        return satelliteDataGateway.findBySatelliteName(name)
                .orElseThrow(() -> new SatelliteNotFoundException(
                        messageSource.getMessage(NOT_ENOUGH_INFORMATION_SATELLITE, null, getDefault())));
    }
}
