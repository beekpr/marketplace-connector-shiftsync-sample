package io.beekeeper.connector.shiftsync.sample.client;

import io.beekeeper.connector.shiftsync.sample.ShiftSyncException;
import io.beekeeper.connector.shiftsync.sample.external.model.ExternalSchedule;
import io.beekeeper.connector.shiftsync.sample.external.model.ExternalShift;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Collection;

@AllArgsConstructor
public class SampleRestServiceClient {
    private final SampleRestService sampleRestService;

    public Collection<ExternalSchedule> getSchedules() {
        try {
            final Call<SampleRestService.GetSchedulesResponse> schedules = sampleRestService.getSchedules();
            final Response<SampleRestService.GetSchedulesResponse> response = schedules.execute();
            if (!response.isSuccessful()) {
                throwAnException(response, "Unable to fetch schedules");
            }
            return response.body().getSchedules();
        } catch (IOException ioException) {
            throw new ShiftSyncException("Error when doing http call", ioException);
        }
    }

    public Collection<ExternalShift> getShiftsForSchedule(String scheduleId) {
        try {
            final Call<SampleRestService.GetShiftsResponse> shifts = sampleRestService.getShifts(scheduleId);
            final Response<SampleRestService.GetShiftsResponse> response = shifts.execute();
            if (!response.isSuccessful()) {
                throwAnException(response, "Unable to fetch shifts");
            }
            return response.body().getShifts();
        } catch (IOException ioException) {
            throw new ShiftSyncException("Error when doing http call", ioException);
        }
    }

    public static SampleRestServiceClient create(String baseUrl, String apiToken) {
        final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AccessTokenInterceptor(apiToken))
            .build();

        final SampleRestService sampleRestService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build()
            .create(SampleRestService.class);
        return new SampleRestServiceClient(sampleRestService);
    }

    private void throwAnException(Response<?> execute, String baseMessage) throws IOException {
        throw new ShiftSyncException(
                String.format(
                    "%s. Error code: '%s', message: '%s'",
                    baseMessage,
                    execute.code(),
                    execute.errorBody() == null ? "" : execute.errorBody().string()
                )
        );
    }
}
