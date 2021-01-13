package youtube;

import youtube.api.ApiExample;
import youtube.api.Uploader;
import youtube.api.interfaces.IUploader;
import youtube.file.DirectoryScanner;
import youtube.file.enums.Folder;
import youtube.file.interfaces.IDirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

public class Handler {

    private static final Logger logger = Logger.getGlobal();

    private IDirectoryScanner directoryScanner;
    private IUploader uploader;


    public Handler() {
        this.directoryScanner = new DirectoryScanner();
        this.uploader = new Uploader();
    }

    // Engine of program
    public void parseAndUploadNewFiles() {
        logger.info("Handler loop initiated. Running engine ...");

        while (true) {
            logger.info("Begin loop ...");
            File file = directoryScanner.searchForNewFile();
            if (file != null) {
                logger.info("Found new file, " + file.getName() + ". Pausing program for x minutes before uploading to give mp4 file time to decode.");
                delay(3600);     // Set one hour delay for zoom video to decode.

                logger.info("Program resume.");
                boolean uploadSuccessful = upload(file);
                moveFileToFolder(file, uploadSuccessful);
            } else {
                logger.info("Did not detect a file to upload.");
            }

            int delay = 1800;       // Delay 30 minutes so loop isn't running too often.
            logger.info("End loop. Will loop again in " + delay + " milliseconds.");
            delay(delay);
        }
    }

    private boolean upload(File file) {
        logger.info("Preparing to upload to Youtube ...");
//        try {
//            ApiExample.main(file);
//            delay(600);      // 10 minute delay so that program doesn't move file while uploading. (Will cause exception otherwise.)
//            logger.info("Success! Video uploaded.");
//            return true;
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
//        logger.warning("Upload failed.");
        return false;
    }

    // There are 2 folders. This moves the file to the success folder or the failed folder, so there is a directoy log of which videos are uploaded.
    private void moveFileToFolder(File file, boolean uploadSuccessful) {
        if (uploadSuccessful) {
            logger.info("Moving file to + " + Folder.VIDEOS_UPLOADED.toString());
            directoryScanner.moveFileToFolder(file, Folder.VIDEOS_UPLOADED);
        } else {
            logger.info("Moving file to + " + Folder.VIDEOS_UNABLE_TO_UPLOAD.toString());
            directoryScanner.moveFileToFolder(file, Folder.VIDEOS_UNABLE_TO_UPLOAD);
        }
    }

    // 60ms is one minute, 120ms is two minutes, 3600ms is an hour, etc.
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds * 1000);
        } catch (InterruptedException e) {
            logger.info("Loop is running faster than expected. This may cause issues if the Zoom video is uploaded before it's been decoded");
            e.printStackTrace();
        }
    }

}
