package com.quasarfireoperation.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SatelliteCoordinate {
    KENOBI(-500, -200),
    SKYWALKER(100, -100),
    SATO(500, 100);

    float x;
    float y;
}
