
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.gs1us.sgg.gbservice.api.AttributeSet"%>
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	Map<String,String> uomTable = new HashMap<String,String>();
	uomTable.put("X_1N", "Count");
	uomTable.put("FTQ", "ft\u00b3");
	uomTable.put("MMQ", "mm\u00b3");
	uomTable.put("G21", "c");
	uomTable.put("E27", "dose");
	uomTable.put("DZN", "doz");
	uomTable.put("PTD", "pint");
	uomTable.put("EA",  "ea");
	uomTable.put("OZA", "fl\u00a0oz");
	uomTable.put("FOT", "ft");
	uomTable.put("GLL", "gal");
	uomTable.put("GRM", "g");
	uomTable.put("INH", "in");
	uomTable.put("KGM", "kg");
	uomTable.put("PTL", "pint");
	uomTable.put("QTL", "quart");
	uomTable.put("LTR", "l");
	uomTable.put("MTR", "m");
	uomTable.put("MGM", "mg");
	uomTable.put("MLT", "ml");
	uomTable.put("MMT", "mm");
	uomTable.put("NIU", "UI");
	uomTable.put("ONZ", "oz");
	uomTable.put("H87", "piece");
	uomTable.put("LBR", "lb");
	uomTable.put("QTD", "quart");
	uomTable.put("FTK", "ft\u00b2");
	uomTable.put("INK", "in\u00b2");
	uomTable.put("MTK", "m\u00b2");
	uomTable.put("MMK", "mm\u00b2");
	uomTable.put("YDK", "yard\u00b2");
	uomTable.put("E55", "use");
	uomTable.put("YRD", "yard");

	Product product = (Product)request.getAttribute("product");
	AttributeSet attributes = product.getAttributes();
	String brandOwnerName = attributes.getAttribute("brandOwnerName");
	String brandName = attributes.getAttribute("brandName");
	String subBrand = attributes.getAttribute("subBrand");
	String functionalName = attributes.getAttribute("functionalName");
	String tradeItemDescription = attributes.getAttribute("tradeItemDescription");
	String additionalTradeItemDescription = attributes.getAttribute("additionalTradeItemDescription");
	String netContent1 = attributes.getAttribute("netContent1");
	String netContent1Uom = uomTable.get(attributes.getAttribute("netContent1_uom"));
	String netContent2 = attributes.getAttribute("netContent2");
	String netContent2Uom = uomTable.get(attributes.getAttribute("netContent2_uom"));
	String netContent3 = attributes.getAttribute("netContent3");
	String netContent3Uom = uomTable.get(attributes.getAttribute("netContent3_uom"));
	String netContent4 = attributes.getAttribute("netContent4");
	String netContent4Uom = uomTable.get(attributes.getAttribute("netContent4_uom"));
	String descriptiveSize = attributes.getAttribute("descriptiveSize");
	String gpcCategoryCode = attributes.getAttribute("gpcCategoryCode");
	String informationProviderGLN = attributes.getAttribute("informationProviderGLN");
	String itemDataLanguage = attributes.getAttribute("itemDataLanguage");
	String mobileDeviceImage = attributes.getAttribute("uriProductImage");
	String trustmarkUrl = request.getContextPath() + "/images/trustmark_82x20.png";
	String imageUrl = mobileDeviceImage == null ? request.getContextPath() + "/images/placeholder_image.png" : mobileDeviceImage;
	String websiteUrl = attributes.getAttribute("uriProductWebsite");
	
	
	HashMap<String, String>  countryCodes = new HashMap<String, String>();
	countryCodes.put("1","AFGHANISTAN");
	countryCodes.put("2","ALBANIA");
	countryCodes.put("3","ALGERIA");
	countryCodes.put("4","AMERICAN SAMOA");
	countryCodes.put("5","ANDORRA");
	countryCodes.put("6","ANGOLA");
	countryCodes.put("7","ANGUILLA");
	countryCodes.put("8","ANTARCTICA");
	countryCodes.put("9","ANTIGUA AND BARBUDA");
	countryCodes.put("10","ARGENTINA");
	countryCodes.put("11","ARMENIA");
	countryCodes.put("12","ARUBA");
	countryCodes.put("13","AUSTRALIA");
	countryCodes.put("14","AUSTRIA");
	countryCodes.put("15","AZERBAIJAN");
	countryCodes.put("16","BAHAMAS");
	countryCodes.put("17","BAHRAIN");
	countryCodes.put("18","BANGLADESH");
	countryCodes.put("19","BARBADOS");
	countryCodes.put("20","BELARUS");
	countryCodes.put("21","BELGIUM");
	countryCodes.put("22","BELIZE");
	countryCodes.put("23","BENIN");
	countryCodes.put("24","BERMUDA");
	countryCodes.put("25","BHUTAN");
	countryCodes.put("26","BOLIVIA");
	countryCodes.put("27","BOSNIA AND HERZEGOWINA");
	countryCodes.put("28","BOTSWANA");
	countryCodes.put("29","BOUVET ISLAND");
	countryCodes.put("30","BRAZIL");
	countryCodes.put("31","BRITISH INDIAN OCEAN TERRITORY");
	countryCodes.put("32","BRUNEI DARUSSALAM");
	countryCodes.put("33","BULGARIA");
	countryCodes.put("34","BURKINA FASO");
	countryCodes.put("35","BURUNDI");
	countryCodes.put("36","CAMBODIA");
	countryCodes.put("37","CAMEROON");
	countryCodes.put("38","CANADA");
	countryCodes.put("39","CAPE VERDE");
	countryCodes.put("40","CAYMAN ISLANDS");
	countryCodes.put("41","CENTRAL AFRICAN REPUBLIC");
	countryCodes.put("42","CHAD");
	countryCodes.put("43","CHILE");
	countryCodes.put("44","CHINA");
	countryCodes.put("45","CHRISTMAS ISLAND");
	countryCodes.put("46","COCOS (KEELING) ISLANDS");
	countryCodes.put("47","COLOMBIA");
	countryCodes.put("48","COMOROS");
	countryCodes.put("49","CONGO");
	countryCodes.put("50","COOK ISLANDS");
	countryCodes.put("51","COSTA RICA");
	countryCodes.put("52","COTE D'IVOIRE");
	countryCodes.put("53","CROATIA (local name: Hrvatska)");
	countryCodes.put("54","CUBA");
	countryCodes.put("55","CYPRUS");
	countryCodes.put("56","CZECH REPUBLIC");
	countryCodes.put("57","DENMARK");
	countryCodes.put("58","DJIBOUTI");
	countryCodes.put("59","DOMINICA");
	countryCodes.put("60","DOMINICAN REPUBLIC");
	countryCodes.put("61","EAST TIMOR");
	countryCodes.put("62","ECUADOR");
	countryCodes.put("63","EGYPT");
	countryCodes.put("64","EL SALVADOR");
	countryCodes.put("65","EQUATORIAL GUINEA");
	countryCodes.put("66","ERITREA");
	countryCodes.put("67","ESTONIA");
	countryCodes.put("68","ETHIOPIA");
	countryCodes.put("69","FALKLAND ISLANDS (MALVINAS)");
	countryCodes.put("70","FAROE ISLANDS");
	countryCodes.put("71","FIJI");
	countryCodes.put("72","FINLAND");
	countryCodes.put("73","FRANCE");
	countryCodes.put("74","FRANCE, METROPOLITAN");
	countryCodes.put("75","FRENCH GUIANA");
	countryCodes.put("76","FRENCH POLYNESIA");
	countryCodes.put("77","FRENCH SOUTHERN TERRITORIES");
	countryCodes.put("78","GABON");
	countryCodes.put("79","GAMBIA");
	countryCodes.put("80","GEORGIA");
	countryCodes.put("81","GERMANY");
	countryCodes.put("82","GHANA");
	countryCodes.put("83","GIBRALTAR");
	countryCodes.put("84","GREECE");
	countryCodes.put("85","GREENLAND");
	countryCodes.put("86","GRENADA");
	countryCodes.put("87","GUADELOUPE");
	countryCodes.put("88","GUAM");
	countryCodes.put("89","GUATEMALA");
	countryCodes.put("90","GUINEA");
	countryCodes.put("91","GUINEA-BISSAU");
	countryCodes.put("92","GUYANA");
	countryCodes.put("93","HAITI");
	countryCodes.put("94","HEARD AND MC DONALD ISLANDS");
	countryCodes.put("95","HONDURAS");
	countryCodes.put("96","HONG KONG");
	countryCodes.put("97","HUNGARY");
	countryCodes.put("98","ICELAND");
	countryCodes.put("99","INDIA");
	countryCodes.put("100","INDONESIA");
	countryCodes.put("101","IRAN (ISLAMIC REPUBLIC OF)");
	countryCodes.put("102","IRAQ");
	countryCodes.put("103","IRELAND");
	countryCodes.put("104","ISRAEL");
	countryCodes.put("105","ITALY");
	countryCodes.put("106","JAMAICA");
	countryCodes.put("107","JAPAN");
	countryCodes.put("108","JORDAN");
	countryCodes.put("109","KAZAKHSTAN");
	countryCodes.put("110","KENYA");
	countryCodes.put("111","KIRIBATI");
	countryCodes.put("112","KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF");
	countryCodes.put("113","KOREA, REPUBLIC OF");
	countryCodes.put("114","KUWAIT");
	countryCodes.put("115","KYRGYZSTAN");
	countryCodes.put("116","LAO PEOPLE'S DEMOCRATIC REPUBLIC");
	countryCodes.put("117","LATVIA");
	countryCodes.put("118","LEBANON");
	countryCodes.put("119","LESOTHO");
	countryCodes.put("120","LIBERIA");
	countryCodes.put("121","LIBYAN ARAB JAMAHIRIYA");
	countryCodes.put("122","LIECHTENSTEIN");
	countryCodes.put("123","LITHUANIA");
	countryCodes.put("124","LUXEMBOURG");
	countryCodes.put("125","MACAU");
	countryCodes.put("126","MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF");
	countryCodes.put("127","MADAGASCAR");
	countryCodes.put("128","MALAWI");
	countryCodes.put("129","MALAYSIA");
	countryCodes.put("130","MALDIVES");
	countryCodes.put("131","MALI");
	countryCodes.put("132","MALTA");
	countryCodes.put("133","MARSHALL ISLANDS");
	countryCodes.put("134","MARTINIQUE");
	countryCodes.put("135","MAURITANIA");
	countryCodes.put("136","MAURITIUS");
	countryCodes.put("137","MAYOTTE");
	countryCodes.put("138","MEXICO");
	countryCodes.put("139","MICRONESIA, FEDERATED STATES OF");
	countryCodes.put("140","MOLDOVA, REPUBLIC OF");
	countryCodes.put("141","MONACO");
	countryCodes.put("142","MONGOLIA");
	countryCodes.put("143","MONTSERRAT");
	countryCodes.put("144","MOROCCO");
	countryCodes.put("145","MOZAMBIQUE");
	countryCodes.put("146","MYANMAR");
	countryCodes.put("147","NAMIBIA");
	countryCodes.put("148","NAURU");
	countryCodes.put("149","NEPAL");
	countryCodes.put("150","NETHERLANDS");
	countryCodes.put("151","NETHERLANDS ANTILLES");
	countryCodes.put("152","NEW CALEDONIA");
	countryCodes.put("153","NEW ZEALAND");
	countryCodes.put("154","NICARAGUA");
	countryCodes.put("155","NIGER");
	countryCodes.put("156","NIGERIA");
	countryCodes.put("157","NIUE");
	countryCodes.put("158","NORFOLK ISLAND");
	countryCodes.put("159","NORTHERN MARIANA ISLANDS");
	countryCodes.put("160","NORWAY");
	countryCodes.put("161","OMAN");
	countryCodes.put("162","PAKISTAN");
	countryCodes.put("163","PALAU");
	countryCodes.put("164","PANAMA");
	countryCodes.put("165","PAPUA NEW GUINEA");
	countryCodes.put("166","PARAGUAY");
	countryCodes.put("167","PERU");
	countryCodes.put("168","PHILIPPINES");
	countryCodes.put("169","PITCAIRN");
	countryCodes.put("170","POLAND");
	countryCodes.put("171","PORTUGAL");
	countryCodes.put("172","PUERTO RICO");
	countryCodes.put("173","QATAR");
	countryCodes.put("174","REUNION");
	countryCodes.put("175","ROMANIA");
	countryCodes.put("176","RUSSIAN FEDERATION");
	countryCodes.put("177","RWANDA");
	countryCodes.put("178","SAINT KITTS AND NEVIS");
	countryCodes.put("179","SAINT LUCIA");
	countryCodes.put("180","SAINT VINCENT AND THE GRENADINES");
	countryCodes.put("181","SAMOA");
	countryCodes.put("182","SAN MARINO");
	countryCodes.put("183","SAO TOME AND PRINCIPE");
	countryCodes.put("184","SAUDI ARABIA");
	countryCodes.put("185","SENEGAL");
	countryCodes.put("186","SEYCHELLES");
	countryCodes.put("187","SIERRA LEONE");
	countryCodes.put("188","SINGAPORE");
	countryCodes.put("189","SLOVAKIA (Slovak Republic)");
	countryCodes.put("190","SLOVENIA");
	countryCodes.put("191","SOLOMON ISLANDS");
	countryCodes.put("192","SOMALIA");
	countryCodes.put("193","SOUTH AFRICA");
	countryCodes.put("194","SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS");
	countryCodes.put("195","SPAIN");
	countryCodes.put("196","SRI LANKA");
	countryCodes.put("197","ST. HELENA");
	countryCodes.put("198","ST. PIERRE AND MIQUELON");
	countryCodes.put("199","SUDAN");
	countryCodes.put("200","SURINAME");
	countryCodes.put("201","SVALBARD AND JAN MAYEN ISLANDS");
	countryCodes.put("202","SWAZILAND");
	countryCodes.put("203","SWEDEN");
	countryCodes.put("204","SWITZERLAND");
	countryCodes.put("205","SYRIAN ARAB REPUBLIC");
	countryCodes.put("206","TAIWAN");
	countryCodes.put("207","TAJIKISTAN");
	countryCodes.put("208","TANZANIA, UNITED REPUBLIC OF");
	countryCodes.put("209","THAILAND");
	countryCodes.put("210","TOGO");
	countryCodes.put("211","TOKELAU");
	countryCodes.put("212","TONGA");
	countryCodes.put("213","TRINIDAD AND TOBAGO");
	countryCodes.put("214","TUNISIA");
	countryCodes.put("215","TURKEY");
	countryCodes.put("216","TURKMENISTAN");
	countryCodes.put("217","TURKS AND CAICOS ISLANDS");
	countryCodes.put("218","TUVALU");
	countryCodes.put("219","UGANDA");
	countryCodes.put("220","UKRAINE");
	countryCodes.put("221","UNITED ARAB EMIRATES");
	countryCodes.put("222","UNITED KINGDOM");
	countryCodes.put("223","UNITED STATES");
	countryCodes.put("224","UNITED STATES MINOR OUTLYING ISLANDS");
	countryCodes.put("225","URUGUAY");
	countryCodes.put("226","UZBEKISTAN");
	countryCodes.put("227","VANUATU");
	countryCodes.put("228","VATICAN CITY STATE (HOLY SEE)");
	countryCodes.put("229","VENEZUELA");
	countryCodes.put("230","VIET NAM");
	countryCodes.put("231","VIRGIN ISLANDS (BRITISH)");
	countryCodes.put("232","VIRGIN ISLANDS (U.S.)");
	countryCodes.put("233","WALLIS AND FUTUNA ISLANDS");
	countryCodes.put("234","WESTERN SAHARA");
	countryCodes.put("235","YEMEN");
	countryCodes.put("236","YUGOSLAVIA");
	countryCodes.put("237","ZAIRE");
	countryCodes.put("238","ZAMBIA");
	countryCodes.put("239","ZIMBABWE");	
	
%>
    
<jsp:include page="/jspinfo/cp/header.jsp" flush="true">
  <jsp:param name="pageTitle" value="Product" />
</jsp:include>

<section>
	<div id="product-mobileDeviceImage"><img src="<%= imageUrl %>" /></div>
    <div id="product-trustmark" class="pull-right"><img src="<%= trustmarkUrl %>" /></div>

    <h4>GTIN</h4>
    <p><%=product.getGtin() %></p>

    <h4>Target Market</h4>
    <p><%= countryCodes.get(product.getAttributes().getAttribute("targetMarket"))%></p>
    
    <h4>Brand</h4>
    <p><%= product.getAttributes().getAttribute("brandName") %></p>

    <h4>Label Description</h4>
    <p><%= product.getAttributes().getAttribute("additionalTradeItemDescription") %></p>
    
    <h4>Company Name</h4>
    <p><%= product.getAttributes().getAttribute("companyName") %></p>

    <h4>Product Classification</h4>
    <p><%= product.getAttributes().getAttribute("gpcCategoryCode") %></p>
        
</section>


<jsp:include page="/jspinfo/cp/footer.jsp" flush="true" />
