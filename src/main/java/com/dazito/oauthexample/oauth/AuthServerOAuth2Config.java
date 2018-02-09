package com.dazito.oauthexample.oauth;

import com.dazito.oauthexample.config.AppConfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Created by daz on 27/06/2017.
 */
@EnableAuthorizationServer
@Configuration
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {
    
    private final AuthenticationManager authenticationManager;
    private final DataSource dataSource;
    
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    public AuthServerOAuth2Config(AuthenticationManager authenticationManager, DataSource dataSource) {
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*
         * Allow our tokens to be delivered from our token access point as well as for tokens
         * to be validated from this point
         */
        security.checkTokenAccess("permitAll()");
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore); // Persist the tokens in the database
    }
}
