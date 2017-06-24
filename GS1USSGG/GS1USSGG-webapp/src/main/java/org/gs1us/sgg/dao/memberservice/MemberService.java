package org.gs1us.sgg.dao.memberservice;

public interface MemberService
{
    public User authenticate(String username, String password) throws AuthenticationException;
    public Member getMemberByGln(String gln);
}
