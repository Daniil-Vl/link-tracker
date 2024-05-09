package edu.java.bot.retrying.backoff;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExponentialBackoff implements Backoff {
    private final Duration constant;

    @Override
    public Duration calculateWaitTime(int attempt) {
        return Duration.ofSeconds(
            (long) Math.pow(constant.getSeconds(), attempt)
        );
    }
}
