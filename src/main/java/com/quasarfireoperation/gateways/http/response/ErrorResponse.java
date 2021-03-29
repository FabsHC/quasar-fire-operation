package com.quasarfireoperation.gateways.http.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private List<String> error = new ArrayList();
}
