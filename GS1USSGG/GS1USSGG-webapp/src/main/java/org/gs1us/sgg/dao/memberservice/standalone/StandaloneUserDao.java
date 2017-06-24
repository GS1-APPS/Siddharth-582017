package org.gs1us.sgg.dao.memberservice.standalone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class StandaloneUserDao implements UserDetailsService
{
    public abstract List<StandaloneUser> getAllUsers();
    public abstract List<StandaloneUser> getAllUsers(String sortKey, int sortDirection);
    /*
    public abstract StandaloneUser createUser();
    public abstract void updateUser(StandaloneUser user);
    public abstract void deleteUser(StandaloneUser user);
    */
    public abstract StandaloneUser getUser(String id);
    public abstract StandaloneUser getUserByUsername(String username);
    public abstract StandaloneUser getUserByPasswordReset(String passwordReset);
    
    @Resource
    private PasswordEncoder m_passwordEncoder;
    
    @Override
    public StandaloneUser loadUserByUsername(String username) throws UsernameNotFoundException
    {
        StandaloneUser user = getUserByUsername(username);
        
        if (null == user)
            throw new UsernameNotFoundException("Cannot authenticate this username or password");
                              
        String m_apiKey = m_passwordEncoder.encode(user.getApiKey() );
        //return new User(user.getUsername(), user.getEncryptedPassword(), auth);
        /*
         * NOTE: OVERWRITE the password with APIKey for GG module so that authentication is performed using API Key
         */
        user.setPassword(m_apiKey);
        return user;
    }

}
