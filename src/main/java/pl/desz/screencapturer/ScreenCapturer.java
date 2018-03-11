package pl.desz.screencapturer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Captures screen shots and saves
 * them into specified directory.
 *
 * Defaults to home_dir/screen_captures,
 * if none specified.
 */
public class ScreenCapturer {

    private static final Logger LOG = Logger.getLogger(ScreenCapturer.class.getName());

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss__dd_MM_yyyy");

    private Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    private String fileFormat = "jpg";

    private String picName;
    private String destDir;
    private Robot robot;

    public ScreenCapturer() {

        String homepath = System.getenv("HOMEPATH");

        Path screen_captures = Paths.get(homepath).resolve("screen_captures");
        this.destDir = screen_captures.toString();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalStateException("cannot crete an image capturer", e);
        }
    }

    public ScreenCapturer(String destinationDir) {
        // setup
        this();
        // make sure dest dir exists
        if (! Files.exists(Paths.get(destinationDir))) {
            try {
                Files.createDirectories(Paths.get(destinationDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // override def consturctor setup
        this.destDir = destinationDir;
    }

    public BufferedImage capture() {
        return robot.createScreenCapture(screenRect);
    }

    public void save(BufferedImage image, String formatName) {
        try {
            File screenShotFileName = getFileName(formatName);
            // save
            ImageIO.write(image, formatName, screenShotFileName);
        } catch (IOException e) {
            throw new RuntimeException("Could not save an image.", e);
        }
    }

    private File getFileName(String formatName) {
        // before saving update timestamp
        picName = dateTimeFormatter.format(LocalDateTime.now()) + "_pic";
        // prepare file with timestamp in name
        return Paths.get(destDir).resolve(picName + "." + formatName).toFile();
    }

    public void captureAndSave() {
        BufferedImage image = capture();
        save(image, fileFormat);
    }
}
