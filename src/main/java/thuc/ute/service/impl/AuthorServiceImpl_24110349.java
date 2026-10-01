package thuc.ute.service.impl;

import java.util.List;

import thuc.ute.entity.Author_24110349;
import thuc.ute.repository.IAuthorRepository_24110349;
import thuc.ute.repository.impl.AuthorRepositoryImpl_24110349;
import thuc.ute.service.IAuthorService_24110349;

public class AuthorServiceImpl_24110349 implements IAuthorService_24110349 {
    private final IAuthorRepository_24110349 authorRepository = new AuthorRepositoryImpl_24110349();

    @Override
    public List<Author_24110349> findPage(int page, int pageSize) {
        return authorRepository.findPage(page, pageSize);
    }

    @Override
    public List<Author_24110349> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public long countAll() {
        return authorRepository.countAll();
    }

    @Override
    public Author_24110349 findById(int authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public void insert(Author_24110349 author) {
        validate(author);
        authorRepository.insert(author);
    }

    @Override
    public void update(Author_24110349 author) {
        validate(author);
        authorRepository.update(author);
    }

    @Override
    public void delete(int authorId) {
        authorRepository.delete(authorId);
    }

    private void validate(Author_24110349 author) {
        if (author == null || author.getAuthorName() == null || author.getAuthorName().isBlank()) {
            throw new IllegalArgumentException("Tên tác giả không được để trống.");
        }
        if (author.getAuthorName().trim().length() > 100) {
            throw new IllegalArgumentException("Tên tác giả không được vượt quá 100 ký tự.");
        }
        author.setAuthorName(author.getAuthorName().trim());
    }
}
