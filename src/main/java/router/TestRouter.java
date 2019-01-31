package router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import service.TestService;

@Configuration
public class TestRouter {
	 @Bean
	  public RouterFunction<ServerResponse> route(TestService testService) {
	    return RouterFunctions
	        .route(RequestPredicates.POST("/example")
	        		.and(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)), testService::hello);
	  }
}
 
