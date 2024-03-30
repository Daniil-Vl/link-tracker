package edu.java.retrying;

import edu.java.retrying.backoff.Backoff;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RetryFilter implements ExchangeFilterFunction {
    private final Backoff backoff;
    private final Set<HttpStatus> errorHttpStatuses;
    private final int maxAttempts;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return retry(request, next, 1);
    }

    private Mono<ClientResponse> retry(ClientRequest request, ExchangeFunction next, int attempt) {
        return next
            .exchange(request)
            .flatMap(clientResponse -> {
                HttpStatus httpStatusCode = (HttpStatus) clientResponse.statusCode();

                if (errorHttpStatuses.contains(httpStatusCode) && attempt <= maxAttempts) {
                    Duration waitTime = backoff.calculateWaitTime(attempt);
                    Mono<ClientResponse> defer = Mono.defer(() -> retry(request, next, attempt + 1));
                    return Mono.delay(waitTime).then(defer);
                }

                return Mono.just(clientResponse);
            });
    }
}
