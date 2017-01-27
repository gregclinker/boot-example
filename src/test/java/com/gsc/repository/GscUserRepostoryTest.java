package com.gsc.repository;

import com.gsc.Application;
import com.gsc.entity.GscUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = Application.class)
public class GscUserRepostoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1() throws Exception {
        assertNotNull(userRepository);
        userRepository.save(makeUser("greg", "greg"));
        assertEquals(1, userRepository.findAll().spliterator().getExactSizeIfKnown());
        GscUser greg = userRepository.findByUserName("greg");
        assertNotNull(greg);
        assertEquals(0, greg.getFailedLoginAttempts());
        greg.setFailedLoginAttempts(greg.getFailedLoginAttempts() + 1);
        userRepository.save(greg);
        greg = userRepository.findByUserName("greg");
        assertNotNull(greg);
        assertEquals(1, greg.getFailedLoginAttempts());
        greg.setFailedLoginAttempts(greg.getFailedLoginAttempts() + 1);
        userRepository.save(greg);
    }

    private GscUser makeUser(String name, String password)
    {
        GscUser gscUser = new GscUser();
        gscUser.setUserName(name);
        gscUser.setPassword(password);
        return gscUser;
    }

}
