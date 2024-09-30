package com.example.da.Service;

import com.example.da.domain.User;

public interface LoginService {
    User login(String username, String password);
}
