package edu.java.bot.configuration.retrying;

import edu.java.bot.retrying.backoff.Backoff;
import edu.java.bot.retrying.backoff.ConstantBackoff;
import edu.java.bot.retrying.backoff.ExponentialBackoff;
import edu.java.bot.retrying.backoff.LinearBackoff;
import java.time.Duration;

public enum BackoffType {
    CONSTANT,
    LINEAR,
    EXPONENTIAL;

    public Backoff getBackoff(Duration minBackoff) {
        return switch (this) {
            case CONSTANT -> new ConstantBackoff(minBackoff);
            case LINEAR -> new LinearBackoff(minBackoff);
            case EXPONENTIAL -> new ExponentialBackoff(minBackoff);
        };
    }
}
