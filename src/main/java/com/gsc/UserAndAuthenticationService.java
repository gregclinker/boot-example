package com.gsc;

import com.gsc.entity.GscUser;
import com.gsc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(noRollbackForClassName = "BadCredentialsException")
public class UserAndAuthenticationService implements UserDetailsService, AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    private void incrementFailedLoginAttempts(String userName) {
        GscUser gscUser = userRepository.findByUserName(userName);
        if (gscUser != null) {
            gscUser.setFailedLoginAttempts(gscUser.getFailedLoginAttempts() + 1);
            userRepository.save(gscUser);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        GscUser gscUser = userRepository.findByUserName(userName);
        User user = new User(userName, gscUser.getPassword(), true, true, true, gscUser.getFailedLoginAttempts() < 3, null);
        return user;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();
        GscUser gscUser = userRepository.findByUserName(principal);
        if (gscUser == null) {
            throw new BadCredentialsException("invalid user");
        }
        else if (gscUser.getFailedLoginAttempts() >= 3) {
            throw new LockedException("account is locked");

        } else if (!credentials.equals(gscUser.getPassword())) {
            incrementFailedLoginAttempts(principal);
            throw new BadCredentialsException("invalid password");
        }
        gscUser.setFailedLoginAttempts(0);
        userRepository.save(gscUser);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
