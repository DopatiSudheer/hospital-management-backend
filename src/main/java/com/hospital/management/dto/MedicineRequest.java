package com.hospital.management.dto;

public class MedicineRequest {
    
    private String medicineName;
    private Double price;
    private Integer stock;

    public MedicineRequest() {
    }

    public MedicineRequest(String medicineName, Double price, Integer stock) {
        this.medicineName = medicineName;
        this.price = price;
        this.stock = stock;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}

