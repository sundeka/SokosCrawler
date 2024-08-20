package sokos;

public class SideMenuItem {
	private Category category;
	private String excelTitle;
	private String sideBarTitle;
	private String xpath;
	
	public SideMenuItem(Category category) {
		this.category = category;
		String commonXpath = "//div[text()='%s']/../..";
		switch (category) {
			case Category.FRUITS_AND_VEGGIES:
				this.setExcelTitle("hedelmat_ja_vihannekset");
				this.setSideBarTitle("Hedelmät ja vihannekset");
				break;
			case Category.BREADS_COOKIES_AND_PASTRIES:
				this.setExcelTitle("leivat_keksit__ja_leivonnaiset");
				this.setSideBarTitle("Leivät, keksit ja leivonnaiset");
				break;
			case Category.MEAT_AND_PROTEIN:
				this.setExcelTitle("liha_ja_kasvisproteiinit");
				this.setSideBarTitle("Liha ja kasvisproteiinit");
				break;
			case Category.FISH:
				this.setExcelTitle("kala_ja_merenelavat");
				this.setSideBarTitle("Kala ja merenelävät");
				break;
			case Category.MILK_EGGS_FATS:
				this.setExcelTitle("maito_munat_ja_rasvat");
				this.setSideBarTitle("Maito, munat ja rasvat");
				break;
			case Category.CHEESE:
				this.setExcelTitle("juustot");
				this.setSideBarTitle("Juustot");
				break;
			case Category.PASTA_RICE_AND_NOODLES:
				this.setExcelTitle("pastat_riisit_ja_nuudelit");
				this.setSideBarTitle("Pastat, riisit ja nuudelit");
				break;
			case Category.OILS_AND_SPICES:
				this.setExcelTitle("oljyt_mausteet_ja_maustaminen");
				this.setSideBarTitle("Öljyt, mausteet, maustaminen");
				break;
			case Category.DRINKS:
				this.setExcelTitle("juomat");
				this.setSideBarTitle("Juomat");
				break;
			case Category.SNACKS:
				this.setExcelTitle("snacksit");
				this.setSideBarTitle("Snacksit");
				break;
			default:
				break;
		}
		this.setXpath(String.format(commonXpath, this.sideBarTitle));
	}
	
	public Category getCategory() {
		return this.category;
	}

	public String getExcelTitle() {
		return excelTitle;
	}

	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}

	public String getSideBarTitle() {
		return sideBarTitle;
	}

	public void setSideBarTitle(String sideBarTitle) {
		this.sideBarTitle = sideBarTitle;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
}
