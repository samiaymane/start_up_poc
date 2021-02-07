package fr.tse.fise3.info6.start_up_poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import javax.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

public class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    protected String email;

    protected void loginUsing(String email) throws Exception {
        this.email = email;
    }

    protected void logout(){
        this.email = null;
    }

    protected ResultActions getAction(String path) throws Exception {
        if(this.email != null)
            return this.mvc.perform(get(path).with(user(this.email)));
        else{
            return this.mvc.perform(get(path));
        }
    }


}
