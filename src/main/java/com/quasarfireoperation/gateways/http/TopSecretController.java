package com.quasarfireoperation.gateways.http;

import com.quasarfireoperation.domains.Location;
import com.quasarfireoperation.domains.Satellite;
import com.quasarfireoperation.gateways.SatelliteDataGateway;
import com.quasarfireoperation.gateways.http.request.SatelliteListRequest;
import com.quasarfireoperation.gateways.http.request.SatelliteRequest;
import com.quasarfireoperation.gateways.http.response.LocationResponse;
import com.quasarfireoperation.gateways.http.response.SpaceShipResponse;
import com.quasarfireoperation.usecase.CalculateLocationFromSatellites;
import com.quasarfireoperation.usecase.DecryptSatelliteMessages;
import com.quasarfireoperation.usecase.FindSatelliteByName;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
public class TopSecretController {

    private static final String WHITE_SPACE_SEPARATOR = " ";
    private final CalculateLocationFromSatellites calculateLocationFromSatellites;
    private final SatelliteDataGateway satelliteDataGateway;
    private final FindSatelliteByName findSatelliteByName;
    private final DecryptSatelliteMessages decryptSatelliteMessages;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "/topsecret/")
    @ResponseStatus(OK)
    public SpaceShipResponse sendMessageToSatellites(@Valid @RequestBody final SatelliteListRequest request){
        request.getSatellites()
                .stream()
                .map(SatelliteRequest::toDomain)
                .forEach(satelliteDataGateway::save);
        List<Float> distances = request.getSatellites().stream().map(SatelliteRequest::getDistance).collect(toList());
        final Location location = calculateLocationFromSatellites.getLocation(distances.toArray(Float[]::new));
        final String decryptedMessages = decryptSatelliteMessages.getMessage(
                request.getSatellites().stream()
                .map(SatelliteRequest::getMessage)
                .collect(toList()));
        return new SpaceShipResponse(new LocationResponse(location), decryptedMessages);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "/topsecret_split/{satellite_name}")
    @ResponseStatus(OK)
    public SpaceShipResponse sendMessageToSpecificSatellite(
            @PathVariable(value = "satellite_name")
            @Pattern(regexp = "kenobi|skywalker|sato", message = "{field.must.match}", flags = CASE_INSENSITIVE)
            final String satelliteName,
            @Valid @RequestBody final SatelliteRequest request) {
        request.setName(satelliteName);
        final Satellite satellite = satelliteDataGateway.save(request.toDomain());
        return new SpaceShipResponse(new LocationResponse(satellite.getLocation()), join(satellite.getMessage(), WHITE_SPACE_SEPARATOR));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "/topsecret_split/{satellite_name}")
    @ResponseStatus(OK)
    public SpaceShipResponse getMessageFromSpecificSatellite(
            @PathVariable(value = "satellite_name") final String satelliteName) {
        final Satellite satellite = findSatelliteByName.execute(satelliteName);
        return new SpaceShipResponse(new LocationResponse(satellite.getLocation()), join(satellite.getMessage(), WHITE_SPACE_SEPARATOR));
    }
}
