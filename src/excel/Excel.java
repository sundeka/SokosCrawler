package excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import exceptions.ExcelException;

public class Excel {
	private Path filePath;
	private Workbook workbook;
	private Sheet sheet;

	
	public Excel(Path excelFolder, String filename, Logger logger) throws ExcelException {
		this.filePath = excelFolder.resolve(filename + ".xlsx");
		if (fileExists()) {
			throw new ExcelException("File '" + filename + ".xlsx' already exists!");
		}
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet("Data");
		saveToFile();
		logger.info("Created a new Excel file: " + this.filePath.toString());
	}
	
	public void createHeaderRow(String headerTitle) {
		int rowNum = this.sheet.getLastRowNum();
		
		// No divider row needed for the first entry
		if (rowNum != -1) {
			rowNum += 1;
			Row dividerRow = this.sheet.createRow(rowNum);
			Cell dividerCell = dividerRow.createCell(0);
			dividerCell.setCellValue("");
		}
		
		Row titleRow = this.sheet.createRow(rowNum + 1);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(headerTitle);
		
		Row columnRow = this.sheet.createRow(rowNum + 2);
		for (int i=0;i<14;i++) {
			Cell cell = columnRow.createCell(i);
			if (i == 0) {
				cell.setCellValue("name");
			} else if (i == 1) {
				cell.setCellValue("kcal");
			} else if (i == 2) {
				cell.setCellValue("fat");
			} else if (i == 3) {
				cell.setCellValue("fat saturated");
			} else if (i == 4) {
				cell.setCellValue("carbs");
			} else if (i == 5) {
				cell.setCellValue("fiber");
			} else if (i == 6) {
				cell.setCellValue("protein");
			} else if (i == 7) {
				cell.setCellValue("salt");
			} else if (i == 8) {
				cell.setCellValue("kcal RI");
			} else if (i == 9) {
				cell.setCellValue("fat RI");
			} else if (i == 10) {
				cell.setCellValue("fat saturated RI");
			} else if (i == 11) {
				cell.setCellValue("carbs RI");
			} else if (i == 12) {
				cell.setCellValue("fiber RI");
			} else if (i == 13) {
				cell.setCellValue("protein RI");
			} else if (i == 14) {
				cell.setCellValue("salt RI");
			}
		}
		saveToFile();
	}
	
	public void createDataRow(ExcelEntry entry) {
		int rowNum = this.sheet.getLastRowNum() + 1; // Starts at -1 which causes error...?
		Row dataRow = this.sheet.createRow(rowNum);
		for (int i=0;i<14;i++) {
			Cell cell = dataRow.createCell(i);
			if (i == 0) {
				cell.setCellValue(entry.name);
			} else if (i == 1) {
				cell.setCellValue(entry.kcal);
			} else if (i == 2) {
				cell.setCellValue(entry.fat);
			} else if (i == 3) {
				cell.setCellValue(entry.fatSaturated);
			} else if (i == 4) {
				cell.setCellValue(entry.carbs);
			} else if (i == 5) {
				cell.setCellValue(entry.fiber);
			} else if (i == 6) {
				cell.setCellValue(entry.protein);
			} else if (i == 7) {
				cell.setCellValue(entry.salt);
			} else if (i == 8) {
				cell.setCellValue(entry.riKcal);
			} else if (i == 9) {
				cell.setCellValue(entry.riFat);
			} else if (i == 10) {
				cell.setCellValue(entry.riFatSaturated);
			} else if (i == 11) {
				cell.setCellValue(entry.riCarbs);
			} else if (i == 12) {
				cell.setCellValue(entry.riFiber);
			} else if (i == 13) {
				cell.setCellValue(entry.riProtein);
			} else if (i == 14) {
				cell.setCellValue(entry.riSalt);
			}
		}
		saveToFile();
	}
	
	public void close() {
		try {
			this.workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean fileExists() {
		File file = new File(this.filePath.toString());
		return file.exists();
	}
	
	private void saveToFile() {
		try (FileOutputStream fileOut = new FileOutputStream(this.filePath.toString())) {
            this.workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
	
