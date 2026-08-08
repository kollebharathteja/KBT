package com.kbt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * One tile shown on the dashboard's site grid, linking out to one of the
 * user's other websites (old or new). Managed entirely by the "K" super-admin
 * from the control panel (add/remove), and read publicly by the dashboard
 * so every logged-in user sees the same grid of sites.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "website_links")
public class WebsiteLink {

    @Id
    private String id;

    private String title;        // shown on the card, e.g. "My Portfolio"
    private String url;          // where the card links to
    private String imageUrl;     // thumbnail/logo shown on the card
    private String description;  // short one-line blurb shown under the title
}
