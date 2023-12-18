package com.serenitydojo;

import org.assertj.core.data.MapEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingCart {

    // map of fruits and associated quantities that make up our cart
    private final Map<Fruit, Integer> items;

    // constructor to create the cart as a HashMap
    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    // method to add items to the cart
    public void addItem(Fruit fruit, int quantity, Catalog catalog) throws FruitUnavailableException {
        // check if the fruit is available in the default catalog
        if(!catalog.containsFruit(fruit)) {
            throw new FruitUnavailableException("The fruit " + fruit + " is not currently available");
        }
        // merge the desired fruit and amount into our HashMap
        // will create a new entry if the fruit doesn't already exist in our cart
        // and will update the total amount if it does
        items.merge(fruit, quantity, Integer::sum);
    }

    // method to remove items
    public void removeItem(Fruit fruit, int quantity) throws InsufficientQuantityException, ItemNotFoundException {
        // find the current amount of the requested fruit in the cart
        int currentQuantity = items.getOrDefault(fruit, 0);
        // if the currentQuantity of the fruit is zero, throw an exception
        if(currentQuantity == 0) {
            throw new ItemNotFoundException("There isn't any " + fruit + " in the cart!");
            // if the requested quantity is more than currently exists in the cart, throw an exception
        } else if (quantity > currentQuantity) {
            throw new InsufficientQuantityException("There isn't that much " + fruit + " in the cart!");
        }
        // check that the desired fruit to remove is present, deduct the desired value if so
        // computeIfPresent only runs if the target fruit is present in items
        items.computeIfPresent(fruit, (key, current) -> current - quantity);
        // remove the fruit entirely from our HashMap if the quantity becomes zero
        items.remove(fruit, 0);
    }

    // method to get the total price of all items in the cart
    // includes discount if total quantity is 5kg or more
    public double getTotalPrice(Catalog catalog) {
        // initialize a variable to hold the total price
        double totalPrice = 0.0;
        // for-each loop to iterate over contents of items Map
        for(Map.Entry<Fruit, Integer> entry : items.entrySet()) {
            // retrieve a key/value pair of fruit/quantity
            Fruit fruit = entry.getKey();
            int quantity = entry.getValue();
            // retrieve the price of the current fruit
            double price = catalog.getPriceOf(fruit);
            // add the price of the fruit/quantity pair to the total price
            totalPrice += price * quantity;
        }
        // if the total quantity of fruit is over 5kg, apply the discount
        if(getTotalQuantity() >= 5) {
            totalPrice *= 0.9;
        }
        return totalPrice;
    }

    // method to calculate the total price of items in the cart, with or without a discount
    // DEPRECATED - had to move the discount logic to getTotalPrice() so discount would be applied to the total quantity
    // instead of each quantity of fruit separately - did not work correctly when adding multiple types of fruit with less than 5kg each
//    private double calculateDiscountedPrice(double price, int quantity) {
//        // Apply a 10% discount for 5 kilos or more
//        if(quantity >= 5) {
//            return price * quantity * 0.9;
//        }
//        // otherwise calculate full price
//        return price * quantity;
//    }

    // method to get a List<String> of what is in the cart
    public List<String> getCartItems() {
        // convert the items map into a stream representing the entries in the map
        return items.entrySet().stream()
                // uses lambda expression to extract the key / value pair as a concatenated String
                .map(entry -> entry.getKey() + ": " + entry.getValue() + " kg")
                // collects them into a List
                .collect(Collectors.toList());
    }

    // method to get the amount of a given fruit in the cart
    public int getItemQuantity(Fruit fruit) {
        return items.getOrDefault(fruit, 0);
    }

    // method to get the total amount of items in the cart
    private int getTotalQuantity() {
        // sum up the quantities of all items in the cart
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
}
