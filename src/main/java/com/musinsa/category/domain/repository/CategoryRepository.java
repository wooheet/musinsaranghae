package com.musinsa.category.domain.repository;

import com.musinsa.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById (Long id);
    Optional<Category> findByName (String name);
    Optional<Category> findByBranchAndName (String branch, String name);

    Boolean existsByBranchAndName(String branch, String name);

    Optional<Category> findByBranchAndCode(String branch, String code);
}