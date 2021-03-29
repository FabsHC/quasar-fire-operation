package com.quasarfireoperation.gateways.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.quasarfireoperation.domains.Satellite;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SatelliteRequest {
    private String name;
    private float distance;
    @NotEmpty
    private List<String> message;

    public Satellite toDomain() {
        return Satellite.builder()
                .name(this.name)
                .distance(this.distance)
                .message(this.message)
                .build();
    }
}
