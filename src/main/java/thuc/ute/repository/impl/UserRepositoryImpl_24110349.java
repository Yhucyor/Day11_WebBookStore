package thuc.ute.repository.impl;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import thuc.ute.config.JpaConfig_24110349;
import thuc.ute.entity.User_24110349;
import thuc.ute.repository.IUserRepository_24110349;

public class UserRepositoryImpl_24110349 implements IUserRepository_24110349 {

    @Override
    public List<User_24110349> findAll() {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<User_24110349> query = enma.createQuery("SELECT u FROM User_24110349 u", User_24110349.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public User_24110349 findById(int id) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            return enma.find(User_24110349.class, id);
        } finally {
            enma.close();
        }
    }

    @Override
    public void insert(User_24110349 user) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user);
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
    public User_24110349 findByUserName(String username) {
        // Ánh xạ username sang cột fullname
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<User_24110349> query = enma.createQuery("SELECT u FROM User_24110349 u WHERE u.fullname = :username", User_24110349.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<Long> query = enma.createQuery("SELECT COUNT(u) FROM User_24110349 u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistUserName(String username) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<Long> query = enma.createQuery("SELECT COUNT(u) FROM User_24110349 u WHERE u.fullname = :username", Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistPhone(String phone) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<Long> query = enma.createQuery("SELECT COUNT(u) FROM User_24110349 u WHERE CAST(u.phone AS string) = :phone", Long.class);
            query.setParameter("phone", phone);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            enma.close();
        }
    }

    @Override
    public User_24110349 findByEmail(String email) {
        EntityManager enma = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<User_24110349> query = enma.createQuery("SELECT u FROM User_24110349 u WHERE u.email = :email", User_24110349.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            enma.close();
        }
    }
}
