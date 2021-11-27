package com.team2.model;

import java.io.Serializable;

public class Course implements Serializable{
	private int id;
	private String shortname;
	private String fullname;
	private String displayname;
	
	public Course(int id, String shortname, String fullname, String displayname) {
		this.id = id;
		this.shortname = shortname;
		this.fullname = fullname;
		this.displayname = displayname;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getShortname() {
		return shortname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getFullname() {
		return fullname;
	}
	
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getDisplayname() {
		return displayname;
	}
	
	public String toString() {
		return "Course [id=" + id + ", shortname=" + shortname + ", displayname=" + displayname 
				+ ", fullname=" + fullname + "]";
	}
}
