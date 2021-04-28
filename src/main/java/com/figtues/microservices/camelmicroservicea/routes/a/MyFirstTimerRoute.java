package com.figtues.microservices.camelmicroservicea.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFirstTimerRoute extends RouteBuilder {

	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;
	
	@Autowired
	private SimpleLoggingProcessingComponent loggingComponent;

	@Override
	public void configure() throws Exception {
		// timer
		// transform
		// log

		from("timer:first-timer") // queue
		.log("${body}")
		.transform().constant("My Constant Message")
		.log("${body}")
		
		//Processing
		//Transformation
		
		.bean(getCurrentTimeBean, "getCurrentTime")
		.log("${body}")
		.bean(loggingComponent)
		.log("${body}")
		.process(new SimpleLoggingProcessor())
		.to("log:first-timer"); // database/another queue

	}

}

@Component
class GetCurrentTimeBean {
	public String getCurrentTime() {
		return "Time is now " + LocalDateTime.now();
	}
}

@Component
class SimpleLoggingProcessingComponent {
	
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
	
	public void process(String message) {
		logger.info("SimpleLoggingProcessingComponent {}", message);
	}
}


class SimpleLoggingProcessor implements Processor {

	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		logger.info("SimpleLoggingProcessor {}", exchange.getMessage().getBody());
		

	}

}