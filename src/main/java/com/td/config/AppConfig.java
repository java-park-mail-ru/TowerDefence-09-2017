package com.td.config;

import com.td.domain.User;
import com.td.dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class AppConfig {

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
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://frontend.debug:8000");
        config.addAllowedOrigin("https://tdteam.herokuapp.com");

        config.addAllowedHeader("*");
        config.addAllowedHeader("Content-Type");

        config.addAllowedMethod("*");

        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
