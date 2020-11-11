package synalogik.TextFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class takes a filepath as a String and attempts to parse its contents, constructing stats about the text
 *  as it goes. A word in the file is deemed to be any space separated string that doesn't solely contain symbols,
 *  unless it is a single joining symbol such as &. 
 * @author Mat
 *
 */
public class TextFileReader {
	
	private HashMap<Integer, Integer> wordLengthCounts = new HashMap<Integer, Integer>();

	private int wordCount;
	
	private String filepath;
	
	private int totalWordsLength;
	
	/**
	 * Constructor takes in the filepath and initialises variables.
	 * @param filepath : string pointing to the text file that should be parsed
	 * @throws FileNotFoundException 
	 */
	public TextFileReader(String filepath) throws FileNotFoundException {
		this.filepath = filepath;
	}
	
	/**
	 * Tries to read the file and handle each line until the whole document has been read.
	 * @throws FileNotFoundException 
	 */
	public void readInFile() throws FileNotFoundException {
		// Reset the count variables when a new file is read
		wordCount = 0;
		totalWordsLength = 0;
		wordLengthCounts = new HashMap<Integer, Integer>();
		if (getFilepath() == null) {
			throw new FileNotFoundException("Filepath cannot be null, please enter a valid filepath.");
		}
		// Try and read in the file and parse each line
		File file = new File(getFilepath());
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				parseFileLine(line);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Could not find file for given filepath: " + getFilepath());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Collate all the stats about the text and output them.
	 */
	public void printResults() {
		// maxWordLengthCount is stored as an arrayList as we don't know how many word lengths will occur the most times
		ArrayList<String> mostCommonWordLengths = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int maxLengthCount = findMaxCountFromMap();
		
		//build output string
		sb.append("Word Count = ").append(getWordCount());
		sb.append("\nAverage word length = ").append(calculateAverageWordLength());
		for (Map.Entry<Integer, Integer> entry : getWordLengthCounts().entrySet()) {
			sb.append("\nNumber of words of length " + entry.getKey() + " is " + entry.getValue());
			if (entry.getValue() == maxLengthCount) {
				mostCommonWordLengths.add(entry.getKey().toString());
			}
		}
		sb.append("\nThe most frequently occuring word length appears " + maxLengthCount + " times for words of length " + String.join(" & ", mostCommonWordLengths));
		System.out.println(sb.toString());
	}
	
	/**
	 * Split the string into individual words, sanitise the words and count the necessary stats
	 * @param fileLine : String containing the line of text currently being processed
	 */
	private void parseFileLine(String fileLine) {
		// Split the line from the file based on white space
		String[] splitLine = fileLine.split(" ");
		for (String word : splitLine){
			if (word.isEmpty()) {
				continue;
			}
			// Trim the word and skip if it is determined to not be a word (returns null)
			String trimmedWord = trimWord(word);
			if (trimmedWord == null) {
				continue;
			}
			
			// Increment word count, add length of word to map, increment count if it already exists in the map
			wordCount++;
			int wordLength = trimmedWord.length();
			if (wordLengthCounts.containsKey(wordLength)){
				wordLengthCounts.put(wordLength, wordLengthCounts.get(wordLength) + 1);
			} else {
				wordLengthCounts.put(wordLength, 1);
			}
			
			// Store the total word length for calculating average later
			totalWordsLength += wordLength;
		}
	}
	
	/**
	 * Trims any unnecessary characters from a word, including trailing punctuation and white space.
	 * Then determines if what is left is a valid word according to my interpretation.
	 * @param word : String of the current word in the text file being processed
	 * @return either the trimmed word or null if it isn't determined to be a word
	 */
	private String trimWord(String word) {
		String trimmedWord = word.trim();
		// If the word isn't a single character (such as &) trim it from trailing punctuation
		if (word.length() > 1) {
			trimmedWord = trimmedWord.replaceAll("[,.?!:;]$", "");
		}
		// Return the word if after being trimmed it still contains alphanumeric characters,
		// or if it is a single non alphanumeric character, otherwise return null
		if (trimmedWord.matches(".+[a-zA-Z0-9]")){
			return trimmedWord;
		} else if (trimmedWord.length() == 1) {
			return trimmedWord;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Finds the highest occurring word length in the text
	 * @return int
	 */
	public int findMaxCountFromMap() {
		// Loop through the word length count map and determine what the highest value is
		int maxCount = 0;
		for (Integer lengthCount : wordLengthCounts.values()) {
			if (lengthCount > maxCount) {
				maxCount = lengthCount;
			}
		}
		return maxCount;
	}
	
	/**
	 * Calculates the average word length from the text, converts it to a string, and returns up to 3
	 *  decimal places.
	 * @return
	 */
	public String calculateAverageWordLength() {
		if (totalWordsLength == 0 || wordCount == 0) {
			return "0.000";
		}
		double average = (double)totalWordsLength / (double)wordCount;
		return String.format("%.3f", average);
	}

	public int getWordCount() {
		return wordCount;
	}

	private HashMap<Integer, Integer> getWordLengthCounts() {
		return wordLengthCounts;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getTotalWordsLength() {
		return totalWordsLength;
	}
		
}
