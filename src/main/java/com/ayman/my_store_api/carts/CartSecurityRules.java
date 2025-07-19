package com.ayman.my_store_api.carts;

import com.ayman.my_store_api.common.SecurityRules;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CartSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry)
    {
        registry.requestMatchers("/carts/**").permitAll();
    }
}
