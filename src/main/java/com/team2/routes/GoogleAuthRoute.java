package com.team2.routes;

import java.util.Collections;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.team2.model.GoogleAuthToken;
import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthToken;

/*
	// Print user identifier
	String userId = payload.getSubject();
	System.out.println("User ID: " + userId);
	
	// Get profile information from payload
	String email = payload.getEmail();
	boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
	String name = (String) payload.get("name");
	String pictureUrl = (String) payload.get("picture");
	String locale = (String) payload.get("locale");
	String familyName = (String) payload.get("family_name");
	String givenName = (String) payload.get("given_name");
	
	// Use or store profile information
	// ...
	System.out.print(email + " " + name + " " + pictureUrl + " " + locale + " " + familyName + " " + givenName);
*/

public class GoogleAuthRoute extends RouteBuilder {

	public static final String CLIENT_ID = "905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com";
	
	@Override
	public void configure() throws Exception {
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
			    // Specify the CLIENT_ID of the app that accesses the backend:
			    .setAudience(Collections.singletonList(CLIENT_ID))
			    // Or, if multiple clients access the backend:
			    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
			    .build();
		
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
	
		from("direct:google-auth")
			.unmarshal(new JacksonDataFormat(GoogleAuthToken.class))
			.process(exchange -> {
				GoogleIdToken idToken = verifier.verify(exchange.getIn().getBody(GoogleAuthToken.class).getIdToken());
				if (idToken != null) {
				  Payload payload = idToken.getPayload();
				  exchange.getOut().setBody(payload);
				} else {
				  System.out.println("Invalid ID token.");
				}
			})
			.marshal(new JacksonDataFormat(GoogleAuthToken.class))
			.to("direct:rest-response/success");	
	}

}
