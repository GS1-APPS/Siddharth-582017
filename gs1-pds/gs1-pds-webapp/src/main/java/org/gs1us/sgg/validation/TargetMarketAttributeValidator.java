package org.gs1us.sgg.validation;

import java.util.HashMap;
import java.util.List;

import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class TargetMarketAttributeValidator implements AttributeValidator
{

    @Override
    public boolean validate(AttributeDesc attrDesc, HasAttributes objectToValidate, List<ProductValidationError> validationErrors)
    {
        AttributeSet attributes = objectToValidate.getAttributes();
        int errorCount = validationErrors.size();        
        String value = attributes.getAttribute(attrDesc);
        
        if (value != null && !value.equals(""))
        {
        	boolean byPlanId = GetIdBasedOnPlanId(value);
        	boolean byCountry = GetIdBasedOnCountryName(value);
        	boolean byCountryCode2 = GetIdBasedOnCountryCode2(value);
        	boolean byCountryCode3 = GetIdBasedOnCountryCode3(value);
        	
        	if (!byPlanId && !byCountry && !byCountryCode2 && !byCountryCode3)
        	{
        		validationErrors.add(new ProductValidationErrorImpl("Target Market", "Target Market is not a valid value."));
        	}
        }
        else
        {
        	validationErrors.add(new ProductValidationErrorImpl("Target Market", "Target Market is a required field."));
        }
        
        return validationErrors.size() == errorCount;
    }
    
    private boolean GetIdBasedOnPlanId(String value)
    {
    	HashMap<String, String>  countryCodes = new HashMap<String, String>();
    	countryCodes.put("1", "1");
    	countryCodes.put("2", "2");
    	countryCodes.put("3", "3");
    	countryCodes.put("4", "4");
    	countryCodes.put("5", "5");
    	countryCodes.put("6", "6");
    	countryCodes.put("7", "7");
    	countryCodes.put("8", "8");
    	countryCodes.put("9", "9");
    	countryCodes.put("10", "10");
    	countryCodes.put("11", "11");
    	countryCodes.put("12", "12");
    	countryCodes.put("13", "13");
    	countryCodes.put("14", "14");
    	countryCodes.put("15", "15");
    	countryCodes.put("16", "16");
    	countryCodes.put("17", "17");
    	countryCodes.put("18", "18");
    	countryCodes.put("19", "19");
    	countryCodes.put("20", "20");
    	countryCodes.put("21", "21");
    	countryCodes.put("22", "22");
    	countryCodes.put("23", "23");
    	countryCodes.put("24", "24");
    	countryCodes.put("25", "25");
    	countryCodes.put("26", "26");
    	countryCodes.put("27", "27");
    	countryCodes.put("28", "28");
    	countryCodes.put("29", "29");
    	countryCodes.put("30", "30");
    	countryCodes.put("31", "31");
    	countryCodes.put("32", "32");
    	countryCodes.put("33", "33");
    	countryCodes.put("34", "34");
    	countryCodes.put("35", "35");
    	countryCodes.put("36", "36");
    	countryCodes.put("37", "37");
    	countryCodes.put("38", "38");
    	countryCodes.put("39", "39");
    	countryCodes.put("40", "40");
    	countryCodes.put("41", "41");
    	countryCodes.put("42", "42");
    	countryCodes.put("43", "43");
    	countryCodes.put("44", "44");
    	countryCodes.put("45", "45");
    	countryCodes.put("46", "46");
    	countryCodes.put("47", "47");
    	countryCodes.put("48", "48");
    	countryCodes.put("49", "49");
    	countryCodes.put("50", "50");
    	countryCodes.put("51", "51");
    	countryCodes.put("52", "52");
    	countryCodes.put("53", "53");
    	countryCodes.put("54", "54");
    	countryCodes.put("55", "55");
    	countryCodes.put("56", "56");
    	countryCodes.put("57", "57");
    	countryCodes.put("58", "58");
    	countryCodes.put("59", "59");
    	countryCodes.put("60", "60");
    	countryCodes.put("61", "61");
    	countryCodes.put("62", "62");
    	countryCodes.put("63", "63");
    	countryCodes.put("64", "64");
    	countryCodes.put("65", "65");
    	countryCodes.put("66", "66");
    	countryCodes.put("67", "67");
    	countryCodes.put("68", "68");
    	countryCodes.put("69", "69");
    	countryCodes.put("70", "70");
    	countryCodes.put("71", "71");
    	countryCodes.put("72", "72");
    	countryCodes.put("73", "73");
    	countryCodes.put("74", "74");
    	countryCodes.put("75", "75");
    	countryCodes.put("76", "76");
    	countryCodes.put("77", "77");
    	countryCodes.put("78", "78");
    	countryCodes.put("79", "79");
    	countryCodes.put("80", "80");
    	countryCodes.put("81", "81");
    	countryCodes.put("82", "82");
    	countryCodes.put("83", "83");
    	countryCodes.put("84", "84");
    	countryCodes.put("85", "85");
    	countryCodes.put("86", "86");
    	countryCodes.put("87", "87");
    	countryCodes.put("88", "88");
    	countryCodes.put("89", "89");
    	countryCodes.put("90", "90");
    	countryCodes.put("91", "91");
    	countryCodes.put("92", "92");
    	countryCodes.put("93", "93");
    	countryCodes.put("94", "94");
    	countryCodes.put("95", "95");
    	countryCodes.put("96", "96");
    	countryCodes.put("97", "97");
    	countryCodes.put("98", "98");
    	countryCodes.put("99", "99");
    	countryCodes.put("100", "100");
    	countryCodes.put("101", "101");
    	countryCodes.put("102", "102");
    	countryCodes.put("103", "103");
    	countryCodes.put("104", "104");
    	countryCodes.put("105", "105");
    	countryCodes.put("106", "106");
    	countryCodes.put("107", "107");
    	countryCodes.put("108", "108");
    	countryCodes.put("109", "109");
    	countryCodes.put("110", "110");
    	countryCodes.put("111", "111");
    	countryCodes.put("112", "112");
    	countryCodes.put("113", "113");
    	countryCodes.put("114", "114");
    	countryCodes.put("115", "115");
    	countryCodes.put("116", "116");
    	countryCodes.put("117", "117");
    	countryCodes.put("118", "118");
    	countryCodes.put("119", "119");
    	countryCodes.put("120", "120");
    	countryCodes.put("121", "121");
    	countryCodes.put("122", "122");
    	countryCodes.put("123", "123");
    	countryCodes.put("124", "124");
    	countryCodes.put("125", "125");
    	countryCodes.put("126", "126");
    	countryCodes.put("127", "127");
    	countryCodes.put("128", "128");
    	countryCodes.put("129", "129");
    	countryCodes.put("130", "130");
    	countryCodes.put("131", "131");
    	countryCodes.put("132", "132");
    	countryCodes.put("133", "133");
    	countryCodes.put("134", "134");
    	countryCodes.put("135", "135");
    	countryCodes.put("136", "136");
    	countryCodes.put("137", "137");
    	countryCodes.put("138", "138");
    	countryCodes.put("139", "139");
    	countryCodes.put("140", "140");
    	countryCodes.put("141", "141");
    	countryCodes.put("142", "142");
    	countryCodes.put("143", "143");
    	countryCodes.put("144", "144");
    	countryCodes.put("145", "145");
    	countryCodes.put("146", "146");
    	countryCodes.put("147", "147");
    	countryCodes.put("148", "148");
    	countryCodes.put("149", "149");
    	countryCodes.put("150", "150");
    	countryCodes.put("151", "151");
    	countryCodes.put("152", "152");
    	countryCodes.put("153", "153");
    	countryCodes.put("154", "154");
    	countryCodes.put("155", "155");
    	countryCodes.put("156", "156");
    	countryCodes.put("157", "157");
    	countryCodes.put("158", "158");
    	countryCodes.put("159", "159");
    	countryCodes.put("160", "160");
    	countryCodes.put("161", "161");
    	countryCodes.put("162", "162");
    	countryCodes.put("163", "163");
    	countryCodes.put("164", "164");
    	countryCodes.put("165", "165");
    	countryCodes.put("166", "166");
    	countryCodes.put("167", "167");
    	countryCodes.put("168", "168");
    	countryCodes.put("169", "169");
    	countryCodes.put("170", "170");
    	countryCodes.put("171", "171");
    	countryCodes.put("172", "172");
    	countryCodes.put("173", "173");
    	countryCodes.put("174", "174");
    	countryCodes.put("175", "175");
    	countryCodes.put("176", "176");
    	countryCodes.put("177", "177");
    	countryCodes.put("178", "178");
    	countryCodes.put("179", "179");
    	countryCodes.put("180", "180");
    	countryCodes.put("181", "181");
    	countryCodes.put("182", "182");
    	countryCodes.put("183", "183");
    	countryCodes.put("184", "184");
    	countryCodes.put("185", "185");
    	countryCodes.put("186", "186");
    	countryCodes.put("187", "187");
    	countryCodes.put("188", "188");
    	countryCodes.put("189", "189");
    	countryCodes.put("190", "190");
    	countryCodes.put("191", "191");
    	countryCodes.put("192", "192");
    	countryCodes.put("193", "193");
    	countryCodes.put("194", "194");
    	countryCodes.put("195", "195");
    	countryCodes.put("196", "196");
    	countryCodes.put("197", "197");
    	countryCodes.put("198", "198");
    	countryCodes.put("199", "199");
    	countryCodes.put("200", "200");
    	countryCodes.put("201", "201");
    	countryCodes.put("202", "202");
    	countryCodes.put("203", "203");
    	countryCodes.put("204", "204");
    	countryCodes.put("205", "205");
    	countryCodes.put("206", "206");
    	countryCodes.put("207", "207");
    	countryCodes.put("208", "208");
    	countryCodes.put("209", "209");
    	countryCodes.put("210", "210");
    	countryCodes.put("211", "211");
    	countryCodes.put("212", "212");
    	countryCodes.put("213", "213");
    	countryCodes.put("214", "214");
    	countryCodes.put("215", "215");
    	countryCodes.put("216", "216");
    	countryCodes.put("217", "217");
    	countryCodes.put("218", "218");
    	countryCodes.put("219", "219");
    	countryCodes.put("220", "220");
    	countryCodes.put("221", "221");
    	countryCodes.put("222", "222");
    	countryCodes.put("223", "223");
    	countryCodes.put("224", "224");
    	countryCodes.put("225", "225");
    	countryCodes.put("226", "226");
    	countryCodes.put("227", "227");
    	countryCodes.put("228", "228");
    	countryCodes.put("229", "229");
    	countryCodes.put("230", "230");
    	countryCodes.put("231", "231");
    	countryCodes.put("232", "232");
    	countryCodes.put("233", "233");
    	countryCodes.put("234", "234");
    	countryCodes.put("235", "235");
    	countryCodes.put("236", "236");
    	countryCodes.put("237", "237");
    	countryCodes.put("238", "238");
    	countryCodes.put("239", "239");
    	
    	if (countryCodes.containsValue(value))
    	{
    		return true;
    	}
    	
        return false;    	
    }
    
    private boolean GetIdBasedOnCountryCode2(String countryCode2)
    {
    	countryCode2 = countryCode2.toUpperCase();    	
    	HashMap<String, String>  countryCodes = new HashMap<String, String>();
    	countryCodes.put("1","AF");
    	countryCodes.put("2","AL");
    	countryCodes.put("3","DZ");
    	countryCodes.put("4","AS");
    	countryCodes.put("5","AD");
    	countryCodes.put("6","AO");
    	countryCodes.put("7","AI");
    	countryCodes.put("8","AQ");
    	countryCodes.put("9","AG");
    	countryCodes.put("10","AR");
    	countryCodes.put("11","AM");
    	countryCodes.put("12","AW");
    	countryCodes.put("13","AU");
    	countryCodes.put("14","AT");
    	countryCodes.put("15","AZ");
    	countryCodes.put("16","BS");
    	countryCodes.put("17","BH");
    	countryCodes.put("18","BD");
    	countryCodes.put("19","BB");
    	countryCodes.put("20","BY");
    	countryCodes.put("21","BE");
    	countryCodes.put("22","BZ");
    	countryCodes.put("23","BJ");
    	countryCodes.put("24","BM");
    	countryCodes.put("25","BT");
    	countryCodes.put("26","BO");
    	countryCodes.put("27","BA");
    	countryCodes.put("28","BW");
    	countryCodes.put("29","BV");
    	countryCodes.put("30","BR");
    	countryCodes.put("31","IO");
    	countryCodes.put("32","BN");
    	countryCodes.put("33","BG");
    	countryCodes.put("34","BF");
    	countryCodes.put("35","BI");
    	countryCodes.put("36","KH");
    	countryCodes.put("37","CM");
    	countryCodes.put("38","CA");
    	countryCodes.put("39","CV");
    	countryCodes.put("40","KY");
    	countryCodes.put("41","CF");
    	countryCodes.put("42","TD");
    	countryCodes.put("43","CL");
    	countryCodes.put("44","CN");
    	countryCodes.put("45","CX");
    	countryCodes.put("46","CC");
    	countryCodes.put("47","CO");
    	countryCodes.put("48","KM");
    	countryCodes.put("49","CG");
    	countryCodes.put("50","CK");
    	countryCodes.put("51","CR");
    	countryCodes.put("52","CI");
    	countryCodes.put("53","HR");
    	countryCodes.put("54","CU");
    	countryCodes.put("55","CY");
    	countryCodes.put("56","CZ");
    	countryCodes.put("57","DK");
    	countryCodes.put("58","DJ");
    	countryCodes.put("59","DM");
    	countryCodes.put("60","DO");
    	countryCodes.put("61","TP");
    	countryCodes.put("62","EC");
    	countryCodes.put("63","EG");
    	countryCodes.put("64","SV");
    	countryCodes.put("65","GQ");
    	countryCodes.put("66","ER");
    	countryCodes.put("67","EE");
    	countryCodes.put("68","ET");
    	countryCodes.put("69","FK");
    	countryCodes.put("70","FO");
    	countryCodes.put("71","FJ");
    	countryCodes.put("72","FI");
    	countryCodes.put("73","FR");
    	countryCodes.put("74","FX");
    	countryCodes.put("75","GF");
    	countryCodes.put("76","PF");
    	countryCodes.put("77","TF");
    	countryCodes.put("78","GA");
    	countryCodes.put("79","GM");
    	countryCodes.put("80","GE");
    	countryCodes.put("81","DE");
    	countryCodes.put("82","GH");
    	countryCodes.put("83","GI");
    	countryCodes.put("84","GR");
    	countryCodes.put("85","GL");
    	countryCodes.put("86","GD");
    	countryCodes.put("87","GP");
    	countryCodes.put("88","GU");
    	countryCodes.put("89","GT");
    	countryCodes.put("90","GN");
    	countryCodes.put("91","GW");
    	countryCodes.put("92","GY");
    	countryCodes.put("93","HT");
    	countryCodes.put("94","HM");
    	countryCodes.put("95","HN");
    	countryCodes.put("96","HK");
    	countryCodes.put("97","HU");
    	countryCodes.put("98","IS");
    	countryCodes.put("99","IN");
    	countryCodes.put("100","ID");
    	countryCodes.put("101","IR");
    	countryCodes.put("102","IQ");
    	countryCodes.put("103","IE");
    	countryCodes.put("104","IL");
    	countryCodes.put("105","IT");
    	countryCodes.put("106","JM");
    	countryCodes.put("107","JP");
    	countryCodes.put("108","JO");
    	countryCodes.put("109","KZ");
    	countryCodes.put("110","KE");
    	countryCodes.put("111","KI");
    	countryCodes.put("112","KP");
    	countryCodes.put("113","KR");
    	countryCodes.put("114","KW");
    	countryCodes.put("115","KG");
    	countryCodes.put("116","LA");
    	countryCodes.put("117","LV");
    	countryCodes.put("118","LB");
    	countryCodes.put("119","LS");
    	countryCodes.put("120","LR");
    	countryCodes.put("121","LY");
    	countryCodes.put("122","LI");
    	countryCodes.put("123","LT");
    	countryCodes.put("124","LU");
    	countryCodes.put("125","MO");
    	countryCodes.put("126","MK");
    	countryCodes.put("127","MG");
    	countryCodes.put("128","MW");
    	countryCodes.put("129","MY");
    	countryCodes.put("130","MV");
    	countryCodes.put("131","ML");
    	countryCodes.put("132","MT");
    	countryCodes.put("133","MH");
    	countryCodes.put("134","MQ");
    	countryCodes.put("135","MR");
    	countryCodes.put("136","MU");
    	countryCodes.put("137","YT");
    	countryCodes.put("138","MX");
    	countryCodes.put("139","FM");
    	countryCodes.put("140","MD");
    	countryCodes.put("141","MC");
    	countryCodes.put("142","MN");
    	countryCodes.put("143","MS");
    	countryCodes.put("144","MA");
    	countryCodes.put("145","MZ");
    	countryCodes.put("146","MM");
    	countryCodes.put("147","NA");
    	countryCodes.put("148","NR");
    	countryCodes.put("149","NP");
    	countryCodes.put("150","NL");
    	countryCodes.put("151","AN");
    	countryCodes.put("152","NC");
    	countryCodes.put("153","NZ");
    	countryCodes.put("154","NI");
    	countryCodes.put("155","NE");
    	countryCodes.put("156","NG");
    	countryCodes.put("157","NU");
    	countryCodes.put("158","NF");
    	countryCodes.put("159","MP");
    	countryCodes.put("160","NO");
    	countryCodes.put("161","OM");
    	countryCodes.put("162","PK");
    	countryCodes.put("163","PW");
    	countryCodes.put("164","PA");
    	countryCodes.put("165","PG");
    	countryCodes.put("166","PY");
    	countryCodes.put("167","PE");
    	countryCodes.put("168","PH");
    	countryCodes.put("169","PN");
    	countryCodes.put("170","PL");
    	countryCodes.put("171","PT");
    	countryCodes.put("172","PR");
    	countryCodes.put("173","QA");
    	countryCodes.put("174","RE");
    	countryCodes.put("175","RO");
    	countryCodes.put("176","RU");
    	countryCodes.put("177","RW");
    	countryCodes.put("178","KN");
    	countryCodes.put("179","LC");
    	countryCodes.put("180","VC");
    	countryCodes.put("181","WS");
    	countryCodes.put("182","SM");
    	countryCodes.put("183","ST");
    	countryCodes.put("184","SA");
    	countryCodes.put("185","SN");
    	countryCodes.put("186","SC");
    	countryCodes.put("187","SL");
    	countryCodes.put("188","SG");
    	countryCodes.put("189","SK");
    	countryCodes.put("190","SI");
    	countryCodes.put("191","SB");
    	countryCodes.put("192","SO");
    	countryCodes.put("193","ZA");
    	countryCodes.put("194","GS");
    	countryCodes.put("195","ES");
    	countryCodes.put("196","LK");
    	countryCodes.put("197","SH");
    	countryCodes.put("198","PM");
    	countryCodes.put("199","SD");
    	countryCodes.put("200","SR");
    	countryCodes.put("201","SJ");
    	countryCodes.put("202","SZ");
    	countryCodes.put("203","SE");
    	countryCodes.put("204","CH");
    	countryCodes.put("205","SY");
    	countryCodes.put("206","TW");
    	countryCodes.put("207","TJ");
    	countryCodes.put("208","TZ");
    	countryCodes.put("209","TH");
    	countryCodes.put("210","TG");
    	countryCodes.put("211","TK");
    	countryCodes.put("212","TO");
    	countryCodes.put("213","TT");
    	countryCodes.put("214","TN");
    	countryCodes.put("215","TR");
    	countryCodes.put("216","TM");
    	countryCodes.put("217","TC");
    	countryCodes.put("218","TV");
    	countryCodes.put("219","UG");
    	countryCodes.put("220","UA");
    	countryCodes.put("221","AE");
    	countryCodes.put("222","GB");
    	countryCodes.put("223","US");
    	countryCodes.put("224","UM");
    	countryCodes.put("225","UY");
    	countryCodes.put("226","UZ");
    	countryCodes.put("227","VU");
    	countryCodes.put("228","VA");
    	countryCodes.put("229","VE");
    	countryCodes.put("230","VN");
    	countryCodes.put("231","VG");
    	countryCodes.put("232","VI");
    	countryCodes.put("233","WF");
    	countryCodes.put("234","EH");
    	countryCodes.put("235","YE");
    	countryCodes.put("236","YU");
    	countryCodes.put("237","ZR");
    	countryCodes.put("238","ZM");
    	countryCodes.put("239","ZW");    	
    	
    	if (countryCodes.containsValue(countryCode2))
    	{
    		return true;
    	}
    	
        return false;
    }

    private boolean GetIdBasedOnCountryCode3(String countryCode3)
    {
    	countryCode3 = countryCode3.toUpperCase();    	
    	HashMap<String, String>  countryCodes = new HashMap<String, String>();
    	
    	countryCodes.put("1","AFG");
    	countryCodes.put("2","ALB");
    	countryCodes.put("3","DZA");
    	countryCodes.put("4","ASM");
    	countryCodes.put("5","AND");
    	countryCodes.put("6","AGO");
    	countryCodes.put("7","AIA");
    	countryCodes.put("8","ATA");
    	countryCodes.put("9","ATG");
    	countryCodes.put("10","ARG");
    	countryCodes.put("11","ARM");
    	countryCodes.put("12","ABW");
    	countryCodes.put("13","AUS");
    	countryCodes.put("14","AUT");
    	countryCodes.put("15","AZE");
    	countryCodes.put("16","BHS");
    	countryCodes.put("17","BHR");
    	countryCodes.put("18","BGD");
    	countryCodes.put("19","BRB");
    	countryCodes.put("20","BLR");
    	countryCodes.put("21","BEL");
    	countryCodes.put("22","BLZ");
    	countryCodes.put("23","BEN");
    	countryCodes.put("24","BMU");
    	countryCodes.put("25","BTN");
    	countryCodes.put("26","BOL");
    	countryCodes.put("27","BIH");
    	countryCodes.put("28","BWA");
    	countryCodes.put("29","BVT");
    	countryCodes.put("30","BRA");
    	countryCodes.put("31","IOT");
    	countryCodes.put("32","BRN");
    	countryCodes.put("33","BGR");
    	countryCodes.put("34","BFA");
    	countryCodes.put("35","BDI");
    	countryCodes.put("36","KHM");
    	countryCodes.put("37","CMR");
    	countryCodes.put("38","CAN");
    	countryCodes.put("39","CPV");
    	countryCodes.put("40","CYM");
    	countryCodes.put("41","CAF");
    	countryCodes.put("42","TCD");
    	countryCodes.put("43","CHL");
    	countryCodes.put("44","CHN");
    	countryCodes.put("45","CXR");
    	countryCodes.put("46","CCK");
    	countryCodes.put("47","COL");
    	countryCodes.put("48","COM");
    	countryCodes.put("49","COG");
    	countryCodes.put("50","COK");
    	countryCodes.put("51","CRI");
    	countryCodes.put("52","CIV");
    	countryCodes.put("53","HRV");
    	countryCodes.put("54","CUB");
    	countryCodes.put("55","CYP");
    	countryCodes.put("56","CZE");
    	countryCodes.put("57","DNK");
    	countryCodes.put("58","DJI");
    	countryCodes.put("59","DMA");
    	countryCodes.put("60","DOM");
    	countryCodes.put("61","TMP");
    	countryCodes.put("62","ECU");
    	countryCodes.put("63","EGY");
    	countryCodes.put("64","SLV");
    	countryCodes.put("65","GNQ");
    	countryCodes.put("66","ERI");
    	countryCodes.put("67","EST");
    	countryCodes.put("68","ETH");
    	countryCodes.put("69","FLK");
    	countryCodes.put("70","FRO");
    	countryCodes.put("71","FJI");
    	countryCodes.put("72","FIN");
    	countryCodes.put("73","FRA");
    	countryCodes.put("74","FXX");
    	countryCodes.put("75","GUF");
    	countryCodes.put("76","PYF");
    	countryCodes.put("77","ATF");
    	countryCodes.put("78","GAB");
    	countryCodes.put("79","GMB");
    	countryCodes.put("80","GEO");
    	countryCodes.put("81","DEU");
    	countryCodes.put("82","GHA");
    	countryCodes.put("83","GIB");
    	countryCodes.put("84","GRC");
    	countryCodes.put("85","GRL");
    	countryCodes.put("86","GRD");
    	countryCodes.put("87","GLP");
    	countryCodes.put("88","GUM");
    	countryCodes.put("89","GTM");
    	countryCodes.put("90","GIN");
    	countryCodes.put("91","GNB");
    	countryCodes.put("92","GUY");
    	countryCodes.put("93","HTI");
    	countryCodes.put("94","HMD");
    	countryCodes.put("95","HND");
    	countryCodes.put("96","HKG");
    	countryCodes.put("97","HUN");
    	countryCodes.put("98","ISL");
    	countryCodes.put("99","IND");
    	countryCodes.put("100","IDN");
    	countryCodes.put("101","IRN");
    	countryCodes.put("102","IRQ");
    	countryCodes.put("103","IRL");
    	countryCodes.put("104","ISR");
    	countryCodes.put("105","ITA");
    	countryCodes.put("106","JAM");
    	countryCodes.put("107","JPN");
    	countryCodes.put("108","JOR");
    	countryCodes.put("109","KAZ");
    	countryCodes.put("110","KEN");
    	countryCodes.put("111","KIR");
    	countryCodes.put("112","PRK");
    	countryCodes.put("113","KOR");
    	countryCodes.put("114","KWT");
    	countryCodes.put("115","KGZ");
    	countryCodes.put("116","LAO");
    	countryCodes.put("117","LVA");
    	countryCodes.put("118","LBN");
    	countryCodes.put("119","LSO");
    	countryCodes.put("120","LBR");
    	countryCodes.put("121","LBY");
    	countryCodes.put("122","LIE");
    	countryCodes.put("123","LTU");
    	countryCodes.put("124","LUX");
    	countryCodes.put("125","MAC");
    	countryCodes.put("126","MKD");
    	countryCodes.put("127","MDG");
    	countryCodes.put("128","MWI");
    	countryCodes.put("129","MYS");
    	countryCodes.put("130","MDV");
    	countryCodes.put("131","MLI");
    	countryCodes.put("132","MLT");
    	countryCodes.put("133","MHL");
    	countryCodes.put("134","MTQ");
    	countryCodes.put("135","MRT");
    	countryCodes.put("136","MUS");
    	countryCodes.put("137","MYT");
    	countryCodes.put("138","MEX");
    	countryCodes.put("139","FSM");
    	countryCodes.put("140","MDA");
    	countryCodes.put("141","MCO");
    	countryCodes.put("142","MNG");
    	countryCodes.put("143","MSR");
    	countryCodes.put("144","MAR");
    	countryCodes.put("145","MOZ");
    	countryCodes.put("146","MMR");
    	countryCodes.put("147","NAM");
    	countryCodes.put("148","NRU");
    	countryCodes.put("149","NPL");
    	countryCodes.put("150","NLD");
    	countryCodes.put("151","ANT");
    	countryCodes.put("152","NCL");
    	countryCodes.put("153","NZL");
    	countryCodes.put("154","NIC");
    	countryCodes.put("155","NER");
    	countryCodes.put("156","NGA");
    	countryCodes.put("157","NIU");
    	countryCodes.put("158","NFK");
    	countryCodes.put("159","MNP");
    	countryCodes.put("160","NOR");
    	countryCodes.put("161","OMN");
    	countryCodes.put("162","PAK");
    	countryCodes.put("163","PLW");
    	countryCodes.put("164","PAN");
    	countryCodes.put("165","PNG");
    	countryCodes.put("166","PRY");
    	countryCodes.put("167","PER");
    	countryCodes.put("168","PHL");
    	countryCodes.put("169","PCN");
    	countryCodes.put("170","POL");
    	countryCodes.put("171","PRT");
    	countryCodes.put("172","PRI");
    	countryCodes.put("173","QAT");
    	countryCodes.put("174","REU");
    	countryCodes.put("175","ROM");
    	countryCodes.put("176","RUS");
    	countryCodes.put("177","RWA");
    	countryCodes.put("178","KNA");
    	countryCodes.put("179","LCA");
    	countryCodes.put("180","VCT");
    	countryCodes.put("181","WSM");
    	countryCodes.put("182","SMR");
    	countryCodes.put("183","STP");
    	countryCodes.put("184","SAU");
    	countryCodes.put("185","SEN");
    	countryCodes.put("186","SYC");
    	countryCodes.put("187","SLE");
    	countryCodes.put("188","SGP");
    	countryCodes.put("189","SVK");
    	countryCodes.put("190","SVN");
    	countryCodes.put("191","SLB");
    	countryCodes.put("192","SOM");
    	countryCodes.put("193","ZAF");
    	countryCodes.put("194","SGS");
    	countryCodes.put("195","ESP");
    	countryCodes.put("196","LKA");
    	countryCodes.put("197","SHN");
    	countryCodes.put("198","SPM");
    	countryCodes.put("199","SDN");
    	countryCodes.put("200","SUR");
    	countryCodes.put("201","SJM");
    	countryCodes.put("202","SWZ");
    	countryCodes.put("203","SWE");
    	countryCodes.put("204","CHE");
    	countryCodes.put("205","SYR");
    	countryCodes.put("206","TWN");
    	countryCodes.put("207","TJK");
    	countryCodes.put("208","TZA");
    	countryCodes.put("209","THA");
    	countryCodes.put("210","TGO");
    	countryCodes.put("211","TKL");
    	countryCodes.put("212","TON");
    	countryCodes.put("213","TTO");
    	countryCodes.put("214","TUN");
    	countryCodes.put("215","TUR");
    	countryCodes.put("216","TKM");
    	countryCodes.put("217","TCA");
    	countryCodes.put("218","TUV");
    	countryCodes.put("219","UGA");
    	countryCodes.put("220","UKR");
    	countryCodes.put("221","ARE");
    	countryCodes.put("222","GBR");
    	countryCodes.put("223","USA");
    	countryCodes.put("224","UMI");
    	countryCodes.put("225","URY");
    	countryCodes.put("226","UZB");
    	countryCodes.put("227","VUT");
    	countryCodes.put("228","VAT");
    	countryCodes.put("229","VEN");
    	countryCodes.put("230","VNM");
    	countryCodes.put("231","VGB");
    	countryCodes.put("232","VIR");
    	countryCodes.put("233","WLF");
    	countryCodes.put("234","ESH");
    	countryCodes.put("235","YEM");
    	countryCodes.put("236","YUG");
    	countryCodes.put("237","ZAR");
    	countryCodes.put("238","ZMB");
    	countryCodes.put("239","ZWE");
    	    	
    	if (countryCodes.containsValue(countryCode3))
    	{
    		return true;
    	}
    	
        return false;
    }
    
    private boolean GetIdBasedOnCountryName(String countryName)
    {    	
    	countryName = countryName.toUpperCase();    	
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
    	    	
    	if (countryCodes.containsValue(countryName))
    	{
    		return true;
    	}
    	
        return false;
    }
    
}
