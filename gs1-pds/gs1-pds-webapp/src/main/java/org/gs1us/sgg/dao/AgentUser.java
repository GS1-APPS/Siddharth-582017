package org.gs1us.sgg.dao;

import org.gs1us.sgg.dao.memberservice.Member;

public interface AgentUser
{
    public String getUsername();
    //public AgentAccount getAgentAccount();
    public Member getMember();
}
