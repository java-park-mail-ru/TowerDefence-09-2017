package com.td.dtos.constraints;

import com.td.game.domain.PlayerClass;
import com.td.game.resource.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;

public class GameClassValidator implements ConstraintValidator<GameClass, String> {
    private Set<String> availableClasses;

    @Autowired
    private ResourceFactory resourceFactory;


    public void initialize(GameClass constraint) {
        availableClasses = resourceFactory.loadResourceList("PlayerClassList.json", PlayerClass.class)
                .stream()
                .map(PlayerClass::toString)
                .collect(Collectors.toSet());

    }

    public boolean isValid(String obj, ConstraintValidatorContext context) {
        return availableClasses.contains(obj);
    }
}
