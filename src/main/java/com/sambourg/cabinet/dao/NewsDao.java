/**
 * 
 */
package com.sambourg.cabinet.dao;

import java.util.Map;

import com.sambourg.cabinet.model.News;

/**
 * @author noverka
 *
 */
public interface NewsDao {
	
	/**
	 * 
	 * @param news
	 */
	public void saveNews(News news);
	
	/**
	 * 
	 * @param news
	 */
	public void updateNews(News news);
	
	/**
	 * 
	 * @param key
	 */
	public void deleteNews(String titre);
	
	/**
	 * 
	 * @return
	 */
	public Map<String, News> getAll();
	

}
