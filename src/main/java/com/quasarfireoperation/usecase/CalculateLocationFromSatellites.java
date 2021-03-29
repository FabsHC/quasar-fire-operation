package com.quasarfireoperation.usecase;

import com.quasarfireoperation.domains.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.quasarfireoperation.domains.SatelliteCoordinate.*;
import static java.lang.Math.*;
import static java.lang.System.out;

@Component
@RequiredArgsConstructor
public class CalculateLocationFromSatellites {

    private static final String satelites = "Kenobi[%.2f,%.2f] - Skywalker[%.2f,%.2f] - Sato[%.2f,%.2f]";
    public Location getLocation(final float ...distances) {
        out.println("Distances: " + Arrays.toString(distances));
        out.println(String.format(satelites,
                KENOBI.getX(), KENOBI.getY(),
                SKYWALKER.getX(), SKYWALKER.getY(),
                SATO.getX(), SATO.getY()));
        out.println();

        double aKSk = toDegrees(atan2((KENOBI.getY()- SKYWALKER.getY()), (KENOBI.getX()- SKYWALKER.getX())));
        double aKSa = toDegrees(atan2((KENOBI.getY()- SATO.getY()), (KENOBI.getX()- SATO.getX())));
        double aSkK = toDegrees(atan2((SKYWALKER.getY()- KENOBI.getY()), (SKYWALKER.getX()- KENOBI.getX())));
        double aSkSa = toDegrees(atan2((SKYWALKER.getY()- SATO.getY()), (SKYWALKER.getX()- SATO.getX())));
        double aSaK = toDegrees(atan2((SATO.getY()- KENOBI.getY()), (SATO.getX()- KENOBI.getX())));
        double aSaSk = toDegrees(atan2((SATO.getY()- SKYWALKER.getY()), (SATO.getX()- SKYWALKER.getX())));
        double media = (aKSk + aKSa + aSkSa + aSkK + aSaK + aSaSk)/6;

        out.println("Azimuth Kenobi-Skywalker: "+aKSk);
        out.println("Azimuth Kenobi-Sato: "+aKSa);
        out.println("Azimuth Skywalker-Kenobi: "+aSkK);
        out.println("Azimuth Skywalker-Sato: "+aSkSa);
        out.println("Azimuth Sato-Kenobi: "+aSaK);
        out.println("Azimuth Sato-Skywalker: "+aSaSk);
        out.println("Azimuths: "+media);
        out.println();

        float x,y;
        for (float distance : distances) {
            x = ((float) sin(media)) * distance;
            y = ((float) cos(media)) * distance;
            out.println("Azimuth Kenobi-Skywalker : x -> " + x + " / y -> " + y);
            x = ((float) sin(aKSa)) * distance;
            y = ((float) cos(aKSa)) * distance;
            out.println("Azimuth Kenobi-Sato: x -> " + x + " / y -> " + y);
            x = ((float) sin(aSkSa)) * distance;
            y = ((float) cos(aSkSa)) * distance;
            out.println("Azimuth Skywalker-Sato: x -> " + x + " / y -> " + y);
            out.println();
        }
        return new Location(100, -100);
    }

    public static void main(String[] args) {
        new CalculateLocationFromSatellites().getLocation(100, 115.5f, 142.7f );
    }
}
