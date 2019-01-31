package subscriber;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ServerSubscriber implements Subscriber<String> {

	private Subscription sub;
	private List<Byte[]> byteArrays = new ArrayList<>();

	private Runtime rt = Runtime.getRuntime();

	@Override
	public void onSubscribe(Subscription s) {
		this.sub = s;
		s.request(1);
	}

	@Override
	public void onNext(String t) {
		synchronized (this) {
			byteArrays.add(new Byte[4024]);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			double freeMemory = (double) rt.freeMemory();
			double totalMemory = (double) rt.totalMemory();
			double memoryPercentage = freeMemory / totalMemory;
			System.out.println(freeMemory + " " + totalMemory + " " + memoryPercentage);
			if (memoryPercentage > 0.8) {
				System.out.println("requesting one more at request with" + t);
				sub.request(1);
			} else {
				sub.request(1);
				System.out.println("stop processing at request with " + t);
			}
		}
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}

}
