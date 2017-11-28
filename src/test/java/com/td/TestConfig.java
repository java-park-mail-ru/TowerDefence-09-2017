package com.td;

import com.td.game.GameExecutor;
import com.td.websocket.TransportService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public TransportService nameService() {
        return Mockito.mock(TransportService.class);
    }

    @Bean
    @Primary
    public GameExecutor gameExecutor() {return Mockito.mock(GameExecutor.class);}
}
