package thuc.ute.dao;

import java.util.List;
import thuc.ute.entity.CartItem_24110349;

public interface ICartItemDao_24110349 {
    void insert(CartItem_24110349 cartItem);
    void update(CartItem_24110349 cartItem);
    void delete(int cartItemId);
    void deleteByUser(int userId);
    CartItem_24110349 findById(int cartItemId);
    CartItem_24110349 findByUserAndBook(int userId, int bookId);
    List<CartItem_24110349> findByUser(int userId);
}
