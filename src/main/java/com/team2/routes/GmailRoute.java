package com.team2.routes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class GmailRoute extends RouteBuilder {
	public static final String CLIENT_ID = "905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "Gmail Route";
	
	public static JSONObject getJSONObjectFile(String path) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(path));
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		return jsonObject;
	}
	
	
	@Override
	public void configure() throws Exception {
		final String accessToken = (String) getJSONObjectFile("D:\\System_Integration\\RedHatFuseCode\\UET-Calendar-Integration\\src\\data\\google_auth.json")
				.get("token");
		
		onException(Exception.class)
		.handled(true)
		.to("direct:rest-response/failure");
	
	
		from("direct:google-gmail")
		.process(e -> {
			GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
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
		.to("log:com.team2.routes?level=INFO");
	}

}
