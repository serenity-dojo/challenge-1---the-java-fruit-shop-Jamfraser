package com.serenitydojo.fruitmarket;

import com.serenitydojo.Catalog;
import com.serenitydojo.CatalogItem;
import com.serenitydojo.Fruit;

import com.serenitydojo.FruitUnavailableException;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class CatalogTest {

    // create a default catalog for our tests that require one
    Catalog catalog = Catalog.withDefaultItems();

    @Test
    public void shouldCreateADefaultCatalogCorrectly() throws FruitUnavailableException {
        // assert that the catalog is not empty
        assertThat(catalog).isNotNull();

        // assert that the catalog contains the expected fruits with correct quantities
        // MUST OVERRIDE equals() and hashCode() methods in CatalogItem for this to work!
        List<CatalogItem> availableFruits = catalog.getAvailableFruits();
        assertThat(availableFruits).contains(
                new CatalogItem(Fruit.APPLE, 5),
                new CatalogItem(Fruit.BANANA, 5),
                new CatalogItem(Fruit.ORANGE, 5),
                new CatalogItem(Fruit.PEAR, 5)
        );

        // assert that the prices for each fruit are correct
        assertThat(catalog.getPriceOf(Fruit.APPLE)).isEqualTo(4.00);
        assertThat(catalog.getPriceOf(Fruit.BANANA)).isEqualTo(6.00);
        assertThat(catalog.getPriceOf(Fruit.ORANGE)).isEqualTo(5.50);
        assertThat(catalog.getPriceOf(Fruit.PEAR)).isEqualTo(4.50);
    }

    @Test
    public void shouldBeAbleToUpdateTheCurrentPriceOfAFruit() {
        // assert that the initial prices of apples and oranges are correct
        assertThat(catalog.getPriceOf(Fruit.APPLE)).isEqualTo(4.00);
        assertThat(catalog.getPriceOf(Fruit.ORANGE)).isEqualTo(5.50);
        // update the price of an apple to $5 and the price of an orange to $4.50
        catalog.updatePriceOf(Fruit.APPLE, 5.00);
        catalog.updatePriceOf(Fruit.ORANGE, 4.50);
        // assert that the prices have been updated
        assertThat(catalog.getPriceOf(Fruit.APPLE)).isEqualTo(5.00);
        assertThat(catalog.getPriceOf(Fruit.ORANGE)).isEqualTo(4.50);
    }

    @Test
    public void shouldListAvailableFruitsAlphabetically() {
        // create a List with the available fruits
        List<CatalogItem> fruits = catalog.getAvailableFruits();
        //catalog.printCatalog();
        // assert that the order of the fruit names is alphabetical
        assertThat(fruits.get(0).getFruit()).isEqualTo(Fruit.APPLE);
        assertThat(fruits.get(1).getFruit()).isEqualTo(Fruit.BANANA);
        assertThat(fruits.get(2).getFruit()).isEqualTo(Fruit.ORANGE);
        assertThat(fruits.get(3).getFruit()).isEqualTo(Fruit.PEAR);
    }

    @Test
    public void shouldUpdatePriceForFruitNotInitiallyInCatalog() throws FruitUnavailableException {
        // assert that the price of Peach is not initially in the catalog
        assertThrows(FruitUnavailableException.class, () -> catalog.getPriceOf(Fruit.PEACH));
        // update the price of a peach
        catalog.updatePriceOf(Fruit.PEACH, 3.50);
        // assert that the price of a peach has been updated and added to the catalog
        assertThat(catalog.getPriceOf(Fruit.PEACH)).isEqualTo(3.50);
    }

    @Test
    public void shouldThrowErrorWhenCatalogIsEmpty() {
        // create an empty catalog
        Catalog catalog = Catalog.withItems(Collections.emptyMap());
        // assert that attempting to getAvailableFruits on empty catalog throws FruitUnavailableException
        assertThrows(FruitUnavailableException.class, catalog::getAvailableFruits);
    }

    @Test
    public void shouldBeAbleToRetrieveAPrice() {
        // assert that the default prices are correct
        assertThat(catalog.getPriceOf(Fruit.APPLE)).isEqualTo(4.00);
        assertThat(catalog.getPriceOf(Fruit.BANANA)).isEqualTo(6.00);
        assertThat(catalog.getPriceOf(Fruit.ORANGE)).isEqualTo(5.50);
        assertThat(catalog.getPriceOf(Fruit.PEAR)).isEqualTo(4.50);

        // assert that checking for the price of a fruit not in the catalog throws FruitUnavailableException
        assertThrows(FruitUnavailableException.class, () -> catalog.getPriceOf(Fruit.MANGO));
    }
}
