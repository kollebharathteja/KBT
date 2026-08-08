package com.kbt.controller;

import com.kbt.model.SiteContent;
import com.kbt.model.User;
import com.kbt.model.WebsiteLink;
import com.kbt.repository.SiteContentRepository;
import com.kbt.repository.UserRepository;
import com.kbt.repository.WebsiteLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Everything here is locked to ROLE_SUPER_ADMIN in SecurityConfig.
 * This is the "K can change everything" surface: manage users,
 * and manage arbitrary key/value site content blocks that the
 * front-end reads to render text, colors, images, etc.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SiteContentRepository contentRepository;
    private final UserRepository userRepository;
    private final WebsiteLinkRepository websiteLinkRepository;

    // ---- Site content management ----

    @GetMapping("/content")
    public List<SiteContent> getAllContent() {
        return contentRepository.findAll();
    }

    @PostMapping("/content")
    public SiteContent upsertContent(@RequestBody SiteContent content) {
        contentRepository.findByKey(content.getKey()).ifPresent(existing -> content.setId(existing.getId()));
        return contentRepository.save(content);
    }

    @DeleteMapping("/content/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable String id) {
        contentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- User management ----

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        // never expose password hashes to the client
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("status", "K has full control");
    }

    // ---- Website link management (the site cards shown on the dashboard) ----

    @GetMapping("/websites")
    public List<WebsiteLink> getAllWebsites() {
        return websiteLinkRepository.findAll();
    }

    @PostMapping("/websites")
    public WebsiteLink addWebsite(@RequestBody WebsiteLink website) {
        website.setId(null); // always create new; editing isn't needed for add/remove workflow
        return websiteLinkRepository.save(website);
    }

    @DeleteMapping("/websites/{id}")
    public ResponseEntity<Void> deleteWebsite(@PathVariable String id) {
        websiteLinkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
