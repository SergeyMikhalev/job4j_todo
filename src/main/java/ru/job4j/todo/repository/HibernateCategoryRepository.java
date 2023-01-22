package ru.job4j.todo.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class HibernateCategoryRepository implements CategoryRepository {

    public static final String FIND_ALL = "from Category c";
    public static final String FIND_BY_ID = "from Category c where c.id=:fId";

    private final CrudRepository crudRepository;

    public HibernateCategoryRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public List<Category> findAll() {
        return crudRepository.query(FIND_ALL, Category.class);
    }

    @Override
    public Optional<Category> findById(int categoryId) {
        return crudRepository.optional(
                FIND_BY_ID,
                Category.class,
                Map.of("fId", categoryId));
    }
}
