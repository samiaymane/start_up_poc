package fr.tse.fise3.info6.start_up_poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    protected HttpSession session;

    protected void login(String email, String password) throws Exception {
        this.session = this.mvc.perform(post("/login").param("username",
                email).param("password", password))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();
    }

    protected void logout() throws Exception {
        this.mvc.perform(get("/logout").session((MockHttpSession)session)
                .locale(Locale.ENGLISH));
        this.session = null;
    }

}
