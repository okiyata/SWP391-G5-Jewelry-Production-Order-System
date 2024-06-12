package com.swp391.JewelryProduction.services.notification;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Notification;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
//
//    private final ModelMapper modelMapper;
//    private final Flux<ServerSentEvent<Notification>> notificationFlux = Flux.push(this::generateNotifications);
//
//    private final String EVENT_NAME = "notify";
//
//    private ServerSentEvent<Notification> generateNotification(AccountDTO account, Report report, Order order) {
//        Notification newNotification = Notification.builder()
//                .account(modelMapper.map(account, Account.class))
//                .report(report)
//                .order(order)
//                .build();
//
//        return ServerSentEvent.<Notification>builder()
//                .event(EVENT_NAME)
//                .data(newNotification)
//                .build();
//    }
//
//    private void generateNotifications(FluxSink<ServerSentEvent<Notification>> sink) {
//        Flux.interval(Duration.ofSeconds(2))
//                .map(i -> generateNotification())
//                .doOnNext(serverSentEvent -> {
//                    sink.next(serverSentEvent); // Sending notifications to the global Flux via its FluxSink
//                    log.info("Sent for {}", serverSentEvent.data().getId());
//                })
//                .doFinally(signalType -> log.info("Notification flux closed")) // Logging the closure of our generator
//                .takeWhile(notification -> !sink.isCancelled()) // We generate messages until the global Flux is closed
//                .subscribe();
//    }
//
//    private <T> Flux<ServerSentEvent<Notification>> keepAlive(Duration duration, Flux<T> data, String id) {
//        Flux<ServerSentEvent<T>> heartBeat = Flux.interval(duration)
//                .map(e -> ServerSentEvent.<T>builder()
//                    .comment("keep alive for: " + id)
//                    .build()
//                )
//                .doFinally(signalType -> log.info("Heartbeat closed for id: {}", id));
//        return Flux.<ServerSentEvent<Notification>>merge(heartBeat, data);
//    }
//
//    public Flux<ServerSentEvent<Notification>> subscribe(String id) {
//        return keepAlive(Duration.ofSeconds(3),
//                notificationFlux.filter(notification ->
//                        notification.data() == null ||
//                        notification.data().getAccount().getId().equals(id)),
//                id);
//    }
    @Override
    public void saveNotification(Notification notification) {
    notificationRepository.save(notification);
}

    @Override
    public List<Notification> findAllByReceiver_Id(String receiverId) {
        return notificationRepository.findAllByReceiver_Id(receiverId);
    }

}
