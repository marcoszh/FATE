package com.webank.ai.fate.board.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
public class WebConfigration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**").allowedMethods("*").allowedOrigins("http://localhost:8028").allowCredentials(true);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }



    @Bean(name = "asyncServiceExecutor")
    public ThreadPoolTaskExecutor asyncServiceExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(20);

        executor.setMaxPoolSize(50);

        executor.setQueueCapacity(10);

        executor.setThreadNamePrefix("asyncServiceExecutor");


        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }


}
