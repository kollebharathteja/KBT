package com.kbt.controller;

import com.kbt.model.WebsiteLink;
import com.kbt.repository.WebsiteLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/websites")
@RequiredArgsConstructor
public class WebsiteController {

    private final WebsiteLinkRepository websiteLinkRepository;

    @GetMapping
    public List<WebsiteLink> getAll() {
        return websiteLinkRepository.findAll();
    }
}
