package sokos;

import java.util.ArrayList;

public class Sokos {
	private ArrayList<SideMenuItem> sideMenuItems = new ArrayList<SideMenuItem>();
	private Category[] categories;
	
	public Sokos() {
		categories = Category.values();
		for (Category category : categories) {
			sideMenuItems.add(new SideMenuItem(category));
		}
	}
	
	public ArrayList<SideMenuItem> getSideMenuItems() {
		return this.sideMenuItems;
	}
}
