package fr.tse.fise3.info6.start_up_poc.security;

import fr.tse.fise3.info6.start_up_poc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppAuthProvider extends DaoAuthenticationProvider {

    @Autowired
    UserService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        String email = auth.getName();
        String password = auth.getCredentials().toString();
        UserDetails user = this.userDetailsService.loadUserByUsername(email);
        boolean passwordCheck = this.passwordEncoder.matches(password,user.getPassword());
        if (user == null || passwordCheck == false) {
            throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal());
        }
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
