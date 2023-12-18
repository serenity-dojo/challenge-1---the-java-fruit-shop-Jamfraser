package com.serenitydojo;

import java.util.*;

public class Catalog {
    // create a map to hold our fruit/price key/value pairs
    private final Map<Fruit, Double> fruitToPrice = new HashMap<>();

    // create a List to hold the fruits that are added to the catalog
    private final List<CatalogItem> availableFruits = new ArrayList<>();

    public void updatePriceOf(Fruit fruit, double price) {
        // use HashMap .put() method to update the fruit with the new price
        fruitToPrice.put(fruit, price);
    }

    // constructor for default catalog
    public static Catalog withDefaultItems() {
        // create the catalog
        Catalog catalog = new Catalog();
        // set default prices
        catalog.updatePriceOf(Fruit.APPLE, 4.00);
        catalog.updatePriceOf(Fruit.BANANA, 6.00);
        catalog.updatePriceOf(Fruit.ORANGE, 5.50);
        catalog.updatePriceOf(Fruit.PEAR, 4.50);
        // add default items to the availableFruits List
        for (Fruit fruit : Arrays.asList(Fruit.APPLE, Fruit.BANANA, Fruit.ORANGE, Fruit.PEAR)) {
            catalog.availableFruits.add(new CatalogItem(fruit, 5));
        }
        return catalog;
    }

    // constructor for custom catalog
    public static Catalog withItems(Map<Fruit, Double> customPrices, CatalogItem... catalogItems) {
        // create the catalog
        Catalog catalog = new Catalog();
        // set the custom prices
        for(Map.Entry<Fruit, Double> entry : customPrices.entrySet()) {
            catalog.updatePriceOf(entry.getKey(), entry.getValue());
        }
        // for every item included in the constructor parameters
        for (CatalogItem catalogItem : catalogItems) {
            // iterate through and add them to the availableFruits List
            for (int i = 0; i < catalogItem.getQuantity(); i++) {
                catalog.availableFruits.add(catalogItem);
            }
        }
        return catalog;
    }

    // method to get the price of a given fruit
    public double getPriceOf(Fruit fruit) {
        // if the fruitToPrice Map doesn't contain the queried fruit
        if (!fruitToPrice.containsKey(fruit)) {
            // throw the custom exception message
            throw new FruitUnavailableException(fruit.name() + " is not available in the catalog.");
        }
        // otherwise get the price of the available fruit
        return fruitToPrice.get(fruit);
    }

    // method to get a List of which fruits are available in the catalog
    public List<CatalogItem> getAvailableFruits() throws FruitUnavailableException {
        // if existing List of availableFruits contains no fruit, throw an exception
        if(availableFruits.isEmpty()) {
            throw new FruitUnavailableException("No fruits are available in the catalog");
        }
        // otherwise, create a new List from availableFruits and sort it by fruit name, then quantity
        List<CatalogItem> sortedFruits = new ArrayList<>(availableFruits);
        sortedFruits.sort(Comparator
                .<CatalogItem, String>comparing(item -> item.getFruit().name())
                .thenComparingInt(CatalogItem::getQuantity));
        return sortedFruits;
    }

    // method to determine if a given fruit is present in our Map or not
    public boolean containsFruit(Fruit fruit) {
        return fruitToPrice.containsKey(fruit);
    }

    // method to print what is currently in the catalog - useful for debugging
    public void printCatalog() {
        System.out.println("Catalog Contents:");
        // iterate through the List of availableFruits and print out a message including the fruit name and quantity
        for(CatalogItem item : availableFruits) {
            System.out.println(item.getFruit() + " - Quantity: " + item.getQuantity());
        }
    }
}
