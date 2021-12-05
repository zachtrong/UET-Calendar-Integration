package com.team2.routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.team2.model.Event;
import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthToken;
import com.team2.model.UetExportToken;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;

public class UetCoursesCalendarRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
		
		
		from("direct:uet-courses-calendar")
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.process(e -> {
				String wstoken = (String) getJSONObjectFile("D:\\System_Integration\\RedHatFuseCode\\UET-Calendar-Integration\\src\\data\\uet_auth_token.json")
						.get("token");
				e.getIn().setBody("wstoken=" + wstoken + "&wsfunction=core_calendar_get_calendar_export_token");
			})
			.to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?moodlewsrestformat=json&bridgeEndpoint=true")
			.unmarshal(new JacksonDataFormat(UetExportToken.class))
			.process(e -> 
			{
				e.getIn().setHeader("exporttoken", e.getIn().getBody(UetExportToken.class).getToken());
			})
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
			.to("log:com.team2.routes?level=INFO")
			.process(e -> {
				int userid = ((Long) getJSONObjectFile("D:\\System_Integration\\RedHatFuseCode\\UET-Calendar-Integration\\src\\data\\uet_auth_info.json")
						.get("userid")).intValue();
				e.getIn().setBody("userid=" + userid + "&");
			})
			.to("https://courses.uet.vnu.edu.vn/calendar/export_execute.php?authtoken=${header.exporttoken}&preset_what=all&preset_time=monthnow&bridgeEndpoint=true")
			.process(e -> {
//				String filePath = new File("").getAbsolutePath();
//				System.out.println(filePath);
				
				String strIn = e.getIn().getBody(String.class);
				System.out.println(strIn);
				
				String[] arrayEvent = strIn.split("BEGIN:VEVENT");
				
				List<String> listEvents = new ArrayList<>();
				
				for(int i = 0; i < arrayEvent.length; i ++) {
					if(arrayEvent[i].contains("METHOD:PUBLISH")) continue;
					
					String title = getValueByStringRegexFromEventUETCourses("SUMMARY:", arrayEvent[i]);
//					String description = getValueByStringRegexFromEventUETCourses("DESCRIPTION:", arrayEvent[i]);
					String start = getValueByStringRegexFromEventUETCourses("DTSTART:", arrayEvent[i]);
					String end = getValueByStringRegexFromEventUETCourses("DTEND:", arrayEvent[i]);
					
					start = formatTimeFromEventUETCourses(start);
					end = formatTimeFromEventUETCourses(end);
					
					Event event = new Event(title, start, end);
					Gson gson = new Gson();
					String jsonObjectEvent = gson.toJson(event);
					
					listEvents.add(jsonObjectEvent);
				}
				
				e.getOut().setBody(listEvents);
			})
			.to("log:com.team2.routes?level=INFO");
	}
	
	public static String getValueByStringRegexFromEventUETCourses(String from, String data) {
		
		Pattern pattern = Pattern.compile(from + "(.*?)" + "\r");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return "";
	}
	
	public static JSONObject getJSONObjectFile(String path) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(path));
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		return jsonObject;
	}
	
	public static String formatTimeFromEventUETCourses(String time) {

        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String hour = String.valueOf(Integer.valueOf(time.substring(9, 11)) + 7);
        String minutes = time.substring(11, 13);
        String second = time.substring(13, 15);

//        return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + second;
        return year + "-" + month + "-" + day;
    }
}
