package com.bkav.edoc.web.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtService {

    public static DecodedJWT getClaims(String token) {
        DecodedJWT claims = null;
        try {
            claims = JWT.decode(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }


}
