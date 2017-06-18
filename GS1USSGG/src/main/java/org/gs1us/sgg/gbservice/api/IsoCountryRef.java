package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public interface IsoCountryRef
{
	public String getId();
	public String getCountryName();
	public String getCountryCodeTxt2();
	public String getCountryCodeTxt3();
	public String getCountryCodeNum();
	public String getEnabled();
	public Date getModifiedDate();
}
