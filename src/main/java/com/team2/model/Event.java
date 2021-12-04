package com.team2.model;

public class Event {
	private String title;
//	private String description;
	private String start;
	private String end;
	
	public Event() {
		
	}
	
	public Event(String title, String start, String end) {
		this.title = title;
//		this.description = description;
		this.start = start;
		this.end = end;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public String getDescription() {
//		return description;
//	}
	public void setStart(String timeStart) {
		this.start = timeStart;
	}
	public String getStart() {
		return start;
	}
	public void setEnd(String timeEnd) {
		this.end = timeEnd;
	}
	public String getEnd() {
		return end;
	}
}
