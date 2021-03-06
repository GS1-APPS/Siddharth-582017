package org.gs1us.sgg.dao.memberservice.standalone;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.memberservice.Member;
import org.gs1us.sgg.dao.memberservice.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class StandaloneUser implements AgentUser, UserDetails
{
    @Override
    public abstract String getUsername();
    public abstract void setUsername(String username);
        
    public abstract String getFirstName();
    public abstract void setFirstName(String firstName);

    public abstract String getLastName();
    public abstract void setLastName(String lastName);

    public abstract String getTimezone();
    public abstract void setTimezone(String timezone);

    public abstract Member getMember();
    public abstract void setMember(Member member);

    public abstract String getApiKey();
    public abstract void setApiKey(String apiKey);
        
    // Everything below is additional standalone functionality
    
    public abstract String getId();
    public abstract void setId(String id);
    
    @Override
    public abstract String getPassword();
    
    
    public abstract void setPassword(String password);

    public abstract UserState getState();
    public abstract void setState(UserState state);
    
    public abstract String[] getRoles();
    public abstract void setRoles(String[] roles);
    
    public abstract Date getCreated();
    public abstract void setCreated(Date created);
    
    public abstract Date getLastLogin();
    public abstract void setLastLogin(Date lastLogin);

    public abstract String getPasswordReset();
    public abstract void setPasswordReset(String passwordReset);
    
    public abstract Date getPasswordResetExpiration();
    public abstract void setPasswordResetExpiration(Date passwordResetExpiration);
    
    public abstract int getLoginCount();
    public abstract void setLoginCount(int loginCount);
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return new AbstractList<GrantedAuthority>()
        {
            @Override
            public GrantedAuthority get(int index)
            {
                return new SimpleGrantedAuthority(getRoles()[index]);
            }

            @Override
            public int size()
            {
                return getRoles().length;
            }
        };
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return getState() == UserState.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return getState() == UserState.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return getState() == UserState.ACTIVE;
    }

    @Override
    public boolean isEnabled()
    {
        return getState() == UserState.ACTIVE;
    }
    
    public String getSalutation()
    {
        if (getFirstName() != null)
            return getFirstName();
        else if (getLastName() != null)
            return getLastName();
        else
            return getUsername();
    }
    
    /*
     * (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
     * For GG module use API Key for authentication instead of password
    
    @Override
    public String getPassword() {
    	//return getApiKey();
    	return getPassword();
    }
	*/

}
