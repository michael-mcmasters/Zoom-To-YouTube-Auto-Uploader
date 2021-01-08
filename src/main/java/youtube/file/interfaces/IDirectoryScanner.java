package youtube.file.interfaces;

import java.io.File;

public interface IDirectoryScanner {

    public File searchForNewFile();
    public void moveFileToFolder(File file, int place);

}
