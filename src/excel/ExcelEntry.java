package excel;

import java.util.HashMap;

public class ExcelEntry {
	String name;
	double kcal;
	double fat;
	double fatSaturated;
	double carbs;
	double fiber;
	double protein;
	double salt;
	double riKcal;
	double riFat;
	double riFatSaturated;
	double riCarbs;
	double riFiber;
	double riProtein;
	double riSalt;
	
	public ExcelEntry(String name, HashMap<String, double[]> data) {
		this.name = name;
		if (data.size() != 0) {
			this.kcal = data.get("Energiaa")[0];
			this.riKcal = data.get("Energiaa")[1];
			this.fat = data.get("Rasvaa")[0];
			this.riFat = data.get("Rasvaa")[1];
			this.fatSaturated = data.get("Rasvaa, josta tyydyttyneitä rasvoja")[0];
			this.riFatSaturated = data.get("Rasvaa, josta tyydyttyneitä rasvoja")[1];
			this.carbs = data.get("Hiilihydraattia")[0];
			this.riCarbs = data.get("Hiilihydraattia")[1];
			this.fiber = data.get("Ravintokuitua")[0];
			this.riFiber = data.get("Ravintokuitua")[1];
			this.protein = data.get("Proteiinia")[0];
			this.riProtein = data.get("Proteiinia")[1];
			this.salt = data.get("Suolaa")[0];
			this.riSalt = data.get("Suolaa")[1];
		}
	}
}
