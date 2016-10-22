package sample.task;

import javafx.concurrent.Task;

import java.util.Date;

import org.joda.time.Interval;
import org.joda.time.Period;

/**
 * Created by Botan on 22/10/2016.
 */
public class Timer extends Task<Void> {
    private final Date startTime = new Date();

    @Override
    protected Void call() throws Exception {
        for(int i = 0; i < 1;i--) {
            Period period = new Interval(startTime.getTime(), new Date().getTime()).toPeriod();
            updateTitle("Elapsed time h " + period.getHours() + " m " + period.getMinutes() + " s " + period.getSeconds());
        }
        return null;
    }
}
