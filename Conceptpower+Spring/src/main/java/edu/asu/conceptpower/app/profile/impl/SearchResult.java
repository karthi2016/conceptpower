package edu.asu.conceptpower.app.profile.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.profile.ISearchResult;

@Service
public class SearchResult implements ISearchResult {

	private String name;
	private String description;
	private String id;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	
}
