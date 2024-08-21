package excel;

import java.nio.file.Path;
import java.nio.file.Paths;

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
	
	public Path getPath() {
		return this.path;
	}
}
