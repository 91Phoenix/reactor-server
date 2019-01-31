package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import controller.TestController;
import router.TestRouter;
import service.TestService;

@SpringBootApplication
@ComponentScan(basePackageClasses = {TestRouter.class,TestService.class,TestController.class})
public class Server {

	public static void main(String[] args) {
		System.setProperty("reactor.netty.ioWorkerCount", "100");
		SpringApplication.run(Server.class, args);
	}
}