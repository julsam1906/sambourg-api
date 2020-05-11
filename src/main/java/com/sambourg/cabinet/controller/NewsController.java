/**
 * 
 */
package com.sambourg.cabinet.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sambourg.cabinet.dao.impl.NewsDaoImpl;
import com.sambourg.cabinet.model.News;

/**
 * @author noverka
 *
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class NewsController {

	@Autowired
	private NewsDaoImpl newsDaoImpl;

	private Log log = LogFactory.getLog(NewsController.class);

	@PostMapping(value = "/sambourg/saveNews")
	@ResponseBody
	public void saveNews(@RequestBody String news) {
		Gson g = new Gson();
		News p = g.fromJson(news, News.class);
		newsDaoImpl.saveNews(p);
	}
	
	@PostMapping(value = "/sambourg/updateNews")
	@ResponseBody
	public void updateNews(@RequestBody String news) {
		Gson g = new Gson();
		News p = g.fromJson(news, News.class);
		newsDaoImpl.updateNews(p);
	}
	
	@DeleteMapping(value = "/sambourg/deleteNews")
	@ResponseBody
	public void deleteNews(@RequestParam(value = "titre") String titre) {
		log.info("Titre : "+titre.getClass());
		newsDaoImpl.deleteNews(titre);
	}
	
	@GetMapping(value = "/sambourg/allNews")
	@ResponseBody
	public List<News> getAllNews() throws InterruptedException {

		Map<String, News> news = newsDaoImpl.getAll();
		List<News> list = new ArrayList<News>();
		do {
			log.info(new Date());
		}while (news.size() == 0);
		for (Map.Entry<String, News> entry : news.entrySet()) {
			list.add(entry.getValue());
		}
		
		return list;
	}

}
