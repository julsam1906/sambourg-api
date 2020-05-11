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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

	@Override
	public void deleteNews(String titre) {
		getFirebase();
		DatabaseReference database = FirebaseDatabase.getInstance().getReference();
		database.child("news").addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				for (DataSnapshot snap : snapshot.getChildren()) {
					for (DataSnapshot sn : snap.getChildren()) {
						if (sn.getValue(String.class).trim().equals(titre.trim())) {
							snap.getRef().setValueAsync(null);
							log.info("valeur mise à nulle");
							break;
						}
					}
				}

			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public Map<String, News> getAll() {
		getFirebase();
		Map<String, News> mapNews = new HashMap<>();
		DatabaseReference database = FirebaseDatabase.getInstance().getReference();
		
		database.child("news").addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				for (DataSnapshot snap : snapshot.getChildren()) {
					News news = snap.getValue(News.class);
					news.setKey(snap.getKey());
					mapNews.put(snap.getKey(), news);
				}
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
		
		log.info("Fin de getAll dao");
		return mapNews;
	}

}
