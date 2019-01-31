package service;

import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import subscriber.ServerSubscriber;

@Component
public class TestService {

	private ServerSubscriber ss = new ServerSubscriber();

	public Mono<ServerResponse> hello(ServerRequest request) {
		System.out.println("ciao");
		Flux<String> startTime = request.bodyToFlux(String.class)
				.doOnNext(event -> System.err.println(event));
		startTime.subscribe(ss);
		LocalTime end = LocalTime.now();
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(BodyInserters.fromObject(" end:" + end.toString()));
	}
}