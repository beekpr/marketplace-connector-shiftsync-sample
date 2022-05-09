package io.beekeeper.connector.shiftsync.sample.e2e.recorded;

import io.beekeeper.connector.shiftsync.sample.SampleConnector;
import io.beekeeper.integration.connector.shiftsync.api.domain.Schedule;
import io.beekeeper.integration.connector.shiftsync.api.domain.Shift;
import io.beekeeper.integration.test.shift.ShiftSyncExecutorTestSupport;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordedIntegrationTest {
    @Test
    public void importWithMockedDataShouldReturnExpectedSchedulesAndShifts() throws Exception {
        // Given
        final SampleConnector sampleConnector = new SampleConnector();

        // When
        var schedulesWithShifts = new ShiftSyncExecutorTestSupport()
            .performImport(sampleConnector, "happyPath");

        // Then
        assertThat(schedulesWithShifts.getSchedules()).hasSize(1);
        Optional<Schedule> schedule = schedulesWithShifts.getSchedules().stream().findFirst();
        assertThat(schedule).isNotEmpty();
        Collection<Shift> shifts = schedulesWithShifts.getShiftsForSchedule(schedule.get());
        assertThat(shifts).isNotEmpty();
        shifts.stream()
            .forEach(shift -> {
                assertThat(shift.getId()).isNotBlank();
                assertThat(shift.getTitle()).isNotBlank();
                assertThat(shift.getDescription()).isNotBlank();
                assertThat(shift.getExternalUserId()).isNotBlank();
                assertThat(shift.getStartAndEnd()).isNotNull();
                assertThat(shift.getStartAndEnd().getEnd()).isAfter(shift.getStartAndEnd().getStart());
            });
    }
}
