package sokos;

import java.util.ArrayList;

public class SideMenuItem {
	private Category category;
	private String excelTitle;
	private String sideBarTitle;
	private String xpath;
	private ArrayList<String> subMenuTitles = new ArrayList<String>();
	
	public SideMenuItem(Category category) {
		this.category = category;
		String commonXpath = "//div[text()='%s']/../..";
		switch (category) {
			case Category.FRUITS_AND_VEGGIES:
				this.setExcelTitle("hedelmat_ja_vihannekset");
				this.setSideBarTitle("Hedelmät ja vihannekset");
				this.subMenuTitles.add("Hedelmät");
				this.subMenuTitles.add("Vihannekset");
				this.subMenuTitles.add("Juurekset");
				this.subMenuTitles.add("Marjat");
				this.subMenuTitles.add("Sienet");
				break;
			case Category.BREADS_COOKIES_AND_PASTRIES:
				this.setExcelTitle("leivat_keksit__ja_leivonnaiset");
				this.setSideBarTitle("Leivät, keksit ja leivonnaiset");
				this.subMenuTitles.add("Leivät");
				this.subMenuTitles.add("Korput ja rinkelit");
				break;
			case Category.MEAT_AND_PROTEIN:
				this.setExcelTitle("liha_ja_kasviproteiinit");
				this.setSideBarTitle("Liha ja kasviproteiinit");
				this.subMenuTitles.add("Jauheliha");
				this.subMenuTitles.add("Kana, broileri ja kalkkuna");
				this.subMenuTitles.add("Nauta");
				this.subMenuTitles.add("Porsas");
				this.subMenuTitles.add("Kinkut ja leikkeleet");
				this.subMenuTitles.add("Makkarat, nakit ja pekonit");
				break;
			case Category.FISH:
				this.setExcelTitle("kala_ja_merenelavat");
				this.setSideBarTitle("Kala ja merenelävät");
				this.subMenuTitles.add("Kala");
				this.subMenuTitles.add("Äyriäiset");
				break;
			case Category.MILK_EGGS_FATS:
				this.setExcelTitle("maito_munat_ja_rasvat");
				this.setSideBarTitle("Maito, munat ja rasvat");
				this.subMenuTitles.add("Maidot ja piimät");
				this.subMenuTitles.add("Kermat");
				this.subMenuTitles.add("Jogurtit");
				this.subMenuTitles.add("Viilit");
				this.subMenuTitles.add("Rahkat, vanukkaat ja jälkiruoka");
				this.subMenuTitles.add("Munat");
				this.subMenuTitles.add("Rasvat");
				break;
			case Category.CHEESE:
				this.setExcelTitle("juustot");
				this.setSideBarTitle("Juustot");
				this.subMenuTitles.add("Pala- ja viipalejuustot");
				break;
			case Category.PASTA_RICE_AND_NOODLES:
				this.setExcelTitle("pastat_riisit_ja_nuudelit");
				this.setSideBarTitle("Pastat, riisit ja nuudelit");
				this.subMenuTitles.add("Pastat");
				this.subMenuTitles.add("Nuudelit");
				this.subMenuTitles.add("Riisit");
				break;
			case Category.OILS_AND_SPICES:
				this.setExcelTitle("oljyt_mausteet_ja_maustaminen");
				this.setSideBarTitle("Öljyt, mausteet, maustaminen");
				this.subMenuTitles.add("Öljyt");
				this.subMenuTitles.add("Ketsupit ja sinapit");
				this.subMenuTitles.add("Salaatinkastikkeet");
				this.subMenuTitles.add("Majoneesit ja majoneesikastikkeet");
				break;
			case Category.DRINKS:
				this.setExcelTitle("juomat");
				this.setSideBarTitle("Juomat");
				this.subMenuTitles.add("Mehut");
				break;
			case Category.SNACKS:
				this.setExcelTitle("snacksit");
				this.setSideBarTitle("Snacksit");
				this.subMenuTitles.add("Sipsit");
				this.subMenuTitles.add("Suolatikut ja muut leivotut snacksit");
				this.subMenuTitles.add("Kuivalihat ja muut snacksit");
				this.subMenuTitles.add("Pähkinät");
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
	
	public ArrayList<String> getSubMenuTitles() {
		return this.subMenuTitles;
	}
}
