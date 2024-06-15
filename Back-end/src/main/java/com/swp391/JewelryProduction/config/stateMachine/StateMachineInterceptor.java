package com.swp391.JewelryProduction.config.stateMachine;

import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

@Slf4j
public class StateMachineInterceptor extends StateMachineInterceptorAdapter<OrderStatus, OrderEvent> {
    @Override
    public Exception stateMachineError(StateMachine<OrderStatus, OrderEvent> stateMachine, Exception exception) {
        log.error("StateMachine error: {}", exception.getMessage(), exception);
        return exception;
    }

    @Override
    public void preStateChange(
            State<OrderStatus, OrderEvent> state,
            Message<OrderEvent> message,
            Transition<OrderStatus, OrderEvent> transition,
            StateMachine<OrderStatus, OrderEvent> stateMachine,
            StateMachine<OrderStatus, OrderEvent> rootStateMachine)
    {
        try {
            super.preStateChange(state, message, transition, stateMachine, rootStateMachine);
        } catch (Exception e) {
            throw new RuntimeException("Error during state change", e);
        }
    }
}