package edu.java.bot.retrying.backoff;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstantBackoff implements Backoff {
    private final Duration constant;

    @Override
    public Duration calculateWaitTime(int attempt) {
        return constant;
    }
}
