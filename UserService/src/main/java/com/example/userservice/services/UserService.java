package com.example.userservice.services;

import com.example.userservice.config.JwtService;
import com.example.userservice.dtos.UserRequestDTO;
import com.example.userservice.dtos.UserResponseDTO;
import com.example.userservice.entities.Token;
import com.example.userservice.entities.TokenType;
import com.example.userservice.entities.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DeviceService deviceService;

    @Autowired
    public UserService(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService,
                       AuthenticationManager authenticationManager, DeviceService deviceService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.deviceService = deviceService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> findUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponseDTO(user);
    }

    public UserResponseDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        return mapToUserResponseDTO(user);
    }

    public UserResponseDTO register(UserRequestDTO userRequestDTO) {
        User user = mapToUser(userRequestDTO);
        User savedUser = userRepository.save(user);
        deviceService.registerDeviceUser(savedUser);
        saveUserToken(savedUser);
        return mapToUserResponseDTO(savedUser);
    }

    public UserResponseDTO login(UserRequestDTO userRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRequestDTO.getEmail(), userRequestDTO.getPassword()));
        User user = userRepository.findByEmail(userRequestDTO.getEmail());
        saveUserToken(user);
        return mapToUserResponseDTO(user);
    }

    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findByUsername(userRequestDTO.getUsername());
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        updateUserInfo(existingUser, userRequestDTO);
        User updatedUser = userRepository.save(existingUser);
        deviceService.updateDeviceUser(updatedUser);
        saveUserToken(updatedUser);
        return mapToUserResponseDTO(updatedUser);
    }

    public UserResponseDTO delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        deviceService.deleteDeviceUser(user);
        userRepository.delete(user);
        return mapToUserResponseDTO(user);
    }

    public UserResponseDTO findUserByEmailAndPassword(String email, String password) {

//        User user = userRepository.findByEmailAndPassword(email, password);
        System.out.println("email: " + email);
        User user = userRepository.findByEmail(email);
        System.out.println("user: " + user);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        if (user == null) {
            return null;
        }
        return mapToUserResponseDTO(user);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        String jwtToken = findTokenByUser(user);
        if (jwtToken == null ) {
            jwtToken = jwtService.generateToken(user);
        }

        String refreshToken = jwtService.generateRefreshToken(user);
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .username(user.getUsername())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private User mapToUser(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .FirstName(userRequestDTO.getFirstName())
                .LastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .role(userRequestDTO.getRole())
                .build();
    }

    private void saveUserToken(User user) {
        String jwtToken = jwtService.generateToken(user);
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void updateUserInfo(User user, UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty() && !userRequestDTO.getPassword().equals("*****")) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setRole(userRequestDTO.getRole());
    }

    public String findTokenByUser(User user) {
        try {
            Optional<Token> token = tokenRepository.findByUserId(user.getId());
            if (token.isPresent()) {
                String foundToken = token.get().getToken();
                System.out.println("Token found for user " + user.getId() + ": " + foundToken);
                return foundToken;
            } else {
                System.out.println("No token found for user " + user.getId());
            }
        } catch (Exception e) {
            System.err.println("Error finding token for user " + user.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
