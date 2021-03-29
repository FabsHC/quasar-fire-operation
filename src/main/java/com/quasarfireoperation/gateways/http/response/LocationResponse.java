package com.quasarfireoperation.gateways.http.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.quasarfireoperation.domains.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {
    float x;
    float y;

    public LocationResponse (final Location location) {
        this.x = location.getX();
        this.y = location.getY();
    }
}
