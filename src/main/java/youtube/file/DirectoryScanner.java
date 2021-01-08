package youtube.file;

import youtube.file.interfaces.IDirectoryScanner;
import youtube.file.interfaces.ITrackedVideosLogger;

import java.io.File;

public class DirectoryScanner implements IDirectoryScanner {

    ITrackedVideosLogger trackedVideosLogger;
    private static final String directoryToScan = "../../";

    public DirectoryScanner(ITrackedVideosLogger trackedVideosLogger) {
        this.trackedVideosLogger = trackedVideosLogger;
        createSubFolders();
    }

    // If first time running program, it will create subfolders.
    // Returns false if folders are not in directory and were not able to be created.
    public boolean createSubFolders() {
        return false;
    }

    public File searchForNewFile() {
        return null;
    }

}
