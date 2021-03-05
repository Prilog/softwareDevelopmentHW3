package ru.akirakozov.sd.refactoring;

import java.util.Objects;

public class Product {
    public String name;
    public Long price;

    public Product(String n, Long p) {
        name = n;
        price = p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
