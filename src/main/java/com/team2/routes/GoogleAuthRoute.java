package com.team2.routes;

import java.io.InputStream;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.team2.model.GoogleAuthToken;

public class GoogleAuthRoute extends RouteBuilder {
	public static final String CLIENT_ID = "905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
	
	@Override
	public void configure() throws Exception {
		
		InputStream inputStream = this.getContext().getClassResolver().loadResourceAsStream(CLIENT_SECRET);

        GoogleClientSecrets clientSecrets = GsonFactory.getDefaultInstance()
				.fromInputStream(inputStream, GoogleClientSecrets.class);
		
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
	
		from("direct:google-auth")
			.unmarshal(new JacksonDataFormat(GoogleAuthToken.class))
			.process(e -> {
				GoogleAuthToken authToken = e.getIn().getBody(GoogleAuthToken.class);
				
				GoogleTokenResponse tokenResponse =
				          new GoogleAuthorizationCodeTokenRequest(
				        	  NET_HTTP_TRANSPORT,
				        	  GSON_FACTORY,
				              "https://oauth2.googleapis.com/token",
				              clientSecrets.getDetails().getClientId(),
				              clientSecrets.getDetails().getClientSecret(),
				              authToken.getCode(),
				              WebServiceRoute.HOSTNAME)  // Specify the same redirect URI that you use with your web
							                             // app. If you don't have a web version of your app, you can
							                             // specify an empty string.
				              .execute();
				
				
				String accessToken = tokenResponse.getAccessToken();
				e.getOut().setBody(accessToken);
			})
			.marshal(new JacksonDataFormat(GoogleAuthToken.class))	
			.to("direct:rest-response/success", "file:src/data/?fileName=google_auth.json");	
	}

}