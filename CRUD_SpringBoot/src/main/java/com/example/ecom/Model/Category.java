package com.example.ecom.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Entity
@Getter
public class Category {
    @Id
    private int id;
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public int getId() {
        return id;
    }
}
