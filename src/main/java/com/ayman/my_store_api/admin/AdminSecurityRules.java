package com.ayman.my_store_api.admin;

import com.ayman.my_store_api.common.SecurityRules;
import com.ayman.my_store_api.users.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules
{
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry)
    {
        registry.requestMatchers("/admin/**").hasRole(Role.ADMIN.name());
    }
}
