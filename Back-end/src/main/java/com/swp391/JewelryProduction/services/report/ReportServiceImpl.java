package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;
import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.ReportRepository;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.order.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;
    private AccountService accountService;
    private ReportService reportService;
    private OrderService orderService;

    private ModelMapper modelMapper;

    private StateMachineService<OrderStatus, OrderEvent> stateMachineService;
    private StateMachinePersist<OrderStatus, OrderEvent, String> stateMachinePersist;
    private StateMachine<OrderStatus, OrderEvent> currentStateMachine;

    public Report saveReport(Report report) {
        reportRepository.save(report);
        return report;
    }

    @Override
    public Report createRequest(ReportRequest request, Order order) {
        Account sender = modelMapper.map(accountService.findAccountByEmail(request.getSenderEmail()), Account.class);
        Report report = Report.builder()
                        .reportingOrder(order)
                        .title(request.getTitle())
                        .createdDate(LocalDateTime.now())
                        .type(ReportType.REQUEST)
                        .sender(sender)
                        //STATE MACHINE FOR NOTIFICATION
                        .build();

        order.getRelatedReports().add(report);
        orderService.updateOrder(order);
        return report;
    }

    @Transactional
    public void handleUserResponse(String orderId, String response) throws Exception {
        StateMachine<OrderStatus, OrderEvent> stateMachine = getStateMachine(orderId);
        OrderStatus status =  stateMachine.getState().getId();
        OrderEvent triggerEvent;
        switch (response) {
            case "confirm":
                stateMachine.getExtendedState().getVariables().put("isApproved", true);
                triggerEvent = getApprovalEvent(status);
                break;
            case "decline":
                stateMachine.getExtendedState().getVariables().put("isApproved", false);
                triggerEvent = getApprovalEvent(stateMachine.getState().getId());
                break;
            default:
                throw new IllegalArgumentException("Invalid response");
        }
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(triggerEvent).build())).subscribe();
    }

    private OrderEvent getApprovalEvent(OrderStatus approvalType) {
        return switch (approvalType) {
            case OrderStatus.REQ_AWAIT_APPROVAL -> OrderEvent.REQ_PROCESS;
            case OrderStatus.QUO_AWAIT_MANA_APPROVAL -> OrderEvent.QUO_MANA_PROCESS;
            case OrderStatus.QUO_AWAIT_CUST_APPROVAL -> OrderEvent.QUO_CUST_PROCESS;
            case OrderStatus.DES_AWAIT_MANA_APPROVAL -> OrderEvent.DES_MANA_PROCESS;
            case OrderStatus.DES_AWAIT_CUST_APPROVAL -> OrderEvent.DES_CUST_PROCESS;
            case OrderStatus.PRO_AWAIT_APPROVAL -> OrderEvent.PRO_PROCESS;
            case OrderStatus.AWAIT_TRANSACTION -> OrderEvent.TRANSACTION_MAKE;
            // Add other approval types
            default -> throw new IllegalArgumentException("Invalid approval type");
        };
    }

    private synchronized StateMachine<OrderStatus, OrderEvent> getStateMachine(String machineId) throws Exception {
//        listener.resetMessages();
        if (currentStateMachine == null) {
            currentStateMachine = stateMachineService.acquireStateMachine(machineId);
//            currentStateMachine.addStateListener(listener);
            currentStateMachine.startReactively().block();
        } else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
            stateMachineService.releaseStateMachine(currentStateMachine.getId());
            currentStateMachine.stopReactively().block();
            currentStateMachine = stateMachineService.acquireStateMachine(machineId);
//            currentStateMachine.addStateListener(listener);
            currentStateMachine.startReactively().block();
        }
        return currentStateMachine;
    }
}
