/**
 * 
 */
package com.sambourg.cabinet.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

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
@CrossOrigin(origins = {"http://localhost:3000", "https://youtube.com"}, allowCredentials = "true")
public class NewsController {

	@Autowired
	private NewsDaoImpl newsDaoImpl;

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
		newsDaoImpl.deleteNews(titre);
	}
	
	@GetMapping(value = "/sambourg/allNews", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllNews() {
		Map<String, News> map = new HashMap<String, News>();
		String json = "";
		Map<String, News> news = newsDaoImpl.getAll();
		final Instant deadline = Instant.now().plus(500, ChronoUnit.MILLIS);
		while ((Instant.now().isBefore(deadline)) && (news.size() == 0)) {
		}

		for (Map.Entry<String, News> entry : news.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

}
