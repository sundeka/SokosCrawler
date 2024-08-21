package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import crawler.Crawler;
import excel.Excel;
import exceptions.CrawlerException;
import sokos.SideMenuItem;
import sokos.Sokos;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	
	public static void main(String[] args) throws CrawlerException,InterruptedException {
		Crawler crawler = new Crawler(logger);
		Sokos sokos = new Sokos();
		Excel excel = new Excel("/path/here");
		crawler.openSokos();
		for (SideMenuItem menuItem : sokos.getSideMenuItems()) {
			if (excel.fileExists(menuItem.getExcelTitle())) {
				logger.info("Found Excel file for " + menuItem.getSideBarTitle() + ". Continuing to the next one...");
				continue;
			}
			crawler.openCategory(menuItem);
			for (String subMenuTitle : menuItem.getSubMenuTitles()) {
				crawler.openSubMenu(subMenuTitle);
				//for item in items
				//	open_item()
				//	raw_data = scrape()
				//	ExcelEntry entry = excel.parse_data_from_raw(raw_data)
				//	excel.append(menuItem.getExcelTitle(), entry)
			}
		}
		crawler.quit();
		logger.info("Run finished successfully!");
		logger.info("The Excel files can be found at: " + excel.getPath().toString());
	}
}
