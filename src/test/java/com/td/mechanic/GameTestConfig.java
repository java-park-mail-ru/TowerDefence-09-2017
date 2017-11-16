package com.td.mechanic;

import com.td.websocket.TransportService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class GameTestConfig {

    @Bean
    @Primary
    public TransportService nameService() {
        return Mockito.mock(TransportService.class);
    }
}
