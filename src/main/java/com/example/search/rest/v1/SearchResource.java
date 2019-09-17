package com.example.search.rest.v1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.search.data.Content;
import com.example.search.data.types.Contenttype;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author vivek thakur
 *
 */
@RestController
@RequestMapping("/content")
@Api(value = "search content - This service searches the google api and apple api and returns the books and albums", basePath = "/content", consumes = "text", produces = "application/json")
public class SearchResource {

	@Autowired
	private Environment env;

	@Autowired
	RestTemplate restTemplate;

	int maxResults;
	static final int DEFAULT_MAX_RESULTS = 5;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchResource.class);

	/**
	 * Returns the content based on text search.
	 * 
	 * @param text
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/{text}", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "This service operation provides search feature by accepting text string and returns the list of books and albums")
	public List<Content> searchContent(@PathVariable String text) throws InterruptedException, ExecutionException {

		List<Content> contents = new ArrayList<Content>();
		LOGGER.info("start searching ..");
		if (env.getProperty("max.results") != null) {
			maxResults = Integer.parseInt(env.getProperty("max.results"));
		} else {
			maxResults = DEFAULT_MAX_RESULTS;
		}

		LOGGER.info("Max results specified as -> " + maxResults);

		LOGGER.info(env.getProperty("server.port"));

		long startTime = System.currentTimeMillis();
//		CompletableFuture.supplyAsync(()-> getBooks(text)).thenRunAsync(()->getAlbums(text)).join();

		CompletableFuture<List<Content>> booksFuture = getBooks(text);
		CompletableFuture<List<Content>> albums = getAlbums(text);
		CompletableFuture.allOf(booksFuture, albums).join();

		long totalTime = System.currentTimeMillis() - startTime;

		LOGGER.info("Total Time taken => " + totalTime);
		contents.addAll(booksFuture.get());
		contents.addAll(albums.get());

		contents = contents.stream().sorted(new Comparator<Content>() {

			@Override
			public int compare(Content o1, Content o2) {
				// TODO Auto-generated method stub
				return o1.getTitle().compareTo(o2.getTitle());
			}
		}).collect(Collectors.toList());
//		LOGGER.info(books.get());
//		LOGGER.info(albums.get());
		return contents;
	}

	/**
	 * @param query
	 * @return
	 */
	@Async("asyncExecutor")
	private CompletableFuture<List<Content>> getBooks(String query) {
		JSONObject obj = null;
		JSONArray items = null;
		String result = null;
		List<Content> books = new ArrayList<Content>();
		try {
			result = restTemplate.getForObject(
					"https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=" + maxResults,
					String.class);
			LOGGER.info(result);
			obj = new JSONObject(result);

			items = obj.getJSONArray("items");

		} catch (Exception e) {
			// TODO: handle exception
		}

		for (int i = 0; i < items.length(); i++) {

			try {
				JSONObject tempJsonObj = items.getJSONObject(i).getJSONObject("volumeInfo");
				Content content = new Content();
				content.setTitle(tempJsonObj.getString("title"));
				content.setCreator(tempJsonObj.getJSONArray("authors").getString(0));
				content.setType(Contenttype.BOOK);
				books.add(content);
				LOGGER.info(tempJsonObj.getString("title"));
				LOGGER.info(tempJsonObj.getJSONArray("authors").getString(0));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return CompletableFuture.completedFuture(books);
	}

	/**
	 * @param query
	 * @return
	 */
	@Async("asyncExecutor")
	private CompletableFuture<List<Content>> getAlbums(String query) {
		String result = null;
		List<Content> albums = new ArrayList<Content>();
		JSONObject obj = null;
		JSONArray items = null;
		try {
			result = restTemplate.getForObject(
					"https://itunes.apple.com/search?term=+" + query + "&entity=album&limit=" + maxResults,
					String.class);
			LOGGER.info(result);

			obj = new JSONObject(result);
			items = obj.getJSONArray("results");
		} catch (Exception e) {
			// TODO: handle exception
		}

		for (int i = 0; i < items.length(); i++) {
			try {
				Content content = new Content();
				content.setTitle(items.getJSONObject(i).getString("collectionName"));
				content.setCreator(items.getJSONObject(i).getString("artistName"));
				content.setType(Contenttype.ALBUM);
				LOGGER.info(items.getJSONObject(i).getString("collectionName"));
				LOGGER.info(items.getJSONObject(i).getString("artistName"));
				albums.add(content);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return CompletableFuture.completedFuture(albums);
	}
}
