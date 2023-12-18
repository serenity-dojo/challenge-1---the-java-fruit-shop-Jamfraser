package com.serenitydojo;

import java.util.Objects;

public class CatalogItem {
    // each item in our catalog will be a fruit
    private final Fruit fruit;
    // we should also keep track of how many there are
    private final int quantity;

    // constructor to create a catalog item out of enumerated fruits
    public CatalogItem(Fruit fruit, int quantity) {
        this.fruit = fruit;
        this.quantity = quantity;
    }

    // getter methods
    public Fruit getFruit() { return fruit; }

    public int getQuantity() {
        return quantity;
    }

    // override equals() and hashCode() methods so that comparisons can be made to catalog contents instead of their memory addresses
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CatalogItem that = (CatalogItem) obj;
        return this.fruit == that.fruit && this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fruit, quantity);
    }
}
