package youtube.file.interfaces;

import youtube.file.enums.Folder;

import java.io.File;

public interface IDirectoryScanner {

    public File searchForNewFile();
    public void moveFileToFolder(File file, Folder folder);

}
