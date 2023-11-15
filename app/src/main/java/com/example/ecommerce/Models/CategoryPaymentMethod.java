package com.example.ecommerce.Models;

public class CategoryPaymentMethod {
    private String name, imageName;

    public CategoryPaymentMethod(String name, String imageName) {
        this.name = name;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
