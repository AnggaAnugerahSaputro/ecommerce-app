package com.xicoraappdeveloper.ecommerce.service.security;

import com.xicoraappdeveloper.ecommerce.model.UserInfo;

public interface JwtService {

    String generateToken(UserInfo userInfo);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
