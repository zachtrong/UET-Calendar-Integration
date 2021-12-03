package com.team2.model;

public class Event {
	private String title;
	private String description;
	private String timeStart;
	private String timeEnd;
	
	public Event() {
		
	}
	
	public Event(String title, String description, String timeStart, String timeEnd) {
		this.title = title;
		this.description = description;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
}
