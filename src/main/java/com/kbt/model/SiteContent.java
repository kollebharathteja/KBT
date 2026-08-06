package com.kbt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Generic key/value editable content block.
 * The "K" super admin can create/update/delete any of these,
 * and the front-end renders them dynamically - this is how "K"
 * can change anything on the site without touching code.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "site_content")
public class SiteContent {

    @Id
    private String id;

    private String key;     // e.g. "site.title", "hero.subtitle", "login.brandColor"
    private String value;   // the editable value
    private String section; // e.g. "branding", "login", "home"
}
