package pl.createcompetition.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.createcompetition.config.ArgonPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImplementation userDetails;

    @Autowired
    private ArgonPasswordEncoder argonPasswordEncoder;

    @Autowired
    private RestAuthentication restAuthentication;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(argonPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(restAuthentication);
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/sign-in").permitAll()
                .antMatchers(HttpMethod.POST, "/upload").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    // TODO: INFO: ignored endpoint for test purposes only
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/favicon.ico")
                .antMatchers("/error");

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter() {
        return new UserAuthenticationFilter();
    }

}