package youtube.file;

import youtube.file.enums.Folder;
import youtube.file.interfaces.IDirectoryScanner;
import youtube.file.interfaces.ITrackedVideosLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class DirectoryScanner implements IDirectoryScanner {

    private ITrackedVideosLogger trackedVideosLogger;

    // Note! All file paths are relative to this project's location. Use ../ to go out of root folder.
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

    private boolean isMP4File(File file) {
        String fileName = file.getName();
        String[] split = fileName.split("\\.");
        if (split.length < 2) return false;

        String fileType = split[split.length - 1];      // Get text after the last "." because that is the file type.
        if (fileType.equals("mp4")) return true;
        return false;
    }




    public void moveFileToFolder(File file, Folder folder) {
        Path pathFrom = file.toPath();
        Path pathTo = getPathTo(file, folder);
        try {
            Files.move(pathFrom, pathTo);
            System.out.println("File successfully moved to folder.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to move file to folder.");
        }
    }

    // toString() returns file path. file.getName() returns file name. So it comes out as something similar to ...
    // toString:        "../../documents/zoom-videos"
    // file.getName():  "/recorded-video-1.9.21.mp4"
    // Concatenated together equals: "../../documents/zoom-videos/recorded-video-1.9.21.mp4"
    private Path getPathTo(File file, Folder folder) {
        String pathStr = "";
        switch(folder) {
            case VIDEOS_UNABLE_TO_UPLOAD:
                pathStr = videosUnableToUploadName.toString() + "/" + file.getName();
                break;
            case VIDEOS_UPLOADED:
                pathStr = videosUploadedName.toString() + "/" + file.getName();
                break;
        }
        return Paths.get(pathStr);
    }

}
