package edu.java.bot.retrying.backoff;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LinearBackoff implements Backoff {
    private final Duration constant;

    @Override
    public Duration calculateWaitTime(int attempt) {
        return constant.multipliedBy(attempt);
    }
}
