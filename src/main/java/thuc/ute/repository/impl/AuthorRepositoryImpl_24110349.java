package thuc.ute.repository.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import thuc.ute.config.JpaConfig_24110349;
import thuc.ute.entity.Author_24110349;
import thuc.ute.repository.IAuthorRepository_24110349;

public class AuthorRepositoryImpl_24110349 implements IAuthorRepository_24110349 {

    @Override
    public List<Author_24110349> findPage(int page, int pageSize) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT a FROM Author_24110349 a ORDER BY a.authorId ASC",
                    Author_24110349.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Author_24110349> findAll() {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT a FROM Author_24110349 a ORDER BY a.authorName ASC",
                    Author_24110349.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countAll() {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT COUNT(a) FROM Author_24110349 a", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Author_24110349 findById(int authorId) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            return entityManager.find(Author_24110349.class, authorId);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void insert(Author_24110349 author) {
        executeInTransaction(entityManager -> entityManager.persist(author));
    }

    @Override
    public void update(Author_24110349 author) {
        executeInTransaction(entityManager -> {
            Author_24110349 savedAuthor = entityManager.find(Author_24110349.class, author.getAuthorId());
            if (savedAuthor == null) {
                throw new IllegalArgumentException("Không tìm thấy tác giả.");
            }
            savedAuthor.setAuthorName(author.getAuthorName());
            savedAuthor.setDateOfBirth(author.getDateOfBirth());
        });
    }

    @Override
    public void delete(int authorId) {
        executeInTransaction(entityManager -> {
            entityManager.createNativeQuery("DELETE FROM book_author WHERE author_id = :authorId")
                    .setParameter("authorId", authorId)
                    .executeUpdate();
            Author_24110349 author = entityManager.find(Author_24110349.class, authorId);
            if (author != null) {
                entityManager.remove(author);
            }
        });
    }

    private void executeInTransaction(EntityManagerAction_24110349 action) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.execute(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    private interface EntityManagerAction_24110349 {
        void execute(EntityManager entityManager);
    }
}
