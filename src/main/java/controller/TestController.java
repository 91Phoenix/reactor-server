package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class TestController {

	private List<Byte[]> byteArrays = new ArrayList<>();

	private Runtime rt = Runtime.getRuntime();

	@PostMapping(value="/examplqe", consumes = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<ResponseEntity<String>> test(@RequestBody Flux<String> startTime) {
		System.out.println("ciao");
		return startTime
				.limitRate(2).publishOn(Schedulers.parallel()).map(s -> s.split("richiesta")).log()
                .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)));
//		Flux<String> map = null;
//		synchronized (this) {
//			map = startTime.flux().limitRate(getSize()).map(i -> {
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				byteArrays.add(new Byte[4024]);
//				LocalTime end = LocalTime.now();
//				String st = "start time: " + i.toString() + " end:" + end.toString();
//				System.out.println(st);
//				return st;
//			});
//		}
//		return map;
	}

	private int getSize() {
		double freeMemory = (double) rt.freeMemory();
		double totalMemory = (double) rt.totalMemory();
		double memoryPercentage = freeMemory / totalMemory;
		System.out.println(freeMemory + " " + totalMemory + " " + memoryPercentage);
		if (memoryPercentage > 0.8) {
			System.out.println("requesting one more");
			return 1;
		} else {
			System.out.println("stop processing at request with ");
		}
		return 0;
	}

}
