package synalogik.TextFileReader;

import java.io.FileNotFoundException;

/**
 * 
 * @author Mat
 *
 */
public class AppMain {

	public static final String FILEPATH = "C:\\Users\\Mat\\Desktop\\testBible.txt";
	
    public static void main( String[] args ) throws FileNotFoundException
    {
        TextFileReader reader = new TextFileReader(FILEPATH);
        reader.printResults();
    }
}
