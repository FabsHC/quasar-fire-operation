package com.quasarfireoperation.gateways.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceShipResponse {
    private LocationResponse position;
    private String message;
}
