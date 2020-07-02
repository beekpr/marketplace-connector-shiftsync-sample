package io.beekeeper.connector.shiftsync.sample.client;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * This guy is expected to be called before request is sent, and it would add current token to the header
 */
@RequiredArgsConstructor
public class AccessTokenInterceptor implements Interceptor {
    private final String apiToken;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request requestWithAuthorization = withAuthorizationHeader(chain.request(), apiToken);
        return chain.proceed(requestWithAuthorization);
    }

    @NotNull
    public static Request withAuthorizationHeader(Request request, String apiToken) {
        return request
            .newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", String.format("Bearer %s", apiToken))
            .build();
    }
}
