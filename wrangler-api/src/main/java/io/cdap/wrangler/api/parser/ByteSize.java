package io.cdap.wrangler.api.parser;
import io.cdap.wrangler.api.parser.Token;

public class ByteSize extends Token {
    private final long bytes;

    public ByteSize(String value) {
        super(value);
        this.bytes = parseBytes(value.toUpperCase());
    }

    private long parseBytes(String value) {
        if (value.endsWith("TB")) {
            return (long) (Double.parseDouble(value.replace("TB", "")) * Math.pow(1024, 4));
        } else if (value.endsWith("GB")) {
            return (long) (Double.parseDouble(value.replace("GB", "")) * Math.pow(1024, 3));
        } else if (value.endsWith("MB")) {
            return (long) (Double.parseDouble(value.replace("MB", "")) * Math.pow(1024, 2));
        } else if (value.endsWith("KB")) {
            return (long) (Double.parseDouble(value.replace("KB", "")) * 1024);
        } else if (value.endsWith("B")) {
            return Long.parseLong(value.replace("B", ""));
        }
        throw new IllegalArgumentException("Invalid byte size: " + value);
    }

    public long getBytes() {
        return bytes;
    }
}
