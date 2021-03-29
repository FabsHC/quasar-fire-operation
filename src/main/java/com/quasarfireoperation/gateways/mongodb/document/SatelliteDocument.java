package com.quasarfireoperation.gateways.mongodb.document;

import com.quasarfireoperation.domains.Location;
import com.quasarfireoperation.domains.Satellite;
import com.quasarfireoperation.domains.SatelliteCoordinate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("satellite")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SatelliteDocument {
    @Id
    private String name;
    private float distance;
    private List<String> message;

    public SatelliteDocument(final Satellite satellite) {
        this.name = SatelliteCoordinate.valueOf(satellite.getName().toUpperCase()).name();
        this.distance = satellite.getDistance();
        this.message = satellite.getMessage();
    }

    public Satellite toDomain() {
        final SatelliteCoordinate coordinate = SatelliteCoordinate.valueOf(name.toUpperCase());
        return Satellite.builder()
                .name(this.name)
                .distance(this.distance)
                .location(new Location(coordinate.getX(), coordinate.getY()))
                .message(this.message)
                .build();
    }
}
