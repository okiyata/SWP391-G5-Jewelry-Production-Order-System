package com.swp391.JewelryProduction.config.stateMachine;


import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Notification;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.data.support.StateMachineJackson2RepositoryPopulatorFactoryBean;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class OrderStateMachineConfig {

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

//    @Bean
//    public StateMachineJackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean() {
//        StateMachineJackson2RepositoryPopulatorFactoryBean factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
//        factoryBean.setResources(new Resource[]{new ClassPathResource("data.json")});
//        return factoryBean;
//    }


    /*---------------------------------------------------------------------------------*/
    /*--------------------DEPENDENCY INJECT NECESSARY SERVICE LAYER--------------------*/
    /*---------------------------------------------------------------------------------*/
    @Slf4j
    @Configuration
    @EnableStateMachineFactory
    @RequiredArgsConstructor
    public static class config extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

        private final OrderService orderService;
        private final NotificationService notificationService;
        private final AccountService accountService;
        private final ModelMapper modelMapper;

        private StateRepository<? extends RepositoryState> stateRepository;
        private TransitionRepository<? extends RepositoryTransition> transitionRepository;
        private StateMachineRuntimePersister<OrderStatus, OrderEvent, String> stateMachineRuntimePersister;
        private StateMachineInterceptor stateMachineInterceptor;

        @Override
        public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
            config
                    .withPersistence()
                    .runtimePersister(stateMachineRuntimePersister)
            ;
        }

        @Override
        public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
            states
                .withStates()
                    .initial(OrderStatus.REQUEST)
                    .state(OrderStatus.QUOTATION)
                    .state(OrderStatus.DESIGN)
                    .state(OrderStatus.PRODUCTION)
                    .state(OrderStatus.TRANSPORT)
                    .state(OrderStatus.CANCEL)
                    .history(OrderStatus.ORDER_RESTORED, StateConfigurer.History.DEEP)          //History state for reverting back
                    .end(OrderStatus.ORDER_COMPLETED)                                   //End state for finalize the order

                /*-----------REQUEST SUPERSTATE-----------*/
                .and()
                .withStates()
                    .parent(OrderStatus.REQUEST)
                    .initial(OrderStatus.REQUESTING)
                    .state(OrderStatus.REQ_AWAIT_APPROVAL)
                        .choice(OrderStatus.REQ_APPROVAL_PROCESS)
                        .state(OrderStatus.REQ_APPROVED)
                        .state(OrderStatus.REQ_DECLINED, )
                    .state(OrderStatus.AWAIT_ASSIGN_STAFF)
                    .state(OrderStatus.IN_EXCHANGING)

                .and()
                .withStates()
                .parent(OrderStatus.QUOTATION)
                    .initial(OrderStatus.AWAIT_QUO)
                    .state(OrderStatus.QUO_AWAIT_MANA_APPROVAL)
                        .choice(OrderStatus.QUO_MANA_APPROVAL_PROCESS)
                        .state(OrderStatus.QUO_MANA_APPROVED)
                        .state(OrderStatus.QUO_MANA_DECLINED)
                    .state(OrderStatus.QUO_AWAIT_CUST_APPROVAL)
                        .choice(OrderStatus.QUO_CUST_APPROVAL_PROCESS)
                        .state(OrderStatus.QUO_CUST_APPROVED)
                        .state(OrderStatus.QUO_CUST_DECLINED)
                    .state(OrderStatus.AWAIT_TRANSACTION)
                        .choice(OrderStatus.TRANSACTION_PROCESS)
                        .state(OrderStatus.TRANSACTION_APPROVED)
                        .state(OrderStatus.TRANSACTION_DECLINED)

                .and()
                .withStates()
                    .parent(OrderStatus.DESIGN)
                    .initial(OrderStatus.IN_DESIGNING)
                    .state(OrderStatus.DES_AWAIT_MANA_APPROVAL)
                        .choice(OrderStatus.DES_MANA_APPROVAL_PROCESS)
                        .state(OrderStatus.DES_MANA_APPROVED)
                        .state(OrderStatus.DES_MANA_DECLINED)
                    .state(OrderStatus.DES_AWAIT_CUST_APPROVAL)
                        .choice(OrderStatus.DES_CUST_APPROVAL_PROCESS)
                        .state(OrderStatus.DES_CUST_APPROVED)
                        .state(OrderStatus.DES_CUST_DECLINED)

                .and()
                .withStates()
                    .parent(OrderStatus.PRODUCTION)
                    .initial(OrderStatus.IN_PRODUCTION)
                    .state(OrderStatus.PRO_AWAIT_APPROVAL)
                        .choice(OrderStatus.PRO_APPROVAL_PROCESS)
                        .state(OrderStatus.PRO_APPROVED)
                        .state(OrderStatus.PRO_DECLINED)
                .and()
                .withStates()
                    .parent(OrderStatus.TRANSPORT)
                    .state(OrderStatus.SENT)
                    .state(OrderStatus.DELIVERED)
        ;

    }

        @Override
        public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
            transitions
                .withExternal()
                    .source(OrderStatus.CANCEL).target(OrderStatus.ORDER_RESTORED)
                    .event(OrderEvent.RESTORE_ORDER)
                .and()
                .withHistory()
                    .source(OrderStatus.ORDER_RESTORED).target(OrderStatus.REQUESTING)  //Placeholder target

                .and()
                .withLocal()
                    .source(OrderStatus.REQUESTING).target(OrderStatus.REQ_AWAIT_APPROVAL)
                    .event(OrderEvent.REQ_RECEIVED)
                    .action(notifyOrderCreateAction())
                .and()
                .withLocal()
                    .source(OrderStatus.REQ_AWAIT_APPROVAL).target(OrderStatus.REQ_APPROVAL_PROCESS)
                    .event(OrderEvent.REQ_PROCESS)
                .and()
                .withChoice()
                    .source(OrderStatus.REQ_APPROVAL_PROCESS)
                    .first(OrderStatus.REQ_APPROVED, managerRequestGuard())
                    .last(OrderStatus.REQ_DECLINED)
                .and()
                .withExternal()
                    .source(OrderStatus.REQ_DECLINED).target(OrderStatus.CANCEL)
                    .event(OrderEvent.REQ_DECLINE)
                .and()
                .withLocal()
                    .source(OrderStatus.REQ_APPROVED).target(OrderStatus.AWAIT_ASSIGN_STAFF)
                    .event(OrderEvent.REQ_APPROVE)
                .and()
                .withLocal()
                    .source(OrderStatus.AWAIT_ASSIGN_STAFF).target(OrderStatus.IN_EXCHANGING)
                    .event(OrderEvent.ASSIGN_STAFF)

                .and()
                .withExternal()
                    .source(OrderStatus.IN_EXCHANGING).target(OrderStatus.AWAIT_QUO)
                    .event(OrderEvent.FINALIZE_IDEA)

                //<editor-fold desc="QUOTATION SUPER-STATE TRANSITIONS" defaultstate="collapsed">
                .and()
                .withExternal()
                    .source(OrderStatus.AWAIT_QUO).target(OrderStatus.QUO_AWAIT_MANA_APPROVAL)
                    .event(OrderEvent.QUO_FINISH)
