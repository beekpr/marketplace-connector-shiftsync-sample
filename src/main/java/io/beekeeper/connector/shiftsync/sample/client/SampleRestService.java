package io.beekeeper.connector.shiftsync.sample.client;

import io.beekeeper.connector.shiftsync.sample.external.model.ExternalSchedule;
import io.beekeeper.connector.shiftsync.sample.external.model.ExternalShift;
import lombok.Value;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface SampleRestService {
    @GET("api/1/schedules")
    Call<GetSchedulesResponse> getSchedules();

    @GET("api/1/shifts")
    Call<GetShiftsResponse> getShifts(@Query("scheduleId") String scheduleId);

    @Value
    class GetSchedulesResponse {
        List<ExternalSchedule> schedules;
    }

    @Value
    class GetShiftsResponse {
        List<ExternalShift> shifts;
    }
}
