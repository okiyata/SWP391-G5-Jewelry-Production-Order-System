package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.enums.OrderEvent;
import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.ReportRepository;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

    public Report createReport(ReportRequest request, Order order) {
        Account sender = modelMapper.map(accountService
                .findAccountByEmail(request.getSenderEmail()), Account.class);
        Report report = Report.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .createdDate(LocalDateTime.now())
                .sender(sender)
                .build();
        return report;
    }

//    private Report createRequestReport(ReportRequest request, Order order) {
//
//    }

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
