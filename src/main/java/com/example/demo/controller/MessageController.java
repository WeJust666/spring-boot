package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MessageController {
    private final MessageService messageService;
    private final MessageRepository messageRepository;

    public MessageController(MessageService messageService, MessageRepository messageRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("messages", messageService.getAllMessages());
        return "index";
    }

    @PostMapping("/addMessage")
    public String addMessage(@RequestParam String message, Principal principal) {
        if (principal != null && !principal.getName().equals("anonymousUser")) {
            Message msg = new Message();
            msg.setUsername(principal.getName()); // 姓名使用登录的用户名
            msg.setMessage(message);
            messageService.saveMessage(msg);
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
}
