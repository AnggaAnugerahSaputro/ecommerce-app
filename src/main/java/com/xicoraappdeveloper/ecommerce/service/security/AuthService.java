package com.xicoraappdeveloper.ecommerce.service.security;

import com.xicoraappdeveloper.ecommerce.model.UserInfo;

import com.xicoraappdeveloper.ecommerce.model.dto.request.AuthRequest;

public interface AuthService {

    UserInfo authenticate(AuthRequest authRequest);

}
