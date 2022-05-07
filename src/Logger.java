import java.io.PrintStream;

public class Logger {
    public PrintStream outStream;
    public LogLevel level;

    public Logger(LogLevel level) {
        this(level, System.out);
    }
    public Logger(LogLevel level, PrintStream outStream) {
        if (outStream == null)
            throw new IllegalArgumentException("`outStream` is null");
        this.outStream = outStream;

        setLevel(level);
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void print(LogLevel level, String format, Object... args) {
        // level should not be NONE
        if (level.leq(this.level))
            outStream.printf(format, args);
    }
}
