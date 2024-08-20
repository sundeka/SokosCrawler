package main;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import crawler.Crawler;
import exceptions.CrawlerException;
import sokos.SideMenuItem;
import sokos.Sokos;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	
	public static void main(String[] args) throws CrawlerException {
		Crawler crawler = new Crawler(logger);
		Sokos sokos = new Sokos();
		if (crawler.openSokos()) {
			ArrayList<SideMenuItem> menuItems = sokos.getSideMenuItems();
			for (SideMenuItem menuItem : menuItems) {
				System.out.println(menuItem.getSideBarTitle());
				System.out.println(menuItem.getExcelTitle());
				System.out.println("---");
			}
		}	
	}
}
