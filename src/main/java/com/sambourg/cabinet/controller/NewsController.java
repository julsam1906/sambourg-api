/**
 * 
 */
package com.sambourg.cabinet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sambourg.cabinet.dao.impl.NewsDaoImpl;
import com.sambourg.cabinet.model.News;

/**
 * @author noverka
 *
 */
@RestController
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

}
