package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FileStorageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AutoController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public AutoController(UserRepository userRepository, PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "username or password is incorrect!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(required = false) MultipartFile avatar,
                           Model model) throws IOException {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("errorMsg", "username already exists!");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        if(avatar != null) {
            String fileName = fileStorageService.storeFile(avatar);
            user.setAvatar(fileName);
        }

        userRepository.save(user);
        return "redirect:/login";
    }
}
