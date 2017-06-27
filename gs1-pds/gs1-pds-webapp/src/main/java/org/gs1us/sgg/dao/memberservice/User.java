package org.gs1us.sgg.dao.memberservice;

public interface User
{
    public String getUsername();
    public String getFirstName();
    public String getLastName();
    public String getTimezone();
    public Member getMember();
    public String getApiKey();
}
