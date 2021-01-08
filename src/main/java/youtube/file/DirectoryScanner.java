package youtube.file;

import youtube.file.interfaces.IDirectoryScanner;
import youtube.file.interfaces.ITrackedVideosLogger;

import java.io.File;

public class DirectoryScanner implements IDirectoryScanner {

    private ITrackedVideosLogger trackedVideosLogger;
    private static final String directoryToScan = "../../";
    private static final String videosUploadedName = "video-uploaded";
    private static final String videosUnableToUploadName = "video-unable-to-upload";

    public DirectoryScanner(ITrackedVideosLogger trackedVideosLogger) {
        this.trackedVideosLogger = trackedVideosLogger;
        verifyFoldersInDirectory();
    }

    // If first time running program, it will create subfolders.
    // Returns false if folders are not in directory and were not able to be created.
    public boolean verifyFoldersInDirectory() {
        System.out.println("Checking if subfolders exist ...");
        File directory = new File(directoryToScan);
        boolean foldersExist = verifyFoldersExist(directory);
        if (!foldersExist) {
            foldersExist = createFolders();
        }
        return foldersExist;
    }

    private boolean verifyFoldersExist(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Error: Could not find any files in the given directory.");
            return false;
        }

        int counter = 0;
        for (File f : files) {
            if (f.getName().equals(videosUploadedName) || f.getName().equals(videosUnableToUploadName)) {
                counter++;
            }
            if (counter == 2) {
                System.out.println("Verified that the needed subfolders are in the directory.");
                return true;
            }
        }
        return false;
    }

    private boolean createFolders() {

        System.out.println("Created 2 subfolders named " + videosUploadedName + " and " + videosUnableToUploadName + ". " + "Videos will be uploaded to these folders depending on if they were successfully uploaded or not.");
        return false;
    }

    public File searchForNewFile() {
        return null;
    }

}
