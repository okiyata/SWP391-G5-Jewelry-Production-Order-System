package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.config.stateMachine.StateMachineInterceptor;
import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.ReportRepository;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.order.OrderService;
import com.swp391.JewelryProduction.util.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.support.MessageBuilder;
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
        Account sender = modelMapper.map(accountService.findAccountById(request.getSenderId()), Account.class);
        Report report = Report.builder()
                        .reportingOrder(order)
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .createdDate(LocalDateTime.now())
                        .type(ReportType.REQUEST)
                        .sender(sender)
                        .build();
        order.getRelatedReports().add(report);
        orderService.updateOrder(order);

        StateMachine<OrderStatus, OrderEvent> stateMachine = instantiateStateMachine(order);

        stateMachine.sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvent.REQ_RECEIVED).build())
        ).subscribe();
        return report;
    }

    @Override
    public Report createQuotationReport(ReportRequest report, Order order) {
        return null;
    }

    @Override
    public Report createDesignReport(ReportRequest report, Order order) {
        return null;
    }

    @Override
    @Transactional
    public void handleUserResponse(String orderId, String response) throws RuntimeException {
        StateMachine<OrderStatus, OrderEvent> stateMachine = getStateMachine(orderId);
        OrderStatus status =  stateMachine.getState().getId();
        OrderEvent triggerEvent = switch (response) {
            case "approve" -> {
                stateMachine.getExtendedState().getVariables().put("isApproved", true);
                yield getApprovalEvent(status);
            }
            case "decline" -> {
                stateMachine.getExtendedState().getVariables().put("isApproved", false);
                yield getApprovalEvent(stateMachine.getState().getId());
            }
            default -> throw new IllegalArgumentException("Invalid response");
        };
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(triggerEvent).build())).subscribe();
    }

    @Override
    public Report createNormalReport(Order order, String title, String content) {
        Report newReport = Report.builder()
                .title(title)
                .description(content)
                .sender(null)
                .createdDate(LocalDateTime.now())
                .reportingOrder(order)
                .type(ReportType.NONE)
                .build();
        order.getRelatedReports().add(newReport);
        orderService.updateOrder(order);

        return newReport;
    }

    @Override
    public Report findReportByID(Integer id) {
        return reportRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Order of id " + id + " cannot be found"));
    }

    private OrderEvent getApprovalEvent(OrderStatus approvalType) {
        return switch (approvalType) {
            case OrderStatus.REQ_AWAIT_APPROVAL         ->  OrderEvent.REQ_PROCESS;
            case OrderStatus.QUO_AWAIT_MANA_APPROVAL    ->  OrderEvent.QUO_MANA_PROCESS;
            case OrderStatus.QUO_AWAIT_CUST_APPROVAL    ->  OrderEvent.QUO_CUST_PROCESS;
            case OrderStatus.DES_AWAIT_MANA_APPROVAL    ->  OrderEvent.DES_MANA_PROCESS;
            case OrderStatus.DES_AWAIT_CUST_APPROVAL    ->  OrderEvent.DES_CUST_PROCESS;
            case OrderStatus.PRO_AWAIT_APPROVAL         ->  OrderEvent.PRO_PROCESS;
            case OrderStatus.DELIVERED_AWAIT_APPROVAL   ->  OrderEvent.DELIVERED_PROCESS;
            default -> throw new IllegalArgumentException("Invalid approval type");
        };
    }

    private synchronized StateMachine<OrderStatus, OrderEvent> getStateMachine(String machineId) throws RuntimeException {
        if (currentStateMachine == null) {
            currentStateMachine = stateMachineService.acquireStateMachine(machineId);
            currentStateMachine.startReactively().block();
        } else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
            stateMachineService.releaseStateMachine(currentStateMachine.getId());
            currentStateMachine.stopReactively().block();
            currentStateMachine = stateMachineService.acquireStateMachine(machineId);
            currentStateMachine.startReactively().block();
        }
        return currentStateMachine;
    }

    private StateMachine<OrderStatus, OrderEvent> instantiateStateMachine (Order order) {
        String orderId = order.getId();
        StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineService.acquireStateMachine(order.getId(), true);
        stateMachine.getExtendedState().getVariables().put("orderID", orderId);
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(
                        region -> region.addStateMachineInterceptor(new StateMachineInterceptor()
                        )
                );
        stateMachine.startReactively().block();
        return stateMachine;
    }
}
