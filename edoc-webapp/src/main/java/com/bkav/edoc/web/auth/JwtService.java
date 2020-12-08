package com.bkav.edoc.web.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.Logger;

public class JwtService {

    public static DecodedJWT getClaims(String token) {
        DecodedJWT claims = null;
        try {
            claims = JWT.decode(token);
        } catch (Exception e) {
            LOGGER.error("Error decode jwt ", e);
        }
        return claims;
    }

    private static final Logger LOGGER = Logger.getLogger(JwtService.class);
}
