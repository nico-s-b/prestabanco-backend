package com.example.tingeso1.services;

import com.example.tingeso1.entities.User;
import com.example.tingeso1.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    UserService userService = new UserService();

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        //Given
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        List<User> mockUsers = new ArrayList<>(Arrays.asList(user1, user2, user3));

        when(userRepository.findAll()).thenReturn(mockUsers);

        //When
        ArrayList<User> result = userService.getUsers();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockUsers, result);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSaveUser() {
        //Given
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        //When
        User result = userService.saveUser(user);

        //Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        //Given
        User user = new User();
        Long id = 1L;
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        //When
        User result = userService.getUserById(id);

        //Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testGetUserByRut() {
        //Given
        User user = new User();
        String rut = "12345678-9";
        user.setRut(rut);

        when(userRepository.findByRut(rut)).thenReturn(user);

        //When
        User result = userService.getUserByRut(rut);

        //Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByRut(rut);
    }

    @Test
    void testGetUserByEmail() {
        //Given
        User user = new User();
        String email = "test@example.com";
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        //When
        User result = userService.getUserByEmail(email);

        //Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateUser() {
        //Given
        User user = new User();
        user.setId(1L);

        when(userRepository.save(user)).thenReturn(user);

        //When
        User result = userService.updateUser(user);

        //Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        //Given
        Long id = 1L;

        //borrado exitoso no lanza excepción
        doNothing().when(userRepository).deleteById(id);

        //When
        boolean result = userService.deleteUser(id);

        //Then
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteUser_Exception() throws Exception {
        // Given
        Long id = 1L;

        // Al fallar, deleteById lanza una excepción
        doThrow(new RuntimeException("Error al eliminar")).when(userRepository).deleteById(id);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(id);
        });

        //Then
        assertEquals("Error al eliminar", exception.getMessage());
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testAuthenticate_Success() {
        //Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPass("123456");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        //When
        User userReceived = userService.authenticate(user.getEmail(),user.getPass());

        assertNotNull(userReceived);
        assertEquals(user, userReceived);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testAuthenticate_Wrongpass() {
        //Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPass("123456");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        //When
        User userReceived = userService.authenticate(user.getEmail(),"wrong_pass");

        assertNull(userReceived);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testAuthenticate_Failure_UserNotFound() {
        //Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        //When
        User userReceived = userService.authenticate("test@example.com", "123456");

        //Then
        assertNull(userReceived);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

}