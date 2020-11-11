package synalogik.TextFileReader;

import java.io.FileNotFoundException;

/**
 * Main start point of app, set the FILEPATH to point to the text file and run.
 * @author Mat
 *
 */
public class AppMain {

	public static final String FILEPATH = "C:\\youre\\file\\path\\here.txt";
	
    public static void main( String[] args ) throws FileNotFoundException
    {
        TextFileReader reader = new TextFileReader(FILEPATH);
        reader.readInFile();
        reader.printResults();
    }
}
