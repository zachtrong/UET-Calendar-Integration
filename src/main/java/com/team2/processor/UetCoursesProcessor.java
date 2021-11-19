package com.team2.processor;

import java.io.StringReader;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;

public class UetCoursesProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String strIn = exchange.getIn().getBody(String.class);
		
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar = builder.build(new StringReader(strIn));
		
		exchange.getOut().setBody(calendar);
	}

}
