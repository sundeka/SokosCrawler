package ocr;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.sikuli.script.*;

public class OcrFunctions {
	/**
	 * This class exists merely because once ChromeDriver opens itself and the site,
	 * there is a nasty <shadow-root> component that is simply inaccessible via the web driver.
	 */
	private Path ocrImagesDirectory = Paths.get("C:\\Users\\sunde\\eclipse-workspace\\sokosCrawler\\ocr\\");
	private Pattern imageToFind;
	private Match match;
	
	public void closePossibleDefaultBrowserPrompt(Logger logger) {
		Screen screen = new Screen();
		try {
			imageToFind = new Pattern(ocrImagesDirectory.toString() + "\\chrome_selection.PNG");
		    match = screen.find(imageToFind);
		    match.click();
		    imageToFind = new Pattern(ocrImagesDirectory.toString() + "\\set_as_default.PNG");
		    match = screen.find(imageToFind);
		    match.click();
		} catch (FindFailed e) {
			logger.warn("No 'Select default browser' prompt detected, even though it was expected!");
		}
	}
	
	public void closeCookieSettingsPrompt(Logger logger) {
		Screen screen = new Screen();
		try {
			imageToFind = new Pattern(ocrImagesDirectory.toString() + "\\allow_cookies.PNG");
			match = screen.find(imageToFind);
			match.click();
		} catch (FindFailed e) {
			logger.warn("No cookie prompt detected, even though it was expected!");
		}
	}
}
