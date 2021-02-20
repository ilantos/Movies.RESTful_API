package nc.lab2.ilchenko.movies.utils;

import nc.lab2.ilchenko.movies.save.system.MSWordSave;
import nc.lab2.ilchenko.movies.save.system.SaveMovies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {
    @Value("${services.threads.async.max}")
    private int maxThreads;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(maxThreads);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public SaveMovies saveSystem() { return new MSWordSave(); }
}
