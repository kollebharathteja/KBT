package com.kbt.repository;

import com.kbt.model.SiteContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SiteContentRepository extends MongoRepository<SiteContent, String> {
    Optional<SiteContent> findByKey(String key);
    List<SiteContent> findBySection(String section);
}
