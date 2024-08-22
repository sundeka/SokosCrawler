package crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerStringUtils {
	Pattern patternKcal; 
	Pattern patternGrams;
	Pattern patternIntakePercentage;
	
	public CrawlerStringUtils() {
		this.patternKcal = Pattern.compile("\\d+(?=\\s*kcal)");
		this.patternGrams = Pattern.compile("\\d+(?:,\\d+)?(?=\\s+g)");
		this.patternIntakePercentage =  Pattern.compile("\\d{1,3}(?:,\\d+)?");
	}
	
	/**
	 * Convert the raw (string) value parsed from the HTML page to double
	 * Depending on whether it's the value for kcal, grams or recommended intake
	 * 
	 * Examples of format:
	 * Kcal: "23 kcal"
	 * Grams: "0,3 g"
	 * Recommended intake: "0,18%"
	 * @param attr
	 * @param rawText
	 * @return
	 */
	public double convertToDouble(Attribute attr, String rawText) {
		double value = 0.0;
		switch(attr) {
			case Attribute.KCAL:
				Matcher kcalMatcher = patternKcal.matcher(rawText);
		        if (kcalMatcher.find()) {
		            value = Double.parseDouble(kcalMatcher.group());
		        }
		        break;
			case Attribute.GRAMS:
				Matcher gramsMatcher = patternGrams.matcher(rawText);
		        if (gramsMatcher.find()) {
		            value = Double.parseDouble(gramsMatcher.group().replace(",", "."));
		        }
				break;
			case Attribute.RECOMMENDED_INTAKE:
				Matcher riMatcher = patternIntakePercentage.matcher(rawText);
		        if (riMatcher.find()) {
		            value = Double.parseDouble(riMatcher.group().replace(",", "."));
		        }
				break;
			default:
				break;
		}
		return value;
	}
}
