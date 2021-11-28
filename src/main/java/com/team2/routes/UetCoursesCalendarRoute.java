package com.team2.routes;

import java.io.StringReader;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

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
			.process(e -> e.getIn().setBody("wstoken=" + e.getIn().getHeader("wstoken")
					  + "&wsfunction=core_calendar_get_calendar_export_token"))
			.to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?moodlewsrestformat=json&bridgeEndpoint=true")
			.unmarshal(new JacksonDataFormat(UetExportToken.class))
			.process(e -> e.getIn().setHeader("exporttoken", e.getIn().getBody(UetExportToken.class).getToken()))
			
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
			.to("log:com.team2.routes?level=INFO")
			.setBody(p -> "")
			.toD("https://courses.uet.vnu.edu.vn/calendar/export_execute.php?userid=${header.userid}&authtoken=${header.exporttoken}&preset_what=all&preset_time=monthnow&bridgeEndpoint=true")
			.process(e -> {
				String strIn = e.getIn().getBody(String.class);
				
				CalendarBuilder builder = new CalendarBuilder();
				Calendar calendar = builder.build(new StringReader(strIn));
				
				e.getOut().setBody(calendar);
			})
			.to("log:com.team2.routes?level=INFO");
	}

}
