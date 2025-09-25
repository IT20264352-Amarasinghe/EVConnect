package com.evconnect.utils;

import com.auth0.android.jwt.JWT;
import com.evconnect.models.UserInfo;

/**
 * JwtUtils is a utility class responsible for decoding a JSON Web Token (JWT)
 * and extracting specific user information (claims) from its payload.
 */
public class JwtUtils {
    public static UserInfo extractUserInfo(String token) {
        JWT jwt = new JWT(token);

        String name = jwt.getClaim("unique_name").asString(); // or ClaimTypes.Name depending on backend
        String nic = jwt.getClaim("NIC").asString();
        String email = jwt.getClaim("email").asString();
        String role = jwt.getClaim("role").asString();

        //Create and return the UserInfo data object.
        return new UserInfo(name, nic, email, role);
    }
}