package io.cdap.wrangler.api.parser;
import io.cdap.wrangler.api.parser.Token;

public class TimeDuration extends Token {
    private final long milliseconds;

    public TimeDuration(String value) {
        super(value);
        this.milliseconds = parseMilliseconds(value.toLowerCase());
    }

    private long parseMilliseconds(String value) {
        if (value.endsWith("h")) {
            return (long) (Double.parseDouble(value.replace("h", "")) * 3600000);
        } else if (value.endsWith("m")) {
            return (long) (Double.parseDouble(value.replace("m", "")) * 60000);
        } else if (value.endsWith("s")) {
            return (long) (Double.parseDouble(value.replace("s", "")) * 1000);
        } else if (value.endsWith("ms")) {
            return Long.parseLong(value.replace("ms", ""));
        } else if (value.endsWith("ns")) {
            return Long.parseLong(value.replace("ns", "")) / 1000000;
        }
        throw new IllegalArgumentException("Invalid time duration: " + value);
    }

    public long getMilliseconds() {
        return milliseconds;
    }
}
