package com.swp391.JewelryProduction.config.stateMachine;


import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class StateMachineServiceConfig {
    @Bean
    public StateMachineRuntimePersister<OrderStatus, OrderEvent, String> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineService<OrderStatus, OrderEvent> stateMachineService(
            StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory,
            StateMachineRuntimePersister<OrderStatus, OrderEvent, String> stateMachineRuntimePersister) {
        return new DefaultStateMachineService<OrderStatus, OrderEvent>(stateMachineFactory, stateMachineRuntimePersister);
    }
}