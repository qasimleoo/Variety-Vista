package com.practice.project.repository;

import com.practice.project.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository <Category, Integer> {

}
