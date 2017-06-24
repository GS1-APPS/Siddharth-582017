package org.gs1us.sgg.temp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.gs1us.sgg.dao.AgentAccount;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.jpa.JpaNextIdDao;
import org.gs1us.sgg.dao.memberservice.Member;
import org.gs1us.sgg.dao.memberservice.standalone.jpa.JpaStandaloneUserDao;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MockUserDao implements UserDetailsService
{
    @Resource
    private PasswordEncoder m_passwordEncoder;
    
    @Resource
    private JpaStandaloneUserDao m_userDao;
    
    private List<String> m_specs;
    private Map<String, UserDetails> m_userMap = new HashMap<String, UserDetails>();
    
    public MockUserDao(List<String> specs)
    {
        m_specs = specs;
    }
    
    @PostConstruct
    public void init()
    {
        for (String spec : m_specs)
        {
            String[] parts = spec.split(":");
            if (parts.length == 3)
            {
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                m_userMap.put(username, new MockUserDetails(username, password, role)); // "PRODUCT", "ACCOUNT", "BILLING"));
            }
        }
    }
   
    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException
    {
        //return m_userMap.get(username);
    	return m_userDao.getUserByUsername(username);
    }
    
    private class MockUserDetails implements UserDetails, AgentUser
    {
        private String m_username;
        private String m_password;
        private Collection<MockGrantedAuthority> m_authorities;
        private AgentAccount m_agentAccount;
        
        public MockUserDetails(String username, String password, String... authorities)
        {        	
            m_username = username;
            m_password = m_passwordEncoder.encode(password);
            m_authorities = new ArrayList<>(authorities.length);
            for (String authority : authorities)
                m_authorities.add(new MockGrantedAuthority("ROLE_" + authority));
            m_agentAccount = new AgentAccount(){};
        }
        
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities()
        {        	
            return m_authorities;
        }

        @Override
        public String getPassword()
        {
            return m_password;
        }

        @Override
        public String getUsername()
        {
            return m_username;
        }
        
        @Override
        public Member getMember()
        {
            return null;
        }
        
       /* @Override
        public AgentAccount getAgentAccount()
        {
            return m_agentAccount;
        }*/

        @Override
        public boolean isAccountNonExpired()
        {
            return true;
        }

        @Override
        public boolean isAccountNonLocked()
        {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired()
        {
            return true;
        }

        @Override
        public boolean isEnabled()
        {
            return true;
        }
        
    }
    
    private static class MockGrantedAuthority implements GrantedAuthority
    {
        private String m_authority;
        
        
        public MockGrantedAuthority(String authority)
        {
            super();
            m_authority = authority;
        }


        @Override
        public String getAuthority()
        {
            return m_authority;
        }
        
    }

}
