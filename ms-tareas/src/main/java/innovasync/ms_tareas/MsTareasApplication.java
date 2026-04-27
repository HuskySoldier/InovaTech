package innovasync.ms_tareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTareasApplication.class, args);
	}

}
