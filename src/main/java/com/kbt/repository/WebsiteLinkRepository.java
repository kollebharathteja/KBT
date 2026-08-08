package com.kbt.repository;

import com.kbt.model.WebsiteLink;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebsiteLinkRepository extends MongoRepository<WebsiteLink, String> {
}
