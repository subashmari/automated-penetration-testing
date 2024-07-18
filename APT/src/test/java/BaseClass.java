import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BaseClass {

    public static File getLatestFile(String folderPath, String extension) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path or directory does not exist.");
            return null;
        }

        System.out.println("Before listing files");
        File[] filesArray = folder.listFiles((dir, name) -> name.endsWith(extension));
        System.out.println("After listing files");

        if (filesArray == null || filesArray.length == 0) {
            System.out.println("No files with the specified extension found in the folder.");
            return null;
        }

        // Convert the array to a modifiable list
        List<File> fileList = new ArrayList<>(Arrays.asList(filesArray));

        // Get the latest file based on last modified time
        fileList.sort(Comparator.comparingLong(File::lastModified).reversed());

        System.out.println("Latest file: " + fileList.get(0).getName());

        return fileList.get(0);
    }
}
