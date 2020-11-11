package synalogik.TextFileReader;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TextFileReaderTest {
	
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	
	public TextFileReader reader;
	
	@Test
	public void testSampleString() throws IOException {
		String inputText = "Hello world & good morning. The date is 18/05/2016";
		int expectedWordCount = 9;
		int expectedMaxFrequency = 2;
		String expectedAverage = "4.556";
		
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		// assert all values are as expected
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
		
	}
	
	@Test
	public void testSimpleString_onlyAlphabetical() throws IOException {
		String inputText = "This is basic text";
		int expectedWordCount = 4;
		int expectedMaxFrequency = 2;
		String expectedAverage = "3.750";
		
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		// assert all values are as expected
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
		
	}
	
	@Test
	public void testInvalidWordInString() throws IOException {
		String inputText = "*** The stars should not *** count";
		int expectedWordCount = 5;
		int expectedMaxFrequency = 2;
		String expectedAverage = "4.400";
		
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
		
	}
	
	@Test
	public void testTrimmingPunctuation() throws IOException {
		String inputText = "This. Is. A: Rubbish! Sentence?";
		int expectedWordCount = 5;
		int expectedMaxFrequency = 1;
		String expectedAverage = "4.400";
		
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
	}
	
	@Test
	public void testFileWithNewLines() throws IOException {
		String inputText = "This text \nIs split over \nA few lines";
		int expectedWordCount = 8;
		int expectedMaxFrequency = 3;
		String expectedAverage = "3.500";
				
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
	}
	
	@Test
	public void testEmptyLines() throws IOException {
		String inputText = "\n\nCan I handle empty \n\nlines?";
		int expectedWordCount = 5;
		int expectedMaxFrequency = 2;
		String expectedAverage = "4.000";
		
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
	}
	
	@Test
	public void testEmptyFile() throws IOException {
		String inputText = "";
		int expectedWordCount = 0;
		int expectedMaxFrequency = 0;
		String expectedAverage = "0.000";
		
		File myFile = createTempFileWithText(inputText);
		reader = new TextFileReader(myFile.getAbsolutePath());
		
		assertFiguresAreCorrect(reader, expectedWordCount, expectedMaxFrequency, expectedAverage);
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testInvalidFilepath() throws FileNotFoundException {
		reader = new TextFileReader("ThisDoesntActuallyExist");
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testNullFilePath() throws FileNotFoundException {
		reader = new TextFileReader(null);
	}
	
	private void assertFiguresAreCorrect(TextFileReader reader, int expectedWordCount, int expectedMaxFrequency, 
			String expectedAverage) {
		assertEquals(expectedWordCount, reader.getWordCount());
		assertEquals(expectedMaxFrequency, reader.findMaxCountFromMap());
		assertEquals(expectedAverage, reader.calculateAverageWordLength());
	}
	
	private File createTempFileWithText(String fileText) throws IOException {
		// create temp file for test, and give it the test text
		File myFolder = testFolder.newFolder("my-folder");
		File myFile = new File(myFolder, "testInput.txt");
		FileWriter writer = new FileWriter(myFile);
		
		writer.write(fileText);
		writer.close();
		
		return myFile;
	}

}
