package org.gs1us.sgl.webapp;

import java.security.Principal;

import javax.annotation.Resource;

import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.User;
import org.springframework.security.core.Authentication;

public abstract class GBAwareController
{
    @Resource
    private GlobalBrokerService m_gbService;

    protected GlobalBrokerService getGbService()
    {
        return m_gbService;
    }
    
    protected Member getMember(Principal principal)
    {
        if (principal == null)
            return null;
        else
        {
            User user = getUser(principal);
            Member member = user.getMember();
            if (member == null)
                return null;
            else
                return member;
        }
    }

    protected User getUser(Principal principal)
    {
        if (principal == null)
            return null;
        else
            return (User)((Authentication)principal).getPrincipal();
    }
    
    /**
     * Returns the GLN of a user for user-level GB actions. Null if the brand owner agreement has not yet been signed.
     * @param principal
     * @return
     * @throws GlobalBrokerException
     */
    protected String getGBAccountGln(Principal principal) throws GlobalBrokerException
    {
        Member member = getMember(principal);
        if (member == null || member.getBrandOwnerAgreementSignedByUser() == null)
            throw new NotSignedUpException();
        else
            return member.getGln();
    }
}
