package org.gs1us.sgl.memberservice.standalone;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class StandaloneUserDao implements UserDetailsService
{
    public abstract List<StandaloneUser> getAllUsers();
    public abstract List<StandaloneUser> getAllUsers(String sortKey, int sortDirection);
    public abstract StandaloneUser createUser();
    public abstract void updateUser(StandaloneUser user);
    public abstract void deleteUser(StandaloneUser user);
    public abstract StandaloneUser getUser(String id);
    public abstract StandaloneUser getUserByUsername(String username);
    public abstract StandaloneUser getUserByPasswordReset(String passwordReset);
    
    @Override
    public StandaloneUser loadUserByUsername(String username) throws UsernameNotFoundException
    {
        StandaloneUser user = getUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Cannot authenticate this username or password");
        return user;
    }

}
