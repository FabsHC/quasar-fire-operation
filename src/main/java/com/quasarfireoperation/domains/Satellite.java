package com.quasarfireoperation.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Satellite {
    private String name;
    private float distance;
    private Location location;
    private List<String> message;
}
