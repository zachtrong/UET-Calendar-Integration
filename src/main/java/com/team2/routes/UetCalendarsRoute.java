package com.team2.routes;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URLConnection;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.io.IOUtils;
//import org.json.JSONException;
//import org.json.JSONObject;

import com.google.gson.Gson;
import com.team2.model.Course;

public class UetCalendarsRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
		from("direct:uet-calendars")
		  .process(
			  exchange -> {
				    int id = exchange.getIn().getBody(Integer.class);
				    exchange.getOut().setBody(String.valueOf(id));
				}
		  )
		  
		  .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
		  .to("log:DEBUG?showBody=true&showHeaders=true")
		  .removeHeaders("CamelHttp*")
		 // .toD("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?wstoken=5388cd36e837d066d48fa5263f520d18&wsfunction=core_calendar_get_calendar_events&options[timestart]=1630526400&events[courseids][0]=${body}&moodlewsrestformat=json")
		  .recipientList(simple("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?wstoken=5388cd36e837d066d48fa5263f520d18&wsfunction=core_calendar_get_calendar_events&options[timestart]=1630526400&events[courseids][0]=" + "${body}" + "&moodlewsrestformat=json"))
		  .process(
			  exchange -> {
					String events = exchange.getIn().getBody(String.class);
					
//					try {
//					     JSONObject jsonObject = new JSONObject(events);
//					     System.out.println("Events: " + events);
//					     
//						 if(jsonObject.getJSONArray("events").length() > 0)
//						   exchange.getOut().setBody(events);
//							
//					}catch (JSONException err){
//						System.out.println("Error " + err.toString());
//					}
					System.out.println("Events: " + events);
				}
		  )
		  .to("log:com.team2.routes?level=INFO");
		
		
	}
}
