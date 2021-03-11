package com.bezkorder.spring.datajpa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bezkorder.spring.datajpa.model.Tutorial;
import com.bezkorder.spring.datajpa.repository.TutorialRepository;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")

public class TutorialController {

	  @Autowired
	  TutorialRepository tutorialRepository;

	  @GetMapping("/tutorials")
	  public ResponseEntity<Map<String, Object>> getAllTutorials(@RequestParam(required = false) String title,
		      @RequestParam(defaultValue = "0") int page,
		      @RequestParam(defaultValue = "3") int size) {
		  try {
			  List<Tutorial> tutorials = new ArrayList<Tutorial>();
			  Pageable paging = PageRequest.of(page, size);
		      
		      Page<Tutorial> pageTuts;
			  if (title == null)
				  pageTuts = tutorialRepository.findAll(paging);
			  else
				  pageTuts = tutorialRepository.findByTitleContaining(title, paging);
			  
			  tutorials = pageTuts.getContent();
			  
			  Map<String, Object> response = new HashMap<>();
		      response.put("tutorials", tutorials);
		      response.put("currentPage", pageTuts.getNumber());
		      response.put("totalItems", pageTuts.getTotalElements());
		      response.put("totalPages", pageTuts.getTotalPages());

		      return new ResponseEntity<>(response, HttpStatus.OK);
		  } catch (Exception e) {
			  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	    
	  }

	  @GetMapping("/tutorials/{id}")
	  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") String id) {
		  Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		  if (tutorialData.isPresent()) {
		    return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		  } else {
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		  }
	  }

	  @PostMapping("/tutorials")
	  public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		  try {
			  
			  Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
			  return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		  } catch (Exception e) {
			  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	  }

	  @PutMapping("/tutorials/{id}")
	  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") String id, @RequestBody Tutorial tutorial) {
		  Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		  if (tutorialData.isPresent()) {
			  Tutorial _tutorial = tutorialData.get();
			  _tutorial.setTitle(tutorial.getTitle());
			  _tutorial.setDescription(tutorial.getDescription());
			  _tutorial.setPublished(tutorial.isPublished());
			  return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
		  } else {
			  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		  }
	  }

	  @DeleteMapping("/tutorials/{id}")
	  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {
		  try {
			  tutorialRepository.deleteById(id);
			  return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		  } catch (Exception e) {
			  return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	  }

	  @DeleteMapping("/tutorials")
	  public ResponseEntity<HttpStatus> deleteAllTutorials() {
		  try {
			  tutorialRepository.deleteAll();
			  return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		  } catch (Exception e) {
			  return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	  }

	  @GetMapping("/tutorials/published")
	  public ResponseEntity<Map<String, Object>> findByPublished(@RequestParam(defaultValue = "0") int page,
		      @RequestParam(defaultValue = "3") int size) {
		  try {
			  List<Tutorial> tutorials = new ArrayList<Tutorial>();
		      Pageable paging = PageRequest.of(page, size);
		      
		      Page<Tutorial> pageTuts = tutorialRepository.findByPublished(true, paging);
		      tutorials = pageTuts.getContent();
		      
		      if (tutorials.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		      }
		      
		      Map<String, Object> response = new HashMap<>();
		      response.put("tutorials", tutorials);
		      response.put("currentPage", pageTuts.getNumber());
		      response.put("totalItems", pageTuts.getTotalElements());
		      response.put("totalPages", pageTuts.getTotalPages());
		      
		      return new ResponseEntity<>(response, HttpStatus.OK);
		    } catch (Exception e) {
		      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }

}
