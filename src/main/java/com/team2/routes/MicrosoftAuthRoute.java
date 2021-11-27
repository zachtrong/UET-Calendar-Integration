package com.team2.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.team2.model.GoogleAuthToken;

public class MicrosoftAuthRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:microsoft-auth")
			.unmarshal(new JacksonDataFormat(GoogleAuthToken.class));

	}

}
