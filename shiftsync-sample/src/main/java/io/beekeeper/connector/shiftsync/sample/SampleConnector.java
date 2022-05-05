package io.beekeeper.connector.shiftsync.sample;

import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.shiftsync.api.ShiftImportConnector;
import io.beekeeper.integration.connector.shiftsync.api.importer.ScheduleProvider;
import io.beekeeper.integration.connector.shiftsync.api.importer.ShiftProvider;

public class SampleConnector implements ShiftImportConnector {
    @Override
    public ShiftProvider getShiftProvider(GlobalConfiguration globalConfiguration) {
        return SampleShiftImporter.builder()
            .globalConfiguration(globalConfiguration)
            .build();
    }

    @Override
    public ScheduleProvider getScheduleProvider(GlobalConfiguration globalConfiguration) {
        return SampleScheduleImporter.builder()
            .globalConfiguration(globalConfiguration)
            .build();
    }
}
