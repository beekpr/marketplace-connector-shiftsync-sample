package io.beekeeper.connector.shiftsync.sample;

import io.beekeeper.integration.connector.api.OngoingSyncDataProvider;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.api.exception.ConfigurationException;
import io.beekeeper.integration.connector.api.exception.PauseFailedException;
import io.beekeeper.integration.connector.api.exception.ResumeFailedException;
import io.beekeeper.integration.connector.api.exception.StartupException;
import io.beekeeper.integration.connector.shiftsync.api.ongoing.OngoingShiftImportConnector;
import io.beekeeper.integration.connector.shiftsync.api.ongoing.observer.OngoingScheduleImportObserver;
import io.beekeeper.integration.connector.shiftsync.api.ongoing.observer.OngoingShiftImportObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

public class SampleOngoingConnector implements OngoingShiftImportConnector {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    Thread thread = new Thread() {
        @SneakyThrows
        @Override
        public void run() {
            int i = 0;
            while (true) {
                if (isPaused.get()) {
                    Thread.sleep(100);
                } else {
                    System.out.println(ANSI_YELLOW_BACKGROUND + "Ongoing sync is running. Message number " + i++ + ANSI_RESET);
                    Thread.sleep(1000);
                }
            }
        }
    };

    AtomicBoolean isPaused = new AtomicBoolean();

    @Override
    public void start(OngoingScheduleImportObserver scheduleObserver, OngoingShiftImportObserver shiftObserver)
            throws StartupException {
        thread.start();
    }

    @Override
    public void initialize(GlobalConfiguration globalConfiguration, OngoingSyncDataProvider ongoingSyncDataProvider)
            throws ConfigurationException {
    }

    @Override
    public void stop() {
    }

    @Override
    public void pause(String externalTenantId, boolean isThereAnyOtherFullSyncInProgress) throws PauseFailedException {
        isPaused.set(true);
        System.out.println(ANSI_RED + "Pausing ongoing import" + ANSI_RESET);
    }

    @Override
    public void resume(String externalTenantId, boolean isThereAnyOtherFullSyncInProgress)
            throws ResumeFailedException {
        isPaused.set(false);
        System.out.println(ANSI_RED + "Resuming ongoing import" + ANSI_RESET);
    }
}
