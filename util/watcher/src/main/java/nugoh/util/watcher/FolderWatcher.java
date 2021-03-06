package nugoh.util.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 10:42:23 PM
 */
public abstract class FolderWatcher implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(FileWatcher.class);

    private final long interval;
    private final File rootFolder;

    private boolean isRecursive = false;
    private String filter = "[^\\.].*";
    private Pattern pattern;

    private Map<String, Long> lastModifiedMap = new HashMap();

    public FolderWatcher(File rootFolder) {
        this(rootFolder, 5000);
    }

    public FolderWatcher(File rootFolder, long interval) {
        this.interval = interval;
        this.rootFolder = rootFolder;
        if (!rootFolder.isDirectory()) {
            throw new IllegalStateException("Root folder must be a folder");
        }
    }

    @Override
    public void run() {
        pattern = Pattern.compile(filter);
        try {
            while (true) {
                scanFolder(rootFolder);
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            logger.info("FileWatcher interrupted");
        }
    }

    private void scanFolder(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                if (isRecursive) {
                    scanFolder(file);
                }
            } else {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    Long lastModified = lastModifiedMap.get(file.getAbsolutePath());
                    if (lastModified == null || lastModified != file.lastModified()) {
                        logger.debug("file modified: " + file.getAbsolutePath());
                        lastModifiedMap.put(file.getAbsolutePath(), file.lastModified());
                        onChange(file);
                    }
                }
            }
        }
    }

    protected abstract void onChange(File file);
}
