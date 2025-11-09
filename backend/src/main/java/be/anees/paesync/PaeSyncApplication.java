package be.anees.paesync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import be.anees.paesync.service.CourseService;

@SpringBootApplication
public class PaeSyncApplication implements CommandLineRunner{

	@Autowired
    private CourseService courseService;

	public static void main(String[] args) {
		SpringApplication.run(PaeSyncApplication.class, args);
	}

    @Override
    public void run(String... args) {
        courseService.syncMongoToNeo4jAndElasticsearch();
    }
}
