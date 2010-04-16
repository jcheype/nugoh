package nugoh.service.folderwatcher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FolderWatcher {
    private final Logger logger = LoggerFactory.getLogger(FolderWatcher.class);

    private boolean isRecursive = false;
    private String filter = "[^\\.].*";
    private Pattern pattern;

    private Map<String, Long> lastModifiedMap = new HashMap();
    private long interval = 5000;
    private File folder;


    public void setRecursive(String recursive) {
        isRecursive = "true".equals(recursive);
    }

    public String getRecursive() {
        return ""+isRecursive;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public void setInterval(String intervalString) {
        this.interval = Long.parseLong(intervalString);
    }

    public String getInterval() {
        return ""+interval;
    }

    public void setFolder(String folderPath) {
        this.folder = new File(folderPath);
    }

    public void init() {
        pattern = Pattern.compile(filter);
    }

    public synchronized void run(Map<String, Object> context) throws InterruptedException {
        File file = scanFolder(folder);
        context.put("data", file.getAbsolutePath()); 
    }

    private File scanFolder(File folder) throws InterruptedException {
        while (true) {
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
                            return file;
                        }
                    }
                }
            }
            Thread.sleep(interval);
        }
    }
}