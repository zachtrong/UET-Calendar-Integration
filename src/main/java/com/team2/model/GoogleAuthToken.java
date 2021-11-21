package com.team2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleAuthToken {
	@JsonProperty("id_token")
	private String idToken;

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	@Override
	public String toString() {
		return "GoogleAuthToken [idToken=" + idToken + "]";
	}
}
