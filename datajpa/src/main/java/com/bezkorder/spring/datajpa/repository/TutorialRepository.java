package com.bezkorder.spring.datajpa.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bezkorder.spring.datajpa.model.Tutorial;

public interface TutorialRepository extends MongoRepository<Tutorial, String>{
	Page<Tutorial> findByTitleContaining(String title, Pageable pageable);
	Page<Tutorial> findByPublished(boolean published, Pageable pageable);
}