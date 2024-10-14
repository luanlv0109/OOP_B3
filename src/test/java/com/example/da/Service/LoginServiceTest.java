package com.example.da.Service;


import com.example.da.Service.impl.LoginServiceImpl;
import com.example.da.domain.User;
import com.example.da.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Khởi tạo các đối tượng giả lập
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        User mockUser = new User(1L, username, password, false); // Giả lập một user

        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(mockUser);

        // Act
        User result = loginService.login(username, password);

        // Assert
        assertNotNull(result, "User should not be null");
        assertEquals(mockUser.getUsername(), result.getUsername(), "Usernames should match");
        verify(userRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    void login_ShouldReturnNull_WhenCredentialsAreInvalid() {
        // Arrange
        String username = "invalidUser";
        String password = "invalidPassword";

        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(null);

        // Act
        User result = loginService.login(username, password);

        // Assert
        assertNull(result, "User should be null for invalid credentials");
        verify(userRepository, times(1)).findByUsernameAndPassword(username, password);
    }

}
