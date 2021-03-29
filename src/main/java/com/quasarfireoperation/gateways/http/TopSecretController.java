package com.quasarfireoperation.gateways.http;

import com.quasarfireoperation.domains.Location;
import com.quasarfireoperation.domains.Satellite;
import com.quasarfireoperation.domains.exception.SatelliteNotFoundException;
import com.quasarfireoperation.gateways.SatelliteDataGateway;
import com.quasarfireoperation.gateways.http.request.SatelliteListRequest;
import com.quasarfireoperation.gateways.http.request.SatelliteRequest;
import com.quasarfireoperation.gateways.http.response.LocationResponse;
import com.quasarfireoperation.gateways.http.response.SpaceShipResponse;
import com.quasarfireoperation.usecase.CalculateLocationFromSatellites;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.Locale.getDefault;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class TopSecretController {

    private static final String WHITE_SPACE_SEPARATOR = " ";
    private static final String NOT_ENOUGH_INFORMATION_SATELLITE = "not.enough.information.satellite";
    private final CalculateLocationFromSatellites calculateLocationFromSatellites;
    private final SatelliteDataGateway satelliteDataGateway;
    private final MessageSource messageSource;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "/topsecret/")
    @ResponseStatus(OK)
    public SpaceShipResponse sendMessageToSatellites(@Valid @RequestBody final SatelliteListRequest request){

        return new SpaceShipResponse(new LocationResponse(new Location()), "message");
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "/topsecret_split/{satellite_name}")
    @ResponseStatus(OK)
    public SpaceShipResponse sendMessageToSpecificSatellite(
            @PathVariable(value = "satellite_name") final String satelliteName,
            @Valid @RequestBody final SatelliteRequest request) {
        request.setName(satelliteName);
        final Location location = calculateLocationFromSatellites.getLocation(request.getDistance());
        final Satellite satellite = satelliteDataGateway.save(request.toDomain());
        return new SpaceShipResponse(new LocationResponse(location), join(satellite.getMessage(), WHITE_SPACE_SEPARATOR));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "/topsecret_split/{satellite_name}")
    @ResponseStatus(OK)
    public SpaceShipResponse getMessageFromSpecificSatellite(
            @PathVariable(value = "satellite_name") final String satelliteName) {
        final Satellite satellite = satelliteDataGateway.findBySatelliteName(satelliteName);
        if (isNull(satellite)) {
            throw new SatelliteNotFoundException(messageSource.getMessage(NOT_ENOUGH_INFORMATION_SATELLITE, null, getDefault()));
        }
        return new SpaceShipResponse(new LocationResponse(satellite.getLocation()), join(satellite.getMessage(), WHITE_SPACE_SEPARATOR));
    }
}
