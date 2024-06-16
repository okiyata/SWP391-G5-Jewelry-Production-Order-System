package com.swp391.JewelryProduction.services.order;

import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.repositories.OrderRepository;
import com.swp391.JewelryProduction.services.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll().stream().toList();
    }

    @Override
    public Order saveNewOrder(String accountId) {
        Order order = Order.builder()
                .status(OrderStatus.REQUESTING)
                .owner(modelMapper.map(accountService.findAccountById(accountId), Account.class))
                .build();
        order = orderRepository.save(order);

        return order;
    }

    @Override
    public Order findOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}