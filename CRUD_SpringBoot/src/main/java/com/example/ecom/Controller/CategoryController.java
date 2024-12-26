package com.example.ecom.Controller;

import com.example.ecom.Model.Category;
import com.example.ecom.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class CategoryController {
    @Autowired
    private CategoryService CategoryService;
    @GetMapping("/public/category")
    public ResponseEntity<List<Category>> getCategory()
    {
        return CategoryService.getAllCategory();
    }
    @PostMapping("/admin/category")
    public ResponseEntity<String> addNewCategory(@RequestBody Category category)
    {
        return CategoryService.addNewCategory(category);
    }
    @PutMapping("/admin/category")
    public ResponseEntity<String> updateCategory(@RequestBody Category category)
    {
        return CategoryService.updateCategory(category);
    }
    @DeleteMapping("/admin/category")
    public ResponseEntity<String> deleteCategory(@RequestParam int id)
    {
        return CategoryService.deleteCategory(id);
    }
}
