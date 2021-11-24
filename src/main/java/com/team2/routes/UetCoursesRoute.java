package com.team2.routes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team2.model.Course;
import com.team2.processor.UetCoursesProcessor;

public class UetCoursesRoute extends RouteBuilder {

	public static Course[] arrayCourses;
	@Override
	public void configure() throws Exception {
		
//		Course[] courses = {};
		
		from("direct:uet-courses")
		  .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
		  .to("log:DEBUG?showBody=true&showHeaders=true")
		  .removeHeaders("CamelHttp*")
		  .to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?wstoken=5388cd36e837d066d48fa5263f520d18&wsfunction=core_enrol_get_users_courses&userid=3818&moodlewsrestformat=json")
		  .process(
			  exchange -> {
					String coursesJson = exchange.getIn().getBody(String.class);
					Gson gson = new Gson();
					Course[] courses = gson.fromJson(coursesJson, Course[].class);
					exchange.getOut().setBody(courses);
					arrayCourses = courses;
				})
		  
		  .loop(10)
			  .process(
			     exchange -> {
			    	int index = (Integer) exchange.getProperty(Exchange.LOOP_INDEX);
					exchange.getOut().setBody(arrayCourses[index].getId());
			     })
			  .to("direct:uet-calendars")
//			  .transform(
//			    body().append(arrayCourses)
//			  )
		 .end()
		 .to("log:com.team2.routes?level=INFO");
	}

}
