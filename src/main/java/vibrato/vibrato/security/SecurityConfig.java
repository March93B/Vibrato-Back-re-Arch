package vibrato.vibrato.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html");
    }
    public void configure(HttpSecurity httpSec) throws Exception {
        httpSec.csrf().disable()
                .authorizeHttpRequests()

                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()

                .anyRequest().authenticated().and().cors();

        httpSec.addFilterBefore(new SecurityFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}