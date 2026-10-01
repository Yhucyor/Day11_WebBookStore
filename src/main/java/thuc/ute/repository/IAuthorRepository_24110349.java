package thuc.ute.repository;

import java.util.List;

import thuc.ute.entity.Author_24110349;

public interface IAuthorRepository_24110349 {
    List<Author_24110349> findPage(int page, int pageSize);

    List<Author_24110349> findAll();

    long countAll();

    Author_24110349 findById(int authorId);

    void insert(Author_24110349 author);

    void update(Author_24110349 author);

    void delete(int authorId);
}
