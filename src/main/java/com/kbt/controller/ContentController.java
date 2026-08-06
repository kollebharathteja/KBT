package com.kbt.controller;

import com.kbt.model.SiteContent;
import com.kbt.repository.SiteContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public, read-only view of the content K manages from the admin panel.
 * Any page on the site can call this to render text/values that K controls,
 * without needing to be authenticated. Writing still requires SUPER_ADMIN
 * via /api/admin/content (see AdminController + SecurityConfig).
 */
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final SiteContentRepository contentRepository;

    @GetMapping
    public List<SiteContent> getAll() {
        return contentRepository.findAll();
    }

    @GetMapping("/section/{section}")
    public List<SiteContent> getBySection(@PathVariable String section) {
        return contentRepository.findBySection(section);
    }
}
