package pl.desz.screencapturer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        ScreenCapturer sc;

        if (args.length == 0) {
            sc = new ScreenCapturer();
        } else {
            sc = new ScreenCapturer(args[0]);
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // capture screen shot from main monitor in fixed rate
        executorService.scheduleAtFixedRate(sc::captureAndSave, 0L, 5L, TimeUnit.SECONDS);
    }
}
