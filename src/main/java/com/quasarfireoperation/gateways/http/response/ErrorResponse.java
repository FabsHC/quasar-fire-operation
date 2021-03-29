package com.quasarfireoperation.gateways.http.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private List<String> error;

    public void addError(final String message) {
        if (isNull(this.error))
            error = new LinkedList<>();
        error.add(message);
    }
}
