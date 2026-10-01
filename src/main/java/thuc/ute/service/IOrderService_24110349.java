package thuc.ute.service;

import thuc.ute.entity.Order_24110349;
import java.util.List;

public interface IOrderService_24110349 {
    void placeOrder(Order_24110349 order);
    List<Order_24110349> findOrdersByUser(int userId);
    List<Order_24110349> findOrdersByUserAndStatus(int userId, String status);
}
