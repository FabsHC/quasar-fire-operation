package com.quasarfireoperation.usecase;

import com.quasarfireoperation.domains.exception.DecryptMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.Locale.getDefault;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Component
@RequiredArgsConstructor
public class DecryptSatelliteMessages {

    private static final String COULD_NOT_DECRYPT_MESSAGE = "could.not.decrypt.message";
    private final MessageSource messageSource;

    public String getMessage(final List<List<String>> messages) {
        log.info("Decrypting spaceship message status=START, messages={}",messages);
        final List<String> mergedMessage = new LinkedList<>();
        try {
            String messagePart;
            for (int i = 0; i < messages.size(); i++) {
                List<String> actualMessage = messages.get(i);
                for (int j = 0; j < actualMessage.size(); j++) {
                    messagePart = actualMessage.get(j);
                    if (isBlank(actualMessage.get(j))) {
                        for (int k = i + 1; k < messages.size(); k++) {
                            messagePart = messages.get(k).get(j);
                            if (isNotBlank(messagePart))
                                break;
                        }
                    }
                    mergedMessage.add(messagePart);
                }
                if (mergedMessage.size() == actualMessage.size())
                    break;
            }
            if (mergedMessage.stream().anyMatch(Strings::isBlank))
                throw new IllegalArgumentException();
            log.info("Decrypting spaceship message status=FINISH, messages={}, decrypted message={}", messages, mergedMessage);
        } catch (final Exception e) {
            log.error("Decrypting spaceship message status=ERROR, messages={}", messages, e);
            throw new DecryptMessageException(
                    messageSource.getMessage(
                            COULD_NOT_DECRYPT_MESSAGE,
                            null,
                            getDefault())
            );
        }
        return Arrays.toString(mergedMessage.toArray());
    }
}
