package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.OrderDTO;
import com.anubhav.ecom.exceptions.ForbiddenException;
import com.anubhav.ecom.exceptions.ResourceNotFoundException;
import com.anubhav.ecom.models.Order;
import com.anubhav.ecom.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService
{
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDTO> getOrders()
    {
        var user = userService.getCurrentUser();
        List<Order> orders = orderRepository.getAllByCustomer(user);
        return orders
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @Override
    public OrderDTO getOrderById(Long orderId)
    {
        Order order = orderRepository.getOrderWithItems(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found : " + orderId));
        var user = userService.getCurrentUser();

        if (!order.isPlacedBy(user))
        {
            throw new ForbiddenException("You are not authorized to access this order");
        }
        return modelMapper.map(order, OrderDTO.class);
    }
}
