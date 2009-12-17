package nugoh.util.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 5:26:30 PM
 */
public abstract class FileWatcher implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(FileWatcher.class);

    private final File file;
    private final long interval;
    private long lastModified = 0;

    public FileWatcher(File file) {
        this(file, 5000);
    }

    public FileWatcher(File file, long interval) {
        this.file = file;
        this.interval = interval;
    }

    protected abstract void onChange(File file);

    @Override
    public void run() {
        try {
            while (true) {
                if (lastModified != file.lastModified()) {
                    lastModified = file.lastModified();
                    logger.debug("file modified: " + file.getAbsolutePath());
                    onChange(file);
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            logger.debug("FileWatcher interrupted: " + file.getAbsolutePath());
        }
    }
}