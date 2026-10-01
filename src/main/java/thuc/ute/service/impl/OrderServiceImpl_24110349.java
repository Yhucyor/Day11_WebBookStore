package thuc.ute.service.impl;

import thuc.ute.config.JpaConfig_24110349;
import thuc.ute.entity.Book_24110349;
import thuc.ute.entity.OrderDetail_24110349;
import thuc.ute.entity.Order_24110349;
import thuc.ute.service.IOrderService_24110349;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class OrderServiceImpl_24110349 implements IOrderService_24110349 {

    @Override
    public void placeOrder(Order_24110349 order) {
        EntityManager em = JpaConfig_24110349.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(order);
            
            // Trừ số lượng tồn kho của sách
            for (OrderDetail_24110349 detail : order.getOrderDetails()) {
                Book_24110349 managedBook = em.find(Book_24110349.class, detail.getBook().getBookId());
                if (managedBook != null) {
                    int currentQty = managedBook.getQuantity() != null ? managedBook.getQuantity() : 0;
                    int newQty = currentQty - detail.getQuantity();
                    managedBook.setQuantity(newQty < 0 ? 0 : newQty);
                    em.merge(managedBook);
                }
            }
            
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Order_24110349> findOrdersByUser(int userId) {
        EntityManager em = JpaConfig_24110349.getEntityManager();
        try {
            String jpql = "SELECT o FROM Order_24110349 o " +
                          "LEFT JOIN FETCH o.orderDetails d " +
                          "LEFT JOIN FETCH d.book " +
                          "WHERE o.user.id = :userId ORDER BY o.orderDate DESC";
            List<Order_24110349> results = em.createQuery(jpql, Order_24110349.class)
                     .setParameter("userId", userId)
                     .getResultList();
            return new java.util.ArrayList<>(new java.util.LinkedHashSet<>(results));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Order_24110349> findOrdersByUserAndStatus(int userId, String status) {
        if (status == null || status.trim().isEmpty() || "T\u1EA5t c\u1EA3".equalsIgnoreCase(status)) {
            return findOrdersByUser(userId);
        }
        EntityManager em = JpaConfig_24110349.getEntityManager();
        try {
            String jpql = "SELECT o FROM Order_24110349 o " +
                          "LEFT JOIN FETCH o.orderDetails d " +
                          "LEFT JOIN FETCH d.book " +
                          "WHERE o.user.id = :userId AND o.status = :status ORDER BY o.orderDate DESC";
            List<Order_24110349> results = em.createQuery(jpql, Order_24110349.class)
                     .setParameter("userId", userId)
                     .setParameter("status", status)
                     .getResultList();
            return new java.util.ArrayList<>(new java.util.LinkedHashSet<>(results));
        } finally {
            em.close();
        }
    }
}
