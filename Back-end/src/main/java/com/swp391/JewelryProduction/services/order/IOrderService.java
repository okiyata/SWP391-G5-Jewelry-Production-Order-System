package com.swp391.JewelryProduction.services.order;

import com.swp391.JewelryProduction.dto.OrderDTO;

import java.util.List;

public interface IOrderService {
    List<OrderDTO> findAllOrders();
}
