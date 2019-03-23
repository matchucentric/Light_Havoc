import java.io.*;
import java.util.ArrayList;

public class Filefetcher {

	public Filefetcher() {}

	public static ArrayList<String> showFiles(String dir) {
		ArrayList<String> fileOut = new ArrayList<String>();
		File[] files = new File(dir).listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            //showFiles(file.listFiles()); // Calls same method again.
	        } else {
	            fileOut.add(file.getName());
	        }
	    }
	    return fileOut;
  	}
}