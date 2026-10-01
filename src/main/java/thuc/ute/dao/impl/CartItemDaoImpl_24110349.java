package thuc.ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import thuc.ute.config.JpaConfig_24110349;
import thuc.ute.dao.ICartItemDao_24110349;
import thuc.ute.entity.CartItem_24110349;

public class CartItemDaoImpl_24110349 implements ICartItemDao_24110349 {

    @Override
    public void insert(CartItem_24110349 cartItem) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            // Gắn lại các đối tượng đã bị detached (vì lấy từ session hoặc request khác)
            if (cartItem.getUser() != null) {
                cartItem.setUser(enma.getReference(thuc.ute.entity.User_24110349.class, cartItem.getUser().getId()));
            }
            if (cartItem.getBook() != null) {
                cartItem.setBook(enma.getReference(thuc.ute.entity.Book_24110349.class, cartItem.getBook().getBookId()));
            }
            enma.persist(cartItem);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(CartItem_24110349 cartItem) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(cartItem);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(int cartItemId) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            CartItem_24110349 item = enma.find(CartItem_24110349.class, cartItemId);
            if (item != null) {
                enma.remove(item);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void deleteByUser(int userId) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.createQuery("DELETE FROM CartItem_24110349 c WHERE c.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public CartItem_24110349 findById(int cartItemId) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        CartItem_24110349 item = enma.find(CartItem_24110349.class, cartItemId);
        enma.close();
        return item;
    }

    @Override
    public CartItem_24110349 findByUserAndBook(int userId, int bookId) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<CartItem_24110349> query = enma.createQuery(
                "SELECT c FROM CartItem_24110349 c WHERE c.user.id = :userId AND c.book.bookId = :bookId", CartItem_24110349.class);
            query.setParameter("userId", userId);
            query.setParameter("bookId", bookId);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<CartItem_24110349> findByUser(int userId) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<CartItem_24110349> query = enma.createQuery(
                "SELECT c FROM CartItem_24110349 c JOIN FETCH c.book WHERE c.user.id = :userId", CartItem_24110349.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }
}
