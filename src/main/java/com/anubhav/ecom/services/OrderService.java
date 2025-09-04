package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.OrderDTO;

import java.util.List;

public interface OrderService
{
    List<OrderDTO>  getOrders();
    OrderDTO getOrderById(Long orderId);
}
