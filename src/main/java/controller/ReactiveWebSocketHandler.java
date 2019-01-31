package controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {

	private static final ObjectMapper json = new ObjectMapper();
	private List<Byte[]> byteArrays = new ArrayList<>();

	private Runtime rt = Runtime.getRuntime();

	private Flux<String> eventFlux = Flux.generate(sink -> {

		try {
			sink.next(json.writeValueAsString("String"));
		} catch (JsonProcessingException e) {
			sink.error(e);
		}
	});

	private Flux<String> intervalFlux = Flux.interval(Duration.ofMillis(1000L)).zipWith(eventFlux,
			(time, event) -> event);

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		Mono<Void> send = webSocketSession.send(intervalFlux.map(webSocketSession::textMessage));
		webSocketSession.receive().map(WebSocketMessage::getPayloadAsText).subscribe(new Subscriber<String>() {

			Subscription s;
			int t = 0;

			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				s.request(5);
			}

			@Override
			public void onNext(String st) {
				System.out.println(st);
				synchronized (this) {
					byteArrays.add(new Byte[10024]);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					double memoryPercentage = getMemoryPercentageFree();
					if (memoryPercentage > 0.8) {
						System.out.println("requesting one more at request with" + t);
						s.request(1);
					} else {
						System.out.println("stop processing at request with " + t);
						while(getMemoryPercentageFree() < 0.8) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						s.request(1);
					}
				}
			}

			private double getMemoryPercentageFree() {
				double freeMemory = (double) rt.freeMemory();
				double totalMemory = (double) rt.totalMemory();
				double memoryPercentage = freeMemory / totalMemory;
				System.out.println(freeMemory + " " + totalMemory + " " + memoryPercentage);
				return memoryPercentage;
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub

			}
		});
		return send;
	}
}