//            action().
            //</editor-fold>
            //<editor-fold desc="DESIGN SUPER-STATE TRANSITIONS" defaultstate="collapsed">
            //</editor-fold>

            ;

            configureCancelTransition(transitions, OrderStatus.values());
        }

        private void configureCancelTransition(
                StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions,
                OrderStatus[] statuses
        ) throws Exception {
            for (OrderStatus status : statuses) {
                if (status != OrderStatus.CANCEL) {
                    transitions
                            .withExternal()
                            .source(status)
                            .target(OrderStatus.CANCEL)
                            .event(OrderEvent.CANCEL)
                            .action(notifyCancelAction());
                }
            }
        }

        //<editor-fold desc="GUARD BEAN" defaultstate="collapsed">
        @Bean
        public Guard<OrderStatus, OrderEvent> managerRequestGuard() {
            return context -> {
                Boolean approved = (Boolean) context.getMessageHeader("managerReply");
                return approved != null && approved;
            };
        }
        //</editor-fold>

        //<editor-fold desc="ACTION BEAN" defaultstate="collapsed">
        @Bean
        public Action<OrderStatus, OrderEvent> notifyOrderCreateAction() {
            return context -> {
                Order order = context.getExtendedState().get("order", Order.class);
                Report report = context.getExtendedState().get("report", Report.class);
                List<Account> managers = accountService
                        .findAllByRole(Role.MANAGER)
                        .stream()
                        .map(acc -> modelMapper.map(acc, Account.class))
                        .toList();

                managers.forEach(manager -> {
                        Notification notification = Notification.builder()
                                .receiver(manager)
                                .report(report)
                                .order(order)
                                .build();
                        notification = notificationService.createOptionNotification(notification);
                        log.info("Notification with id {} of order id {} sent to user {} of role {}",
                                notification.getId(), order.getId(), manager.getId(), manager.getRole()
                        );
                });
            };
        }

        @Bean
        public Action<OrderStatus, OrderEvent> notifyCustomerAction() {
            return context -> {
                String notifyMessage = context.getExtendedState().get("message", String.class);
                //Notify logic using NotificationService
                log.info(notifyMessage);
            };
        }

        @Bean
        public Action<OrderStatus, OrderEvent> notifySaleStaffAction() {
            return context -> {
                String notifyMessage = context.getExtendedState().get("message", String.class);
                //Notify logic using NotificationService
                log.info(notifyMessage);
            };
        }

        @Bean
        public Action<OrderStatus, OrderEvent> notifyDesignStaffAction() {
            return context -> {
                String notifyMessage = context.getExtendedState().get("message", String.class);
                //Notify logic using NotificationService
                log.info(notifyMessage);
            };
        }

        @Bean
        public Action<OrderStatus, OrderEvent> notifyProductionStaffAction() {
            return context -> {
                String notifyMessage = context.getExtendedState().get("message", String.class);
                //Notify logic using NotificationService
                log.info(notifyMessage);
            };
        }

        @Bean
        public Action<OrderStatus, OrderEvent> notifyManagerAction() {
            return context -> {
                String notifyMessage = context.getExtendedState().get("message", String.class);
                //Notify logic using NotificationService
                log.info(notifyMessage);
            };
        }

        @Bean
        public Action<OrderStatus, OrderEvent> notifyCancelAction() {
            return context -> {
                Order order = context.getExtendedState().get("order", Order.class);
                StateMachine<OrderStatus, OrderEvent> stateMachine = context.getStateMachine();
                Message<OrderEvent> message;

                switch (context.getStateMachine().getState().getId()) {
                    case OrderStatus.REQ_DECLINED -> {
                        message = MessageBuilder
                                .withPayload(OrderEvent.REQ_DECLINE)
                                .build();
                    } case OrderStatus.QUO_MANA_DECLINED -> {
                        message = MessageBuilder
                                .withPayload(OrderEvent.QUO_MANA_DECLINE)
                                .build();
                    } case OrderStatus.QUO_CUST_DECLINED ->
                        message = MessageBuilder
                                .withPayload(OrderEvent.QUO_CUST_DECLINE).build();
                    case OrderStatus.DES_MANA_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.DES_MANA_DECLINE).build();
                    case OrderStatus.DES_CUST_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.DES_CUST_DECLINE).build();
                    case OrderStatus.PRO_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.PRO_DECLINE).build();
                    default ->
                        throw new RuntimeException("Unexpected state machine state " + context.getStateMachine().getState());
                }
                stateMachine.sendEvent(Mono.just(message)).subscribe();
            };
        }
        //</editor-fold>
    }
}