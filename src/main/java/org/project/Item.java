package org.project;

import java.time.LocalDate;

public class Item {
    int id;
    String Product_name;
    int Product_quantity;
    private LocalDate Product_expiryDate;
    private int Product_minStock;
    private String Product_category;
    private int Product_price;

    public LocalDate getProduct_expiryDate() {
        return Product_expiryDate;
    }

    public void setProduct_expiryDate(LocalDate product_expiryDate) {
        Product_expiryDate = product_expiryDate;
    }

    public int getProduct_minStock() {
        return Product_minStock;
    }

    public void setProduct_minStock(int product_minStock) {
        Product_minStock = product_minStock;
    }

    public String getProduct_category() {
        return Product_category;
    }

    public void setProduct_category(String product_category) {
        Product_category = product_category;
    }

    public int getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(int product_price) {
        Product_price = product_price;
    }

    public Item(int id, String productName, int productQuantity, String Category, int price, LocalDate expiryDate, int minStock)
    {
        this.id = id;
        this.Product_name = productName;
        this.Product_quantity = productQuantity;
        this.Product_category = Category;
        this.Product_price = price;
        this.Product_expiryDate = expiryDate;
        this.Product_minStock = minStock;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public int getProduct_quantity() {
        return Product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        Product_quantity = product_quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
