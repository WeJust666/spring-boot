package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.MessageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.security.Principal;

@Controller
public class MessageController {
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public MessageController(MessageService messageService, MessageRepository messageRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("messages", messageService.getAllMessages());
        return "index";
    }

    @PostMapping("/addMessage")
    public String addMessage(@RequestParam String message, Principal principal) {
        if (principal != null && !principal.getName().equals("anonymousUser")) {
            String username = principal.getName();
            User currentUser = userRepository.findByUsername(username).orElse(null);

            if (currentUser != null) {
                Message msg = new Message();
                msg.setMessage(message);
                msg.setUsername(currentUser.getUsername());
                msg.setAvatar(currentUser.getAvatar());
                messageService.saveMessage(msg);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/deleteMessage/{id}")
    public String deleteMessage(@PathVariable Long id, Authentication authentication) {
        Message msg = messageRepository.findById(id).orElse(null);
        if(msg!=null){
            String currentUser = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
            if (isAdmin || msg.getUsername().equals(currentUser)) {
                messageRepository.deleteById(id);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public Resource serveFile(@PathVariable String filename) throws MalformedURLException {
        Path path = fileStorageService.getFilePath(filename);
        return new UrlResource(path.toUri());
    }
}
