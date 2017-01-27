package com.gsc.repository;

import com.gsc.Application;
import com.gsc.entity.GscUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = Application.class)
public class UserAndAuthenticationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Before
    public void before() throws Exception {
        assertNotNull(userRepository);
        userRepository.save(makeUser("greg", "greg"));
    }

    @Test
    public void authenticate() throws Exception {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("greg", "greg"));
    }

    @Test(expected = BadCredentialsException.class)
    public void badPassword() throws Exception {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("greg", "rubbish"));
    }

    @Test(expected = LockedException.class)
    public void locked() throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
                authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("greg", "rubbish"));
            } catch (BadCredentialsException e) {
                // do nothing
            }
        }
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("greg", "rubbish"));
    }

    private GscUser makeUser(String name, String password) {
        GscUser gscUser = new GscUser();
        gscUser.setUserName(name);
        gscUser.setPassword(password);
        return gscUser;
    }

}
