package youtube.file;

import youtube.file.enums.Folder;
import youtube.file.interfaces.IDirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

public class DirectoryScanner implements IDirectoryScanner {

    private static final Logger logger = Logger.getGlobal();

    // Note! All file paths are relative to this project's location. Use ../ to go out of root folder.
    private static final File directory = new File("../../");
    private static final File videosUploadedName = new File(directory.getPath() + "/videos-uploaded");
    private static final File videosUnableToUploadName = new File(directory.getPath() + "/videos-unable-to-upload");

    public DirectoryScanner() {
        verifyFoldersInDirectory();
    }

    // Creates 2 folders in directory where video files will go in. Does nothing if folders already exist.
    private void verifyFoldersInDirectory() {
        logger.info("Verifying subfolders ...");
        boolean fileOneCreated = videosUploadedName.mkdir();
        boolean fileTwoCreated = videosUnableToUploadName.mkdir();
        if (fileOneCreated && fileTwoCreated) {
            logger.info("Created and verified subfolders, " + videosUploadedName.getName() +
                    " and " + videosUnableToUploadName.getName() + ", are in directory. " +
                    "Videos will be moved to these folders depending on if they were successfully uploaded or not.");
            return;
        }

        // Files weren't created. Check if they are already created from previous time program was ran.
        int detectedFolders = 0;
        File[] files = directory.listFiles();
        if (files == null) {
            logger.info("Unable to verify or create subfolders that videos are supposed to be moved to.");
            return;
        }
        for (File f : files) {
            if (f.equals(videosUploadedName) || f.equals(videosUnableToUploadName)) {
                detectedFolders++;
            }
        }
        if (detectedFolders == 2) {
            logger.info("Verified subfolders are in directory.");
        } else {
            logger.info("Unable to verify or create subfolders that videos are supposed to be moved to.");
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
            logger.info("File successfully moved to folder.");
        } catch (IOException e) {
            logger.info("Unable to move file " + file.getName() + " to folder. The problem may be that the file was still uploading when trying to move it.");
            e.printStackTrace();
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
