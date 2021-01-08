package youtube.file;

import youtube.file.interfaces.IDirectoryScanner;
import youtube.file.interfaces.ITrackedVideosLogger;

import java.io.File;

public class DirectoryScanner implements IDirectoryScanner {

    private ITrackedVideosLogger trackedVideosLogger;
    private static final String directoryToScan = "../../";
    private static final String videosUploadedName = "videos-uploaded";
    private static final String videosUnableToUploadName = "videos-unable-to-upload";

    public DirectoryScanner(ITrackedVideosLogger trackedVideosLogger) {
        this.trackedVideosLogger = trackedVideosLogger;
        verifyFoldersInDirectory();
    }

    // Creates 2 folders in directory where video files will go in. If folders already exist, does nothing.
    private void verifyFoldersInDirectory() {
        File child1 = new File(directoryToScan + videosUploadedName);
        File child2 = new File(directoryToScan + videosUnableToUploadName);
        child1.mkdir();
        child2.mkdir();
        System.out.println("Verified subfolders, " + videosUploadedName + " and " + videosUnableToUploadName + ", are in directory. " + "Videos will be uploaded to these folders depending on if they were successfully uploaded or not.");
    }

    public File searchForNewFile() {
        return null;
    }

}
