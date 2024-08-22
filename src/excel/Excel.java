package excel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Excel {
	Path path;
	
	public Excel(String path) {
		this.path = Paths.get(path);
	}
	
	/**
	 * 
	 * @param fileName: the suffix-less filename of the file e.g. "snacks"
	 * @return boolean
	 */
	public boolean fileExists(String fileName) {
		return false;
	}
	
	public void append(String fileName, String itemName, HashMap<String, double[]> data) {
		
	}
	
	public Path getPath() {
		return this.path;
	}
}
