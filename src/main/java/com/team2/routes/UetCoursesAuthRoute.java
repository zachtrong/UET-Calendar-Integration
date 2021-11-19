package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthToken;

public class UetCoursesAuthRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
		
		from("direct:uet-auth")
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.to("https://courses.uet.vnu.edu.vn/login/token.php?service=moodle_mobile_app&bridgeEndpoint=true")
			.unmarshal(new JacksonDataFormat(UetAuthToken.class))
			.to("log:com.team2.routes?level=INFO")
			  
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.process(exchange -> exchange.getIn().setBody("wstoken=" + exchange.getIn().getBody(UetAuthToken.class).getToken() 
					  + "&wsfunction=core_webservice_get_site_info"))
			.to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?moodlewsrestformat=json&bridgeEndpoint=true")
			.unmarshal(new JacksonDataFormat(UetAuthInfo.class))
			.to("log:com.team2.routes?level=INFO")
			.to("direct:rest-response/success");
	}

}
