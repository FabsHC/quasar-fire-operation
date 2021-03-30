package com.quasarfireoperation.usecase;

import com.quasarfireoperation.domains.Location;
import com.quasarfireoperation.domains.exception.SpaceshipPositionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.quasarfireoperation.domains.SatelliteCoordinate.*;
import static java.lang.Math.pow;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Locale.getDefault;
import static org.apache.commons.lang3.StringUtils.join;

@Component
@RequiredArgsConstructor
public class CalculateLocationFromSatellites {

    private static final String COULD_NOT_FIND_SPACESHIP_COORDINATES = "could.not.find.spaceship.coordinates";
    private final MessageSource messageSource;

    public Location getLocation(final Float ...distances) {
        try {
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
            if (d == 0.0) {
                final String message = join(distances, ",");
                throw new IllegalArgumentException(
                        messageSource.getMessage(
                                COULD_NOT_FIND_SPACESHIP_COORDINATES,
                                new String[]{message},
                                getDefault()));
            }
            double dx = c * f - b * g;
            double dy = a * g - c * e;
            float x = BigDecimal.valueOf(dx / d).setScale(2, HALF_UP).floatValue();
            float y = BigDecimal.valueOf(dy / d).setScale(2, HALF_UP).floatValue();
            return new Location(x, y);
        } catch(final Exception ex) {
            throw new SpaceshipPositionNotFoundException(ex.getMessage());
        }
    }
}