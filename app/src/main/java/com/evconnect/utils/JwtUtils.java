package com.evconnect.utils;

import com.auth0.android.jwt.JWT;
import com.evconnect.models.UserInfo;

public class JwtUtils {
    public static UserInfo extractUserInfo(String token) {
        JWT jwt = new JWT(token);

        String name = jwt.getClaim("unique_name").asString(); // or ClaimTypes.Name depending on backend
        String nic = jwt.getClaim("NIC").asString();
        String email = jwt.getClaim("email").asString();
        String role = jwt.getClaim("role").asString();

        return new UserInfo(name, nic, email, role);
    }
}