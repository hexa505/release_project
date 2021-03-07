
package com.project.release.security;

import com.project.release.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 설정 차단
                .headers().frameOptions().disable() // iframe 관련..? 잘 모르겠음
                .and()
                    .authorizeRequests()
                    .antMatchers( "/upload/**", "/members/**", "/", "/login", "/h2-console/**", "/**").permitAll() // 이 경로는 모두 허용
                    .anyRequest().authenticated() // 나머지는 authenticated된 사람만
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService) // oauth2 로그인 이후 동작
                .and()
                    .defaultSuccessUrl("/login/callback");

    }
}
