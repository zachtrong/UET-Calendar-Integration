package com.team2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MicrosoftAuthToken {
	@JsonProperty("access_token")
	private String idToken;
}
