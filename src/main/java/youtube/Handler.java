package youtube;

import youtube.api.Uploader;
import youtube.api.interfaces.IUploader;
import youtube.file.DirectoryScanner;
import youtube.file.TrackedVideosLogger;
import youtube.file.interfaces.IDirectoryScanner;
import youtube.file.interfaces.ITrackedVideosLogger;

import java.io.File;

public class Handler {

    private IDirectoryScanner directoryScanner;
    private ITrackedVideosLogger trackedVideosLogger;
    private IUploader uploader;

    public Handler() {
        this.trackedVideosLogger = new TrackedVideosLogger();
        this.directoryScanner = new DirectoryScanner(trackedVideosLogger);
        this.uploader = new Uploader();
    }

    // Engine of program
    public void parseAndUploadNewFiles() {
        System.out.println("Handler loop initiated. Running engine ...");

        while (true) {
            System.out.println("\nBegin loop ...");
            File file = directoryScanner.searchForNewFile();
            if (file != null) {
                System.out.println("Found new file. Pausing program for x minutes before uploading to give mp4 file time to decode.");

                // Set one hour delay for zoom video to decode.
                delay(3600);

                System.out.println("Program resume. Preparing to upload to Youtube ...");


                // try uploading to youtube. return true or false if it worked.
                // If it worked, add to videos-uploaded directory.
                // If it didn't work, add to videos-unable-to-upload directory.
                //System.out.println("Success! Video uploaded to Youtube.");
                //System.out.println("Upload to Youtube failed");
            } else {
                System.out.println("No new file found.");
                System.out.println("End loop. Will loop again in x minutes.");
            }

            // Delay so loop isn't running constantly.
            delay(1800);
        }
    }

    // 60s is one minute, 120 is two minutes, 3600 is an hour, etc.
    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("Loop is running faster than expected. This may cause issues if the Zoom video is uploaded before it's been decoded");
            e.printStackTrace();
        }
    }

}
