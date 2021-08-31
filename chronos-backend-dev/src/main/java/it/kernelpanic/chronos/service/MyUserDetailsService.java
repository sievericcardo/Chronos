package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Service class for loading single user using its username from the database.
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao dao;

    /**
     * Retrieve the user from is username
     *
     * @param s string corresponding to the user's username
     * @return the user details
     * @throws UsernameNotFoundException if no user is found
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return dao.getUserByUsername(s);
    }
}
