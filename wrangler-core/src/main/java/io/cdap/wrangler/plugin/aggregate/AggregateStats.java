package io.cdap.wrangler.plugin.aggregate;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;

import java.util.List;
import java.util.stream.Collectors;

public class AggregateStats implements Directive, Aggregate {

    private String byteSizeCol;
    private String timeDurationCol;
    private String outputSizeCol;
    private String outputTimeCol;

    private long totalBytes = 0;
    private long totalMilliseconds = 0;

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder("aggregate-stats")
                .addRequiredArg("byte_size_col")
                .addRequiredArg("time_duration_col")
                .addRequiredArg("output_size_col")
                .addRequiredArg("output_time_col")
                .build();
    }

    @Override
    public void initialize(Arguments arguments) {
        byteSizeCol = arguments.value("byte_size_col");
        timeDurationCol = arguments.value("time_duration_col");
        outputSizeCol = arguments.value("output_size_col");
        outputTimeCol = arguments.value("output_time_col");
    }

    @Override
    public AggregateOutput aggregate(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        for (Row row : rows) {
            String byteSizeVal = row.getValue(byteSizeCol).toString();
            String timeDurationVal = row.getValue(timeDurationCol).toString();

            ByteSize byteSize = new ByteSize(byteSizeVal);
            TimeDuration timeDuration = new TimeDuration(timeDurationVal);

            totalBytes += byteSize.getBytes();
            totalMilliseconds += timeDuration.getMilliseconds();
        }

        double totalMB = totalBytes / (1024.0 * 1024.0);
        double totalSeconds = totalMilliseconds / 1000.0;

        Row outputRow = new Row();
        outputRow.add(outputSizeCol, totalMB);
        outputRow.add(outputTimeCol, totalSeconds);

        return new AggregateOutput(List.of(outputRow));
    }
}
