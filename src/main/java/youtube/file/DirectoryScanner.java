package youtube.file;

import youtube.file.interfaces.IDirectoryScanner;
import youtube.file.interfaces.ITrackedVideosLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class DirectoryScanner implements IDirectoryScanner {

    private ITrackedVideosLogger trackedVideosLogger;
    private static final File directory = new File("../../");
    private static final File videosUploadedName = new File(directory.getPath() + "/videos-uploaded");
    private static final File videosUnableToUploadName = new File(directory.getPath() + "/videos-unable-to-upload");

    public DirectoryScanner(ITrackedVideosLogger trackedVideosLogger) {
        this.trackedVideosLogger = trackedVideosLogger;
        verifyFoldersInDirectory();
    }

    // Creates 2 folders in directory where video files will go in. Does nothing if folders already exist.
    private void verifyFoldersInDirectory() {
        System.out.println("Verifying subfolders ...");
        boolean fileOneCreated = videosUploadedName.mkdir();
        boolean fileTwoCreated = videosUnableToUploadName.mkdir();
        if (fileOneCreated && fileTwoCreated) {
            System.out.println("Created and verified subfolders, " + videosUploadedName.getName() +
                    " and " + videosUnableToUploadName.getName() + ", are in directory. " +
                    "Videos will be moved to these folders depending on if they were successfully uploaded or not.");
            return;
        }

        // Files weren't created. Check if they are already created from previous time program was ran.
        int detectedFolders = 0;
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Unable to verify or create subfolders that videos are supposed to be moved to.");
            return;
        }
        for (File f : files) {
            if (f.equals(videosUploadedName) || f.equals(videosUnableToUploadName)) {
                detectedFolders++;
            }
        }
        if (detectedFolders == 2) {
            System.out.println("Verified subfolders are in directory.");
        } else {
            System.out.println("Unable to verify or create subfolders that videos are supposed to be moved to.");
        }
    }

    public File searchForNewFile() {
        File[] files = directory.listFiles();
        if (files == null) return null;

        for (File f : files) {
            if (isMP4File(f)) {
                return f;
            }
        }

        return null;
    }

    public void moveFileToFolder(File file, int place) {
        Path pathFrom = file.toPath();
        Path pathTo = Paths.get(videosUnableToUploadName.toString() + "/" + file.getName());
        try {
            Files.move(pathFrom, pathTo);
            System.out.println("yes");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("nope");
        }
    }

    private boolean isMP4File(File file) {
        String fileName = file.getName();
        System.out.println("Name is " + fileName);
        String[] split = fileName.split("\\.");
        if (split.length < 2) return false;

        String fileType = split[split.length - 1];      // Get text after the last "." because that is the file type.
        if (fileType.equals("mp4")) return true;
        return false;
    }

}
