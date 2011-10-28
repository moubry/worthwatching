package com.moubry.tomatoratings;

public class CategoryListItem implements CategorizedListItem{

	private final String title;
	
	public CategoryListItem(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}
}
