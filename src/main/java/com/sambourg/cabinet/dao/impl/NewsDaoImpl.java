/**
 * 
 */
package com.sambourg.cabinet.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sambourg.cabinet.dao.NewsDao;
import com.sambourg.cabinet.model.News;

/**
 * @author noverka
 *
 */
@Repository
public class NewsDaoImpl implements NewsDao {

	private Log log = LogFactory.getLog(NewsDaoImpl.class);

	private void getFirebase() {

		FirebaseOptions options = null;
		try {

			InputStream is = getClass().getResourceAsStream("/firebase.json");

			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(is))
					.setDatabaseUrl("https://sambourgetassocies.firebaseio.com/").build();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp app = FirebaseApp.initializeApp(options);
			FirebaseDatabase.getInstance(app);
		}
	}

	@Override
	public void saveNews(News news) {
		getFirebase();
		DatabaseReference database = FirebaseDatabase.getInstance().getReference();
		DatabaseReference actualites = database.child("news");

		actualites.push()
				.setValueAsync(new News(news.getTitre(), 
						news.getImage(), news.getLien(), news.getDescriptif()));

	}

	@Override
	public void updateNews(News news) {
		getFirebase();
		String key = news.getKey();
		DatabaseReference database = FirebaseDatabase.getInstance().getReference();
		DatabaseReference actu = database.child("news");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key + "/titre", news.getTitre());
		map.put(key + "/descriptif", news.getDescriptif());
		map.put(key + "/image", news.getImage());
		map.put(key + "/lien", news.getLien());
		actu.updateChildrenAsync(map);
		
	}

}
