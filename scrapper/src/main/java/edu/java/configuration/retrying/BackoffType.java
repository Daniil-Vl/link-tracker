package edu.java.configuration.retrying;

import edu.java.retrying.backoff.Backoff;
import edu.java.retrying.backoff.ConstantBackoff;
import edu.java.retrying.backoff.ExponentialBackoff;
import edu.java.retrying.backoff.LinearBackoff;
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
