package com.cristian.ExpenseTrackerAPI.controller;

import com.cristian.ExpenseTrackerAPI.model.dto.LoginRequestDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.RegisterRequestDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.UserResponseDTO;
import com.cristian.ExpenseTrackerAPI.model.entity.User;
import com.cristian.ExpenseTrackerAPI.repository.UserRepository;
import com.cristian.ExpenseTrackerAPI.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(PasswordEncoder passwordEncoder, TokenService tokenService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @Operation(tags = {"Auth"}, description = "Log in to use the API")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO body) {
        User user = userRepository.findByUsername(body.username()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            var token = tokenService.generateToken(user);
            return ResponseEntity.ok(new UserResponseDTO(user.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    @Operation(tags = {"Auth"}, description = "Register to use the API")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = userRepository.findByUsername(body.username());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(body.username());
            newUser.setName(body.name());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new UserResponseDTO(newUser.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
