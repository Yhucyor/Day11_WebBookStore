package thuc.ute.service.impl;

import java.util.List;

import thuc.ute.dao.ICartItemDao_24110349;
import thuc.ute.dao.impl.CartItemDaoImpl_24110349;
import thuc.ute.entity.CartItem_24110349;
import thuc.ute.service.ICartItemService_24110349;

public class CartItemServiceImpl_24110349 implements ICartItemService_24110349 {

    private ICartItemDao_24110349 cartDao = new CartItemDaoImpl_24110349();

    @Override
    public void insert(CartItem_24110349 cartItem) {
        cartDao.insert(cartItem);
    }

    @Override
    public void update(CartItem_24110349 cartItem) {
        cartDao.update(cartItem);
    }

    @Override
    public void delete(int cartItemId) {
        cartDao.delete(cartItemId);
    }

    @Override
    public void deleteByUser(int userId) {
        cartDao.deleteByUser(userId);
    }

    @Override
    public CartItem_24110349 findById(int cartItemId) {
        return cartDao.findById(cartItemId);
    }

    @Override
    public CartItem_24110349 findByUserAndBook(int userId, int bookId) {
        return cartDao.findByUserAndBook(userId, bookId);
    }

    @Override
    public List<CartItem_24110349> findByUser(int userId) {
        return cartDao.findByUser(userId);
    }
}
