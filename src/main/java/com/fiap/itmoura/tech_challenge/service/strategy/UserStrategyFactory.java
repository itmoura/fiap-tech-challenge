package com.fiap.itmoura.tech_challenge.service.strategy;

import com.fiap.itmoura.tech_challenge.model.enums.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Set;

@Component
public class UserStrategyFactory {

    private EnumMap<UserRoleEnum, UserStrategy> strategies;

    @Autowired
    public UserStrategyFactory(Set<UserStrategy> strategySet) {
        createStrategies(strategySet);
    }

    public UserStrategy findStrategy(UserRoleEnum strategyName) {
        return strategies.get(strategyName);
    }

    private void createStrategies(Set<UserStrategy> strategySet) {
        strategies = new EnumMap<>(UserRoleEnum.class);
        strategySet.forEach(
            strategy -> strategies.put(strategy.getStrategy(), strategy)
        );
    }


}
