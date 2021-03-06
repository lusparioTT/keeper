package com.example.keeper.config;

import com.example.keeper.model.User;
import com.example.keeper.service.UserService;
import io.github.biezhi.keeper.core.authc.*;
import io.github.biezhi.keeper.core.authc.cipher.Cipher;
import io.github.biezhi.keeper.core.authc.impl.SimpleAuthenticInfo;
import io.github.biezhi.keeper.core.authc.impl.SimpleAuthorizeInfo;
import io.github.biezhi.keeper.exception.KeeperException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class AuthorizationBean implements Authentication, Authorization {

    @Autowired
    private UserService userService;

    @Override
    public AuthenticInfo doAuthentic(AuthorToken token) throws KeeperException {
        log.info("doAuthentic :: {}", token.username());

        User user = userService.findByUsername(token.username());

        return new SimpleAuthenticInfo(
                user.getUsername(),
                user.getPassword(),
                user
        );
    }

    @Override
    public Cipher cipher() {
        return Cipher.EQUALS;
    }

    @Override
    public AuthorizeInfo doAuthorization(AuthenticInfo token) throws KeeperException {
        String username = token.username();

        log.info("doAuthorization :: {}", username);

        Set<String> roles       = userService.findRoles(username);
        Set<String> permissions = userService.findPermissions(username);

        SimpleAuthorizeInfo simpleAuthorizeInfo = new SimpleAuthorizeInfo();
        simpleAuthorizeInfo.setRoles(roles);
        simpleAuthorizeInfo.setPermissions(permissions);
        return simpleAuthorizeInfo;
    }

}
