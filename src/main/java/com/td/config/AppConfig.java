package com.td.config;

import com.td.domain.User;
import com.td.dtos.UserDto;
import com.td.websocket.GameSocketHandler;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    public WebSocketHandler gameWebSocketHandler() {
        return new PerConnectionWebSocketHandler(GameSocketHandler.class);
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .createTypeMap(UserDto.class, User.class)

                .addMappings(mapper -> mapper.skip(User::setId))
                .addMapping(UserDto::getLogin, User::setNickname);
        modelMapper
                .createTypeMap(User.class, UserDto.class)
                .addMapping(User::getNickname, UserDto::setLogin);
        return modelMapper;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setThreadNamePrefix("td-");
        threadPoolScheduler.setPoolSize(2);
        return threadPoolScheduler;
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://frontend.debug:8000");
        config.addAllowedOrigin("https://tdteam.herokuapp.com");
        config.addAllowedOrigin("https://tdgame.pw");
        config.addAllowedHeader("*");
        config.addAllowedHeader("Content-Type");

        config.addAllowedMethod("*");

        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/api/auth/*", config);

        source.registerCorsConfiguration("/api/user", config);
        source.registerCorsConfiguration("/api/user/*", config);

        source.registerCorsConfiguration("/api/scores", config);
        source.registerCorsConfiguration("/api/scores/*/page/*", config);

        return new CorsFilter(source);
    }


}
