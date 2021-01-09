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

public class Handler {

    private IDirectoryScanner directoryScanner;
    private IUploader uploader;

    public Handler() {
        this.directoryScanner = new DirectoryScanner();
        this.uploader = new Uploader();
    }

    // Engine of program
    public void parseAndUploadNewFiles() {
        System.out.println("Handler loop initiated. Running engine ...");

        while (true) {
            System.out.println("\nBegin loop ...");
            File file = directoryScanner.searchForNewFile();
            if (file != null) {
                System.out.println("Found new file, + " + file.getName());
                System.out.println("Pausing program for x minutes before uploading to give mp4 file time to decode.");
                //delay(3600);     // Set one hour delay for zoom video to decode.

                System.out.println("Program resume. Preparing to upload to Youtube ...");


                boolean videoUploaded = false;
                try {
                    ApiExample.main(file);
                    videoUploaded = true;
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // 10 minute delay because file can't be moved while it is uploading.
                delay(600);
                if (videoUploaded) {
                    System.out.println("Success! Video uploaded to Youtube.");
                    directoryScanner.moveFileToFolder(file, Folder.VIDEOS_UPLOADED);
                } else {
                    System.out.println("Upload to Youtube failed");
                    directoryScanner.moveFileToFolder(file, Folder.VIDEOS_UNABLE_TO_UPLOAD);
                }
            } else {
                System.out.println("No new file found.");
                System.out.println("End loop. Will loop again in x minutes.");
            }

            delay(1800);     // Delay loop to only run every 30 minutes.
        }
    }

    // 60ms is one minute, 120ms is two minutes, 3600ms is an hour, etc.
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("Loop is running faster than expected. This may cause issues if the Zoom video is uploaded before it's been decoded");
            e.printStackTrace();
        }
    }

}
