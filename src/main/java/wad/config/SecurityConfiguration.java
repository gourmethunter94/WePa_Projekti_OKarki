package wad.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/free").permitAll()
            .antMatchers("/access").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/news").permitAll()
            .antMatchers("/news/*").permitAll()
            .antMatchers("/news/*/image").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/tyylit.css").permitAll()
            .antMatchers("tyylit.css").permitAll()
            .anyRequest().authenticated().and()
            .formLogin().permitAll().and()
            .logout().permitAll()
            .logoutSuccessUrl("/news");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("jour").password("nalist").roles("USER").authorities("USER");
        
    }
    
}
