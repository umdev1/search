package com.example.search.data;

import com.example.search.data.types.Contenttype;

/**
 * @author vivek thakur
 *
 */
public class Content {

	private String title;
	private String creator;
	private Contenttype type;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the type
	 */
	public Contenttype getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Contenttype type) {
		this.type = type;
	}
	
	
	
}
