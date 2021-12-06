package com.team2.routes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.builder.RouteBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.team2.model.MyEvent;


public class GmailRoute extends RouteBuilder {
	public static final String CLIENT_ID = "905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "client_secret_905434550263-fe4nhl3ec5u3r1tnkd77pq64053ddb6m.apps.googleusercontent.com.json";
	private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "Gmail Route";
	
	public static List<String> getDate(String mail) {
        Matcher m = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2})", Pattern.CASE_INSENSITIVE).matcher(mail);
        List<String> dates = new ArrayList<String>();
        while (m.find()) {
            dates.add(m.group(1));
//            System.out.println(m.group(1));
        }
        return dates;
    }
	
	public static JSONObject getJSONObjectFile(String path) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(path));
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		return jsonObject;
	}
	
	
	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		.handled(true)
		.to("direct:rest-response/failure");
	
	
		from("direct:google-gmail")
		.process(e -> {
			String accessToken = (String) getJSONObjectFile("./src/data/google_auth.json")
					.get("token");
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
		        List<String> listEvents = new ArrayList<>();
		        for (Message message : messages) {
		        	Message detail = service.users().messages().get("me", message.getId()).setFormat("full").execute();
		            System.out.printf("- %s\n", detail.getSnippet());
		            List<String> dates = getDate(detail.getSnippet());
		            if (dates.size() == 1) {
		                String end_time = dates.get(0);
		                MyEvent event = new MyEvent("Date from gmail", end_time, end_time);
		                Gson gson = new Gson();
						String jsonObjectEvent = gson.toJson(event);
		                listEvents.add(jsonObjectEvent);
		                System.out.println(end_time);
		            }
		            else if(dates.size() >= 2) {
		                String start_time = dates.get(0);
		                String end_time = dates.get(1);
		                System.out.println(start_time);
		                System.out.println(end_time);
		                MyEvent event = new MyEvent("Date from gmail", start_time, end_time);
		                Gson gson = new Gson();
						String jsonObjectEvent = gson.toJson(event);
		                listEvents.add(jsonObjectEvent);
		            }
		            else {
		                System.out.println("Khong co ngay thang trong mail");
		            }
		        }
		        e.getOut().setBody(listEvents);
		    }
		})
		.to("log:com.team2.routes?level=INFO");
	}

}
