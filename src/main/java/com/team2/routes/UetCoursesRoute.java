package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.team2.processor.UetCoursesProcessor;

public class UetCoursesRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("timer://foo?period=5000")
		  .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
		  .to("https://courses.uet.vnu.edu.vn/calendar/export_execute.php?userid=3826&authtoken=e603bc7b22fc2cbb332c0a5e138ee8700c557ece&preset_what=all&preset_time=monthnow")
		  .marshal()
		  .string()
		  .process(new UetCoursesProcessor())
		  .to("log:com.team2.routes?level=INFO");

	}

}
