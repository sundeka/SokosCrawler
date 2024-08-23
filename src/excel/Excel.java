package excel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	private String filePath;
	private Workbook workbook;
	
	public Excel(Path excelFolder, String filename, Logger logger) {
		Path dir = Paths.get(excelFolder + filename + ".xlsx");
		this.setFilePath(dir.toString() + "\\" + filename + ".xlsx");
		this.workbook = new XSSFWorkbook();
		logger.info("Created a new Excel file: " + this.getFilePath());
	}
	
	/**
	 * 
	 * @param fileName: the suffix-less filename of the file e.g. "snacks"
	 * @return boolean
	 */
	public boolean fileExists() {
		return false;
	}
	
	public void createHeaderRow(String headerTitle) {
		
	}
	
	public void append(ExcelEntry entry) {
		
	}
	
	public void close() {
		try {
			this.workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
	
