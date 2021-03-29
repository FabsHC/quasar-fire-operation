package com.quasarfireoperation.usecase;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
public class DecryptSatelliteMessages {

    public String getMessage(final List<String> ...messages) {
        final List<String> mergedMessage = new LinkedList<>();
        for (int i = 0; i < messages.length; i++) {
            List<String> actualMessage = messages[i];
            for (int j = 0; j<actualMessage.size(); j++){
                if (isBlank(actualMessage.get(j))) {
                    for (int k = i + 1; k < messages.length; k++) {
                        mergeMessage(messages[k].get(j), mergedMessage);
                    }
                } else
                    mergeMessage(actualMessage.get(j), mergedMessage);
            }
            if (mergedMessage.size()==actualMessage.size())
                break;
        }
        return Arrays.toString(mergedMessage.toArray());
    }

    private void mergeMessage(final String message, final List<String> mergedMessage) {
        if (isNotBlank(message)) {
            mergedMessage.add(message);
        }
    }

    public static void main(String[] args) {
        out.println(new DecryptSatelliteMessages().getMessage(
                Arrays.asList("este", "", "", "mensaje", ""),
                Arrays.asList("", "es", "", "", "secreto"),
                Arrays.asList("este", "", "un", "", "")
        ));
    }
}
