package main;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import crawler.Crawler;
import excel.Excel;
import exceptions.CrawlerException;
import sokos.SideMenuItem;
import sokos.Sokos;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	
	public static void main(String[] args) throws CrawlerException {
		Crawler crawler = new Crawler(logger);
		Sokos sokos = new Sokos();
		Excel excel = new Excel();
		if (crawler.openSokos()) {
			ArrayList<SideMenuItem> menuItems = sokos.getSideMenuItems();
			for (SideMenuItem menuItem : menuItems) {
				if (!excel.fileExists(menuItem.getExcelTitle())) {
					// TODO: Crawler stuff
					continue;
				} else {
					logger.info("Already handled Excel file '" + menuItem.getExcelTitle() + "' on the crashed run, continuing to the next one.");
				}
			}
		}	
	}
}
