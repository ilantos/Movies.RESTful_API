package nc.lab2.ilchenko.movies.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {
    @Value("${services.threads.async.max}")
    private int maxThreads;
    @Value("${filename.output.doc}")
    private String filenameDoc;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(maxThreads);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public HttpHeaders docControllerHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(filenameDoc)
                        .build());
        return headers;
    }
}
