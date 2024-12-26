package com.example.ecom.Service;
import com.example.ecom.Model.Category;
import com.example.ecom.Model.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categoryList = categoryRepo.findAll();
        if (categoryList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
       return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

    public ResponseEntity<String> addNewCategory(Category category) {
        try {
            categoryRepo.save(category);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Category added successfully",HttpStatus.OK);
    }

    public ResponseEntity<String> updateCategory(Category category) {
        Optional<Category> newCategory = categoryRepo.findById(category.getId());
        if(newCategory.isEmpty()) {
            return new ResponseEntity<>("Category not found",HttpStatus.NOT_FOUND);
        }
        categoryRepo.save(category);
        return new ResponseEntity<>(String.format("Category updated successfully Id: %d",category.getId()),HttpStatus.OK);
    }

    public ResponseEntity<String> deleteCategory(int id) {
        Optional<Category> newCategory = categoryRepo.findById(id);
        if (newCategory.isEmpty()) {
            return new ResponseEntity<>("Category not found",HttpStatus.NOT_FOUND);
        }
        categoryRepo.deleteById(id);
        return new ResponseEntity<>(String.format("Category deleted successfully id: %d",id),HttpStatus.OK);
    }
}
