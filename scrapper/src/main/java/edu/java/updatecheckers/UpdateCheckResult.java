package edu.java.updatecheckers;

public record UpdateCheckResult(
    boolean hasUpdate,
    String description
) {
    public static UpdateCheckResult getDefault() {
        return new UpdateCheckResult(false, null);
    }
}
