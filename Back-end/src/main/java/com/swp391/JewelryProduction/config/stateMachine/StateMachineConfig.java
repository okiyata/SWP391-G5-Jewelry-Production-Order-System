package com.swp391.JewelryProduction.config.stateMachine;

import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.*;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.services.order.OrderService;
import com.swp391.JewelryProduction.services.report.ReportService;
import com.swp391.JewelryProduction.util.MessagesConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    @Autowired
    private OrderService orderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ModelMapper modelMapper;

    private final MessagesConstant messagesConstant = new MessagesConstant();

    private StateRepository<? extends RepositoryState> stateRepository;
    private TransitionRepository<? extends RepositoryTransition> transitionRepository;
    private StateMachineRuntimePersister<OrderStatus, OrderEvent, String> stateMachineRuntimePersister;

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
                .end(OrderStatus.ORDER_COMPLETED)                                           //End state for finalize the order

                /*-----------REQUEST SUPERSTATE-----------*/
                .and()
                .withStates()
                .parent(OrderStatus.REQUEST)
                .initial(OrderStatus.REQUESTING)
                .state(OrderStatus.REQ_AWAIT_APPROVAL)
                .choice(OrderStatus.REQ_APPROVAL_PROCESS)
                .state(OrderStatus.REQ_APPROVED, approvedAction(), null)
                .state(OrderStatus.REQ_DECLINED, declinedAction(), null)
                .state(OrderStatus.AWAIT_ASSIGN_STAFF)
                .state(OrderStatus.IN_EXCHANGING, createChatRoomAction())

                .and()
                .withStates()
                .parent(OrderStatus.QUOTATION)
                .initial(OrderStatus.AWAIT_QUO)
                .state(OrderStatus.QUO_AWAIT_MANA_APPROVAL)
                .choice(OrderStatus.QUO_MANA_APPROVAL_PROCESS)
                .state(OrderStatus.QUO_MANA_APPROVED, approvedAction(), null)
                .state(OrderStatus.QUO_MANA_DECLINED, declinedAction(), null)
                .state(OrderStatus.QUO_AWAIT_CUST_APPROVAL)
                .choice(OrderStatus.QUO_CUST_APPROVAL_PROCESS)
                .state(OrderStatus.QUO_CUST_APPROVED, approvedAction(), null)
                .state(OrderStatus.QUO_CUST_DECLINED, declinedAction(), null)
                .state(OrderStatus.AWAIT_TRANSACTION)
                .choice(OrderStatus.TRANSACTION_PROCESS)
                .state(OrderStatus.TRANSACTION_SUCCESSFUL, approvedAction(), notifyTransactionReceiptAction())
                .state(OrderStatus.TRANSACTION_UNSUCCESSFUL, declinedAction(), null)

                .and()
                .withStates()
                .parent(OrderStatus.DESIGN)
                .initial(OrderStatus.IN_DESIGNING)
                .state(OrderStatus.DES_AWAIT_MANA_APPROVAL)
                .choice(OrderStatus.DES_MANA_APPROVAL_PROCESS)
                .state(OrderStatus.DES_MANA_APPROVED, approvedAction(), null)
                .state(OrderStatus.DES_MANA_DECLINED, declinedAction(), null)
                .state(OrderStatus.DES_AWAIT_CUST_APPROVAL)
                .choice(OrderStatus.DES_CUST_APPROVAL_PROCESS)
                .state(OrderStatus.DES_CUST_APPROVED, approvedAction(), null)
                .state(OrderStatus.DES_CUST_DECLINED, declinedAction(), null)

                .and()
                .withStates()
                .parent(OrderStatus.PRODUCTION)
                .initial(OrderStatus.IN_PRODUCTION)
                .state(OrderStatus.PRO_AWAIT_APPROVAL)
                .choice(OrderStatus.PRO_APPROVAL_PROCESS)
                .state(OrderStatus.PRO_APPROVED, approvedAction(), null)
                .state(OrderStatus.PRO_DECLINED, declinedAction(), null)
                .and()
                .withStates()
                .parent(OrderStatus.TRANSPORT)
                .state(OrderStatus.ON_DELIVERING)
                .state(OrderStatus.DELIVERED_AWAIT_APPROVAL)
                .choice(OrderStatus.DELIVERED_APPROVAL_PROCESS)
                .state(OrderStatus.DELIVERED_CONFIRMED, approvedAction(), null)
                .state(OrderStatus.DELIVERED_DENIED, declinedAction(), null)
        ;

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStatus.CANCEL).target(OrderStatus.ORDER_RESTORED)
                .event(OrderEvent.RESTORE_ORDER)
                .action(notifyRestoreOrderAction())
                .and()
                .withHistory()
                .source(OrderStatus.ORDER_RESTORED).target(OrderStatus.REQUESTING)  //Placeholder target

                /*------------------------------------------------------------------------------------------------*/
                /*--------------------------------REQUEST SUPERSTATE LOCAL TRANSITION--------------------------------*/

                .and()
                .withLocal()
                .source(OrderStatus.REQUESTING).target(OrderStatus.REQ_AWAIT_APPROVAL)
                .event(OrderEvent.REQ_RECEIVED)
                .action(notifyManagerApprovalAction())
                .and()
                .withLocal()
                .source(OrderStatus.REQ_AWAIT_APPROVAL).target(OrderStatus.REQ_APPROVAL_PROCESS)
                .event(OrderEvent.REQ_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.REQ_APPROVAL_PROCESS)
                .first(OrderStatus.REQ_APPROVED, checkApprovalGuard())
                .last(OrderStatus.REQ_DECLINED)
                .and()
                .withExternal()
                .source(OrderStatus.REQ_DECLINED).target(OrderStatus.CANCEL)
                .event(OrderEvent.REQ_DECLINE)
                .action(notifyRequestDeclinedAction())
                .and()
                .withLocal()
                .source(OrderStatus.REQ_APPROVED).target(OrderStatus.AWAIT_ASSIGN_STAFF)
                .event(OrderEvent.REQ_APPROVE)
                .action(notifyRequestApprovedAction())
                .and()
                .withLocal()
                .source(OrderStatus.AWAIT_ASSIGN_STAFF).target(OrderStatus.IN_EXCHANGING)
                .event(OrderEvent.ASSIGN_STAFF)

                /*------------------------------------------------------------------------------------------------*/
                /*------------------------------------------------------------------------------------------------*/

                .and()
                .withExternal()
                .source(OrderStatus.IN_EXCHANGING).target(OrderStatus.AWAIT_QUO)
                .event(OrderEvent.FINALIZE_IDEA)

                /*-----------------------------------------------------------------------------------------------------*/
                /*--------------------------------QUOTATION SUPERSTATE LOCAL TRANSITION--------------------------------*/

                .and()
                .withLocal()
                .source(OrderStatus.AWAIT_QUO).target(OrderStatus.QUO_AWAIT_MANA_APPROVAL)
                .event(OrderEvent.QUO_FINISH)
                .action(notifyManagerApprovalAction())
                .and()
                .withLocal()
                .source(OrderStatus.QUO_AWAIT_MANA_APPROVAL).target(OrderStatus.QUO_MANA_APPROVAL_PROCESS)
                .event(OrderEvent.QUO_MANA_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.QUO_MANA_APPROVAL_PROCESS)
                .first(OrderStatus.QUO_MANA_APPROVED, checkApprovalGuard())
                .last(OrderStatus.QUO_MANA_DECLINED)
                .and()
                .withLocal()
                .source(OrderStatus.QUO_MANA_APPROVED).target(OrderStatus.QUO_AWAIT_CUST_APPROVAL)
                .event(OrderEvent.QUO_MANA_APPROVE)
                .action(notifyCustomerApprovalAction())
                .and()
                .withLocal()
                .source(OrderStatus.QUO_MANA_DECLINED).target(OrderStatus.AWAIT_QUO)
                .event(OrderEvent.QUO_MANA_DECLINE)
                .and()
                .withLocal()
                .source(OrderStatus.QUO_AWAIT_CUST_APPROVAL).target(OrderStatus.QUO_CUST_APPROVAL_PROCESS)
                .event(OrderEvent.QUO_CUST_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.QUO_CUST_APPROVAL_PROCESS)
                .first(OrderStatus.QUO_CUST_APPROVED, checkApprovalGuard())
                .last(OrderStatus.QUO_CUST_DECLINED)
                .and()
                .withLocal()
                .source(OrderStatus.QUO_CUST_APPROVED).target(OrderStatus.AWAIT_TRANSACTION)
                .event(OrderEvent.QUO_CUST_APPROVE)
                .action(notifyCustomerTransactionAction())
                .and()
                .withLocal()
                .source(OrderStatus.QUO_CUST_DECLINED).target(OrderStatus.AWAIT_QUO)
                .event(OrderEvent.QUO_CUST_DECLINE)
                .and()
                .withLocal()
                .source(OrderStatus.AWAIT_TRANSACTION).target(OrderStatus.TRANSACTION_PROCESS)
                .event(OrderEvent.TRANSACTION_MAKE)
                .and()
                .withChoice()
                .source(OrderStatus.TRANSACTION_PROCESS)
                .first(OrderStatus.TRANSACTION_SUCCESSFUL, checkTransactionGuard())
                .last(OrderStatus.TRANSACTION_UNSUCCESSFUL)
                .and()
                .withLocal()
                .source(OrderStatus.TRANSACTION_UNSUCCESSFUL).target(OrderStatus.AWAIT_TRANSACTION)
                .event(OrderEvent.TRANSACTION_DECLINE)

                /*------------------------------------------------------------------------------------------------*/
                /*------------------------------------------------------------------------------------------------*/

                .and()
                .withExternal()
                .source(OrderStatus.TRANSACTION_SUCCESSFUL).target(OrderStatus.IN_DESIGNING)
                .event(OrderEvent.TRANSACTION_APPROVE)
                .action(notifyDesignStaffAction())

                /*--------------------------------------------------------------------------------------------------*/
                /*--------------------------------DESIGN SUPERSTATE LOCAL TRANSITION--------------------------------*/

                .and()
                .withLocal()
                .source(OrderStatus.IN_DESIGNING).target(OrderStatus.DES_AWAIT_MANA_APPROVAL)
                .event(OrderEvent.QUO_FINISH)
                .action(notifyManagerApprovalAction())
                .and()
                .withLocal()
                .source(OrderStatus.DES_AWAIT_MANA_APPROVAL).target(OrderStatus.DES_MANA_APPROVAL_PROCESS)
                .event(OrderEvent.DES_MANA_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.DES_MANA_APPROVAL_PROCESS)
                .first(OrderStatus.DES_MANA_APPROVED, checkApprovalGuard())
                .last(OrderStatus.DES_MANA_DECLINED)
                .and()
                .withLocal()
                .source(OrderStatus.DES_MANA_APPROVED).target(OrderStatus.DES_AWAIT_CUST_APPROVAL)
                .event(OrderEvent.DES_MANA_APPROVE)
                .action(notifyCustomerApprovalAction())
                .and()
                .withLocal()
                .source(OrderStatus.DES_MANA_DECLINED).target(OrderStatus.IN_DESIGNING)
                .event(OrderEvent.DES_MANA_DECLINE)
                .action(deleteImageAction())
                .and()
                .withLocal()
                .source(OrderStatus.DES_AWAIT_CUST_APPROVAL).target(OrderStatus.DES_CUST_APPROVAL_PROCESS)
                .event(OrderEvent.DES_CUST_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.DES_CUST_APPROVAL_PROCESS)
                .first(OrderStatus.DES_CUST_APPROVED, checkApprovalGuard())
                .last(OrderStatus.DES_CUST_DECLINED)
                .and()
                .withLocal()
                .source(OrderStatus.DES_CUST_DECLINED).target(OrderStatus.IN_DESIGNING)
                .event(OrderEvent.DES_CUST_DECLINE)

                /*------------------------------------------------------------------------------------------------*/
                /*------------------------------------------------------------------------------------------------*/

                .and()
                .withExternal()
                .source(OrderStatus.DES_CUST_APPROVED).target(OrderStatus.IN_PRODUCTION)
                .event(OrderEvent.DES_CUST_APPROVE)
                .action(notifyProductionStaffAction())

                /*------------------------------------------------------------------------------------------------------*/
                /*--------------------------------PRODUCTION SUPERSTATE LOCAL TRANSITION--------------------------------*/

                .and()
                .withLocal()
                .source(OrderStatus.IN_PRODUCTION).target(OrderStatus.PRO_AWAIT_APPROVAL)
                .event(OrderEvent.PRO_FINISH)
                .action(notifyCustomerApprovalAction())
                .and()
                .withLocal()
                .source(OrderStatus.PRO_AWAIT_APPROVAL).target(OrderStatus.PRO_APPROVAL_PROCESS)
                .event(OrderEvent.PRO_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.PRO_APPROVAL_PROCESS)
                .first(OrderStatus.PRO_APPROVED, checkApprovalGuard())
                .last(OrderStatus.PRO_DECLINED)
                .and()
                .withLocal()
                .source(OrderStatus.PRO_DECLINED).target(OrderStatus.IN_PRODUCTION)
                .event(OrderEvent.PRO_DECLINE)

                /*------------------------------------------------------------------------------------------------*/
                /*------------------------------------------------------------------------------------------------*/

                .and()
                .withExternal()
                .source(OrderStatus.PRO_APPROVED).target(OrderStatus.ON_DELIVERING)
                .event(OrderEvent.PRO_APPROVE)

                /*-----------------------------------------------------------------------------------------------------*/
                /*--------------------------------TRANSPORT SUPERSTATE LOCAL TRANSITION--------------------------------*/

                .and()
                .withLocal()
                .source(OrderStatus.ON_DELIVERING).target(OrderStatus.DELIVERED_AWAIT_APPROVAL)
                .event(OrderEvent.DELIVERED)
                .action(notifyDeliveredAction())
                .and()
                .withLocal()
                .source(OrderStatus.DELIVERED_AWAIT_APPROVAL).target(OrderStatus.DELIVERED_APPROVAL_PROCESS)
                .event(OrderEvent.DELIVERED_PROCESS)
                .and()
                .withChoice()
                .source(OrderStatus.DELIVERED_APPROVAL_PROCESS)
                .first(OrderStatus.DELIVERED_CONFIRMED, checkApprovalGuard())
                .last(OrderStatus.DELIVERED_DENIED)
                .and()
                .withLocal()
                .source(OrderStatus.DELIVERED_DENIED).target(OrderStatus.ON_DELIVERING)
                .event(OrderEvent.DELIVERED_DENY)

                /*------------------------------------------------------------------------------------------------*/
                /*------------------------------------------------------------------------------------------------*/
                .and()
                .withExternal()
                .source(OrderStatus.DELIVERED_CONFIRMED).target(OrderStatus.ORDER_COMPLETED)
                .event(OrderEvent.DELIVERED_APPROVE)
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
    public Guard<OrderStatus, OrderEvent> checkApprovalGuard() {
        return context -> {
            Boolean approved = context.getExtendedState().get("isApproved", Boolean.class);
            return approved != null && approved;
        };
    }

    @Bean
    public Guard<OrderStatus, OrderEvent> checkTransactionGuard () {
        return context -> {
            Boolean successful = context.getExtendedState().get("isSuccessful", Boolean.class);
            return successful != null && successful;
        };
    }
    //</editor-fold>

    //<editor-fold desc="ACTION BEAN" defaultstate="collapsed">
    @Bean
    public Action<OrderStatus, OrderEvent> notifyManagerApprovalAction() {
        return context -> {
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));
            Report report = reportService.findReportByID(context.getExtendedState().get("reportID", Integer.class));
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
    public Action<OrderStatus, OrderEvent> notifyCustomerApprovalAction() {
        return context -> {
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));
            Report report = reportService.findReportByID(context.getExtendedState().get("reportID", Integer.class));
            Account owner = order.getOwner();

            Notification notification = Notification.builder()
                    .receiver(owner)
                    .report(report)
                    .order(order)
                    .build();
            notification = notificationService.createOptionNotification(notification);
            log.info("Notification with id {} of order id {} sent to customer with id {} and email {}",
                    notification.getId(), order.getId(), owner.getId(), owner.getEmail()
            );
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
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));
            Staff designStaff = order.getDesignStaff();

            String title = String.format("Work assignment for order %s", order.getId());
            String description = String.format("You have been assigned to the order %s", order.getId());

            Report report = reportService.createNormalReport(order, title, description);

            Notification notification = Notification.builder()
                    .receiver(designStaff)
                    .report(report)
                    .order(order)
                    .build();
            notification = notificationService.createOptionNotification(notification);
            log.info("Notification with id {} of order id {} sent to design staff of id {} with email {}",
                    notification.getId(), order.getId(), designStaff.getId(), designStaff.getEmail()
            );
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
    public Action<OrderStatus, OrderEvent> approvedAction () {
        return context -> {
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));
            StateMachine<OrderStatus, OrderEvent> stateMachine = context.getStateMachine();
            Message<OrderEvent> message;

            switch (context.getStateMachine().getState().getId()) {
                case OrderStatus.REQ_APPROVED ->
                        message = MessageBuilder.withPayload(OrderEvent.REQ_APPROVE)
                                .setHeader("message", messagesConstant.createRequestApprovedMessage(order.getOwner().getUserInfo().getFirstName()))
                                .build();
                case OrderStatus.QUO_MANA_APPROVED ->
                        message = MessageBuilder.withPayload(OrderEvent.QUO_MANA_APPROVE).build();
                case OrderStatus.QUO_CUST_APPROVED ->
                        message = MessageBuilder.withPayload(OrderEvent.QUO_CUST_DECLINE).build();
                case OrderStatus.TRANSACTION_SUCCESSFUL ->
                        message = MessageBuilder.withPayload(OrderEvent.TRANSACTION_APPROVE).build();
                case OrderStatus.DES_MANA_APPROVED ->
                        message = MessageBuilder.withPayload(OrderEvent.DES_MANA_APPROVE).build();
                case OrderStatus.DES_CUST_APPROVED ->
                        message = MessageBuilder.withPayload(OrderEvent.DES_CUST_APPROVE).build();
                case OrderStatus.PRO_APPROVED ->
                        message = MessageBuilder.withPayload(OrderEvent.PRO_APPROVE).build();
                case OrderStatus.DELIVERED_CONFIRMED ->
                        message = MessageBuilder.withPayload(OrderEvent.DELIVERED_APPROVE).build();
                default ->
                        throw new RuntimeException("Unexpected state machine state " + context.getStateMachine().getState());
            }
            stateMachine.sendEvent(Mono.just(message)).subscribe();
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> declinedAction() {
        return context -> {
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));
            StateMachine<OrderStatus, OrderEvent> stateMachine = context.getStateMachine();
            Message<OrderEvent> message;

            switch (context.getStateMachine().getState().getId()) {
                case OrderStatus.REQ_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.REQ_DECLINE)
                                .setHeader("message", messagesConstant.createRequestDeclinedMessage(order.getOwner().getUserInfo().getFirstName()))
                                .build();
                case OrderStatus.QUO_MANA_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.QUO_MANA_DECLINE).build();
                case OrderStatus.QUO_CUST_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.QUO_CUST_DECLINE).build();
                case OrderStatus.TRANSACTION_UNSUCCESSFUL ->
                        message = MessageBuilder.withPayload(OrderEvent.TRANSACTION_DECLINE).build();
                case OrderStatus.DES_MANA_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.DES_MANA_DECLINE).build();
                case OrderStatus.DES_CUST_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.DES_CUST_DECLINE).build();
                case OrderStatus.PRO_DECLINED ->
                        message = MessageBuilder.withPayload(OrderEvent.PRO_DECLINE).build();
                case OrderStatus.DELIVERED_DENIED ->
                        message = MessageBuilder.withPayload(OrderEvent.DELIVERED_DENY).build();
                default ->
                        throw new RuntimeException("Unexpected state machine state " + context.getStateMachine().getState());
            }
            stateMachine.sendEvent(Mono.just(message)).subscribe();
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyCancelAction() {
        return context -> {
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));

            String title = String.format("Your order %s have been cancelled", order.getId());
            String description = String.format("We are sorry to announce that your order %s have been cancelled", order.getId());

            Report report = reportService.createNormalReport(order, title, description);

            Notification notification = notificationService.saveNotification(
                    Notification.builder()
                            .report(report)
                            .order(order)
                            .receiver(order.getOwner())
                            .build()
            );
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyCustomerTransactionAction() {
        return context -> {

        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> deleteImageAction () {
        return context -> {
            Order order = orderService.findOrderById(context.getExtendedState().get("orderID", String.class));
            Design design = order.getDesign();
            design.setDesignLink(null);
            orderService.updateOrder(order);
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyDeliveredAction () {
        return context -> {

        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyTransactionReceiptAction() {
        return context -> {

        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyRestoreOrderAction() {
        return context -> {

        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyRequestApprovedAction() {
        return context -> {

        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> notifyRequestDeclinedAction () {
        return context -> {

        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> createChatRoomAction () {
        return context -> {

        };
    }
    //</editor-fold>
}
