package com.quasarfireoperation.usecase;

import com.quasarfireoperation.domains.exception.DecryptMessageException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.Locale.getDefault;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
@RequiredArgsConstructor
public class DecryptSatelliteMessages {

    private static final String COULD_NOT_DECRYPT_MESSAGE = "could.not.decrypt.message";
    private final MessageSource messageSource;

    public String getMessage(final List<List<String>> messages) {
        final List<String> mergedMessage = new LinkedList<>();
        try {
            for (int i = 0; i < messages.size(); i++) {
                List<String> actualMessage = messages.get(i);
                for (int j = 0; j < actualMessage.size(); j++) {
                    if (isBlank(actualMessage.get(j))) {
                        for (int k = i + 1; k < messages.size(); k++) {
                            mergeMessage(messages.get(k).get(j), mergedMessage);
                        }
                    } else
                        mergeMessage(actualMessage.get(j), mergedMessage);
                }
                if (mergedMessage.size() == actualMessage.size())
                    break;
            }
            if (mergedMessage.stream().anyMatch(Strings::isBlank))
                throw new IllegalArgumentException();
        } catch (final Exception e) {

            throw new DecryptMessageException(
                    messageSource.getMessage(
                            COULD_NOT_DECRYPT_MESSAGE,
                            null,
                            getDefault())
            );
        }
        return Arrays.toString(mergedMessage.toArray());
    }

    private void mergeMessage(final String message, final List<String> mergedMessage) {
        if (isNotBlank(message)) {
            mergedMessage.add(message);
        }
    }
}
