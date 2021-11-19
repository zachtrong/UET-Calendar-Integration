package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class RestResponseSuccessRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:rest-response/success")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, constant("application/json"));
				exchange.getMessage().setBody("{\"status\": \"Success\"}");	
			} 
		  });
	}

}
