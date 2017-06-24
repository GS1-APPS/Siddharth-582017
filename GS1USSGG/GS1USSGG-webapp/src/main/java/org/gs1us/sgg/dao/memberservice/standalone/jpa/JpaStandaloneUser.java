package org.gs1us.sgg.dao.memberservice.standalone.jpa;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.dao.memberservice.Member;
import org.gs1us.sgg.dao.memberservice.standalone.StandaloneUser;
import org.gs1us.sgg.dao.memberservice.standalone.UserState;

@Entity
@Table(name="standaloneuser")
public class JpaStandaloneUser extends StandaloneUser
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;
    
    @Basic
    private String m_username;
    
    @Basic
    private String m_firstName;
    
    @Basic
    private String m_lastName;
    
    @Basic
    private String m_timezone;
    
    @ManyToOne
    private JpaStandaloneMember m_member;
    
    @Basic
    private String m_password;

    @Basic
    private UserState m_state;

    @Basic
    private String m_roles;

    @Temporal(TemporalType.TIMESTAMP)
    private Date m_created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date m_lastLogin;

    @Basic
    private String m_passwordReset;

    @Temporal(TemporalType.TIMESTAMP)
    private Date m_passwordResetExpiration;

    @Basic
    private int m_loginCount;

    @Basic
    private String m_apiKey;
        
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getApiKey()
    {
        return m_apiKey;
    }

    public void setApiKey(String apiKey)
    {
    	m_apiKey = apiKey;
    }    
    
    public String getUsername()
    {
        return m_username;
    }

    public void setUsername(String username)
    {
        m_username = username;
    }

    public String getFirstName()
    {
        return m_firstName;
    }

    public void setFirstName(String firstName)
    {
        m_firstName = firstName;
    }

    public String getLastName()
    {
        return m_lastName;
    }

    public void setLastName(String lastName)
    {
        m_lastName = lastName;
    }

    public String getTimezone()
    {
        return m_timezone;
    }

    public void setTimezone(String timezone)
    {
        m_timezone = timezone;
    }

    public Member getMember()
    {
        return m_member;
    }

    public void setMember(Member member)
    {
        m_member = (JpaStandaloneMember)member;
    }

    
    public String getPassword()
    {
        return m_password;
    }

    public void setPassword(String password)
    {
        m_password = password;
    }



    public UserState getState()
    {
        return m_state;
    }

    public void setState(UserState state)
    {
        m_state = state;
    }

    public String[] getRoles()
    {
        return m_roles.split(",");
    }

    public void setRoles(String[] roles)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < roles.length; i++)
        {
            if (i > 0)
                buf.append(",");
            buf.append(roles[i]);
        }
        m_roles = buf.toString();
    }


    public void setRoles(String roles)
    {
        m_roles = roles;
    }

    public Date getCreated()
    {
        return m_created;
    }

    public void setCreated(Date created)
    {
        m_created = created;
    }

    public Date getLastLogin()
    {
        return m_lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        m_lastLogin = lastLogin;
    }

    
    public String getPasswordReset()
    {
        return m_passwordReset;
    }

    public void setPasswordReset(String passwordReset)
    {
        m_passwordReset = passwordReset;
    }

    public Date getPasswordResetExpiration()
    {
        return m_passwordResetExpiration;
    }

    public void setPasswordResetExpiration(Date passwordResetExpiration)
    {
        m_passwordResetExpiration = passwordResetExpiration;
    }

    public int getLoginCount()
    {
        return m_loginCount;
    }

    public void setLoginCount(int loginCount)
    {
        m_loginCount = loginCount;
    }




}
