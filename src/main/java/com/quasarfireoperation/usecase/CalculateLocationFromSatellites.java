package com.quasarfireoperation.usecase;

import com.quasarfireoperation.domains.Location;
import com.quasarfireoperation.domains.exception.SpaceshipPositionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.quasarfireoperation.domains.SatelliteCoordinate.*;
import static java.lang.Math.pow;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Locale.getDefault;
import static org.apache.commons.lang3.StringUtils.join;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculateLocationFromSatellites {

    private static final String COULD_NOT_FIND_SPACESHIP_COORDINATES = "could.not.find.spaceship.coordinates";
    private final MessageSource messageSource;

    public Location getLocation(final Float ...distances) {
        log.info("Calculate trilateration status=START distances={}", Arrays.toString(distances));
        try {
            //for the operation below we need the 3 distances of the satellites
            if (distances.length<3)
                throw new IllegalArgumentException(getMessage(distances));
            //Distance equation Kenobi - Skywalker
            double a = -2 * KENOBI.getX() + 2 * SKYWALKER.getX();
            double b = -2 * KENOBI.getY() + 2 * SKYWALKER.getY();
            double c = pow(distances[0], 2) - pow(distances[1], 2) - pow(KENOBI.getX(), 2) + pow(SKYWALKER.getX(), 2) - pow(KENOBI.getY(), 2) + pow(SKYWALKER.getY(), 2);
            //Distance equation Skywalker - Sato
            double e = -2 * SKYWALKER.getX() + 2 * SATO.getX();
            double f = -2 * SKYWALKER.getY() + 2 * SATO.getY();
            double g = pow(distances[1], 2) - pow(distances[2], 2) - pow(SKYWALKER.getX(), 2) + pow(SATO.getX(), 2) - pow(SKYWALKER.getY(), 2) + pow(SATO.getY(), 2);
            //Finding Cramer's rule determinants
            double d = a * f - b * e;
            if (d == 0.0)
                throw new IllegalArgumentException(getMessage(distances));
            double dx = c * f - b * g;
            double dy = a * g - c * e;
            float x = BigDecimal.valueOf(dx / d).setScale(2, HALF_UP).floatValue();
            float y = BigDecimal.valueOf(dy / d).setScale(2, HALF_UP).floatValue();
            log.info("Calculate trilateration status=FINISH distances={}, Spaceship Coordinates=({};{})", Arrays.toString(distances), x, y);
            return new Location(x, y);
        } catch(final Exception ex) {
            log.error("Calculate trilateration status=ERROR distances={}", Arrays.toString(distances), ex);
            throw new SpaceshipPositionNotFoundException(ex.getMessage());
        }
    }

    private String getMessage(final Float... distances) {
        final String message = join(distances, ",");
        return messageSource.getMessage(
                COULD_NOT_FIND_SPACESHIP_COORDINATES,
                new String[]{message},
                getDefault());
    }
}