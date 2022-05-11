package io.beekeeper.connector.shiftsync.sample;

public class ShiftSyncException extends RuntimeException {
    public ShiftSyncException(String message) {
        super(message);
    }

    public ShiftSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
