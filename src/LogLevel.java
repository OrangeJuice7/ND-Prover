public enum LogLevel {
    NONE(0),
    WARNING(1),
    STATUS(2);

    public final int val;

    LogLevel(int val) {
        this.val = val;
    }

    public boolean leq(LogLevel other) {
        return val <= other.val;
    }
}
