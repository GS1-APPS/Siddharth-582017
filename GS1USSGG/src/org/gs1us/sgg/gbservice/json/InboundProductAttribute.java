package org.gs1us.sgg.gbservice.json;

public class InboundProductAttribute {
	//{"itemDataLanguage":"xx","brandName":"NESTLE","additionalTradeItemDescription":"NESTLÉ KITKAT Chunky Peanut Butter Single 42 g","targetMarket":"14","gpcCategoryCode":"10000045","brandOwnerName":"GS1","companyName":"Nestlé Österreich GmbH, Am Europlatz 2, 1120 Wien, Austria","uriProductImage":"https://nestle.thirdlight.com/gtin/03800020403631.jpg","informationProviderGLN":"9300605000001"}
	
	private String gtin;
	private String targetMarket;
	private String brandName;
	private String itemDataLanguage;
	private String additionalTradeItemDescription;
	private String gpcCategoryCode;
	private String brandOwnerName; 
	private String companyName;
	private String informationProviderGLN;
	private String uriProductImage;
	
	
	public String getGtin() {
		return gtin;
	}
	public void setGtin(String gtin) {
		this.gtin = gtin;
	}
	public String getTargetMarket() {
		return targetMarket;
	}
	public void setTargetMarket(String targetMarket) {
		this.targetMarket = targetMarket;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getItemDataLanguage() {
		return itemDataLanguage;
	}
	public void setItemDataLanguage(String itemDataLanguage) {
		this.itemDataLanguage = itemDataLanguage;
	}
	public String getAdditionalTradeItemDescription() {
		return additionalTradeItemDescription;
	}
	public void setAdditionalTradeItemDescription(String additionalTradeItemDescription) {
		this.additionalTradeItemDescription = additionalTradeItemDescription;
	}
	public String getGpcCategoryCode() {
		return gpcCategoryCode;
	}
	public void setGpcCategoryCode(String gpcCategoryCode) {
		this.gpcCategoryCode = gpcCategoryCode;
	}
	public String getBrandOwnerName() {
		return brandOwnerName;
	}
	public void setBrandOwnerName(String brandOwnerName) {
		this.brandOwnerName = brandOwnerName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getInformationProviderGLN() {
		return informationProviderGLN;
	}
	public void setInformationProviderGLN(String informationProviderGLN) {
		this.informationProviderGLN = informationProviderGLN;
	}
	public String getUriProductImage() {
		return uriProductImage;
	}
	public void setUriProductImage(String uriProductImage) {
		this.uriProductImage = uriProductImage;
	}
	
	
		
	
}
