package com.team2.routes;

import java.io.FileReader;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.commons.io.IOUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
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
	public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Google Auth";
	
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

				// Use access token to call API
				GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//				Drive drive =
//				    new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
//				        .setApplicationName("Auth Code Exchange Demo")
//				        .build();
//				File file = drive.files().get("appfolder").execute();

				// Get profile info from ID token
				GoogleIdToken idToken = tokenResponse.parseIdToken();
				GoogleIdToken.Payload payload = idToken.getPayload();
				String userId = payload.getSubject();  // Use this value as a key to identify a user.
				String email = payload.getEmail();
				boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
				String name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				String locale = (String) payload.get("locale");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");

				System.out.println(idToken + " " + userId + " " + email + " " + name + " " + pictureUrl);
				
		        Gmail service = new Gmail.Builder(NET_HTTP_TRANSPORT, GSON_FACTORY, credential)
		                .setApplicationName(APPLICATION_NAME)
		                .build();
		        
		        // Print the labels in the user's account.
		        String user = "me";
		        ListMessagesResponse listMessages = service.users().messages().list(user).execute();
		        List<Message> messages = listMessages.getMessages();
		        
		        if (messages.isEmpty()) {
		            System.out.println("No messages found.");
		        } else {
		            System.out.println("Messages:");
		            for (Message message : messages) {
		            	Message detail = service.users().messages().get("me", message.getId()).setFormat("full").execute();
		                System.out.printf("- %s\n", detail.getSnippet());
		            }
		        }

			})
			.marshal(new JacksonDataFormat(GoogleAuthToken.class))
			.to("direct:rest-response/success", "file:src/data/?fileName=google_auth.json");	
	}

}
