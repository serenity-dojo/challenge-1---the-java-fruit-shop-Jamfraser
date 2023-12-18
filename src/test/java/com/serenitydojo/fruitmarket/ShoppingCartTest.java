package com.serenitydojo.fruitmarket;

import com.serenitydojo.*;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class ShoppingCartTest {
    // create a shopping cart and default catalog to use in our test cases
    ShoppingCart cart = new ShoppingCart();
    // default catalog contains 5 kg each of APPLE, BANANA, ORANGE, PEAR
    Catalog catalog = Catalog.withDefaultItems();

    @Test
    public void shouldAddSingleItemToCart() throws FruitUnavailableException {
        // add a single fruit to the cart
        cart.addItem(Fruit.ORANGE, 1, catalog);
        // assert that the cart contains one orange and the total price is correct
        assertThat(cart.getTotalPrice(catalog)).isEqualTo(catalog.getPriceOf(Fruit.ORANGE));
        // adding a fruit that doesn't exist in the catalog should throw an exception
        assertThrows(FruitUnavailableException.class, () ->  cart.addItem(Fruit.PEACH, 1, catalog));
    }

    @Test
    public void shouldAddMultipleItemsToCart() throws FruitUnavailableException {
        // add multiple different fruits to the cart
        cart.addItem(Fruit.PEAR, 2, catalog);
        cart.addItem(Fruit.APPLE, 1, catalog);
        cart.addItem(Fruit.BANANA, 1, catalog);
        // assert that the cart contains these different fruits
        List<String> cartItems = cart.getCartItems();
        assertThat(cartItems).contains("PEAR: 2 kg", "APPLE: 1 kg", "BANANA: 1 kg");
        // adding multiple fruits that don't exist in the catalog should throw an exception
        assertThrows(FruitUnavailableException.class, () -> {
                    cart.addItem(Fruit.PEACH, 1, catalog);
                    cart.addItem(Fruit.MANGO, 2, catalog);
                });
    }

    @Test
    public void shouldRemoveSingleItemFromCart() throws ItemNotFoundException, InsufficientQuantityException {
        // removing items from an empty cart should throw an exception
        assertThrows(ItemNotFoundException.class, () -> cart.removeItem(Fruit.APPLE, 1));
        // add a quantity of items to the cart in order to test removal
        cart.addItem(Fruit.BANANA, 5, catalog);
        // removing an item that doesn't exist in the cart should throw an exception
        assertThrows(ItemNotFoundException.class, () -> cart.removeItem(Fruit.PEAR, 1));
        // record the initial added quantity value for comparison later
        int initialQuantity = cart.getItemQuantity(Fruit.BANANA);
        // if the item is present in the cart, decrement its quantity by requested amount
        cart.removeItem(Fruit.BANANA, 1);
        // assert that the new quantity is correct
        int newQuantity = cart.getItemQuantity(Fruit.BANANA);
        assertThat(newQuantity).isEqualTo(initialQuantity - 1);
        // removing more items than are currently present should throw an exception
        assertThrows(InsufficientQuantityException.class, () -> cart.removeItem(Fruit.BANANA, 5));
    }

    @Test
    public void shouldRemoveMultipleItemsOfSameTypeFromCart() throws ItemNotFoundException, InsufficientQuantityException {
        // removing items from an empty cart should throw an exception
        assertThrows(ItemNotFoundException.class, () -> cart.removeItem(Fruit.ORANGE, 1));
        // add multiple items of the same type to the cart
        cart.addItem(Fruit.ORANGE, 3, catalog);
        // record the initial added quantity value for comparison later
        int initialQuantity = cart.getItemQuantity(Fruit.ORANGE);
        // remove multiple items of the same type from the cart
        cart.removeItem(Fruit.ORANGE, 2);
        // assert that the new quantity is correct
        int newQuantity = cart.getItemQuantity(Fruit.ORANGE);
        assertThat(newQuantity).isEqualTo(initialQuantity - 2);
        // removing more items than are currently present should throw an exception
        assertThrows(InsufficientQuantityException.class, () -> cart.removeItem(Fruit.ORANGE, 5));
    }

    @Test
    public void shouldRemoveItemsOfDifferentTypesFromCart() throws ItemNotFoundException, InsufficientQuantityException {
        // removing items from an empty cart should throw an exception
        assertThrows(ItemNotFoundException.class, () -> cart.removeItem(Fruit.ORANGE, 1));
        // add items of different types to the cart
        cart.addItem(Fruit.PEAR, 2, catalog);
        cart.addItem(Fruit.APPLE, 4, catalog);
        // record quantities for comparison later
        int initialPearQuantity = cart.getItemQuantity(Fruit.PEAR);
        int initialAppleQuantity = cart.getItemQuantity(Fruit.APPLE);
        // remove items of different types from the cart
        cart.removeItem(Fruit.PEAR, 1);
        cart.removeItem(Fruit.APPLE, 2);
        // assert that the new quantities are correct
        int newPearQuantity = cart.getItemQuantity(Fruit.PEAR);
        assertThat(newPearQuantity).isEqualTo(initialPearQuantity - 1);
        int newAppleQuantity = cart.getItemQuantity(Fruit.APPLE);
        assertThat(newAppleQuantity).isEqualTo(initialAppleQuantity - 2);
        // removing more items than are currently present should throw an exception
        assertThrows(InsufficientQuantityException.class, () -> cart.removeItem(Fruit.PEAR, 5));
        assertThrows(InsufficientQuantityException.class, () -> cart.removeItem(Fruit.APPLE, 5));
    }

    @Test
    public void shouldCalculateTotalPriceOfAnEmptyCart() {
        // get the total price of an empty cart
        double totalPrice = cart.getTotalPrice(catalog);
        // assert that the price is zero
        assertThat(totalPrice).isZero();
    }

    @Test
    public void shouldCalculateTotalPriceOfASingleItem() {
        // add a single item to the cart
        cart.addItem(Fruit.BANANA, 1, catalog);
        // get the price of a banana from the catalog
        double bananaPrice = catalog.getPriceOf(Fruit.BANANA);
        // get the total price of the one item
        double totalPrice = cart.getTotalPrice(catalog);
        // assert that the price is correct
        assertThat(totalPrice).isEqualTo(bananaPrice);
    }

    @Test
    public void shouldCalculateTotalPriceOfMultipleItemsOfSameType() throws FruitUnavailableException {
        // add multiple items of the same type to the cart
        cart.addItem(Fruit.ORANGE, 3, catalog);
        // calculate the total price for the cart without discount
        double totalPrice = cart.getTotalPrice(catalog);
        // assert the total price is correct for the given quantity of fruit
        double expectedPrice = catalog.getPriceOf(Fruit.ORANGE) * 3;
        assertThat(totalPrice).isEqualTo(expectedPrice);
        // add more items of the same type to trigger the discount
        cart.addItem(Fruit.ORANGE, 3, catalog);
        // calculate the new total price for the cart
        totalPrice = cart.getTotalPrice(catalog);
        // assert that the new total price is correct and includes the discount
        double expectedTotalPrice = (catalog.getPriceOf(Fruit.ORANGE) * 6) * 0.9;
        assertThat(totalPrice).isEqualTo(expectedTotalPrice);
    }

    @Test
    public void shouldCalculateTotalPriceOfMultipleItemsOfDifferentTypes() throws FruitUnavailableException {
        // add multiple items of different types to the cart
        cart.addItem(Fruit.APPLE, 1, catalog);
        cart.addItem(Fruit.BANANA, 2, catalog);
        cart.addItem(Fruit.PEAR, 1, catalog);
        // calculate the total price for the cart
        double totalPrice = cart.getTotalPrice(catalog);
        // assert the total price is correct for the given quantity of fruit
        double expectedPrice = catalog.getPriceOf(Fruit.APPLE) * 1
                + catalog.getPriceOf(Fruit.BANANA) * 2
                + catalog.getPriceOf(Fruit.PEAR);
        assertThat(totalPrice).isEqualTo(expectedPrice);
    }

    @Test
    public void shouldCalculatePriceWithDiscountOn5KgOrMoreOfFruit() throws FruitUnavailableException {
        // add less than 5kg of fruit to the cart
        cart.addItem(Fruit.APPLE, 4, catalog);
        // calculate the total price without a discount
        double totalPriceWithoutDiscount = cart.getTotalPrice(catalog);
        // assert the total price is correct for the given quantity of fruit, with no discount
        double expectedPriceWithoutDiscount = catalog.getPriceOf(Fruit.APPLE) * 4;
        assertThat(totalPriceWithoutDiscount).isEqualTo(expectedPriceWithoutDiscount);
        // add enough fruit to make the total 5kg or more
        cart.addItem(Fruit.BANANA, 2, catalog);
        // calculate the total price with a 10% discount
        double totalPriceWithDiscount = cart.getTotalPrice(catalog);
        // assert the total price is correct for the given quantity of fruit, now including a 10% discount
        double expectedPriceWithDiscount = (expectedPriceWithoutDiscount + catalog.getPriceOf(Fruit.BANANA) * 2) * 0.9;
        assertThat(totalPriceWithDiscount).isEqualTo(expectedPriceWithDiscount);
    }

    @Test
    public void shouldNotApplyDiscountForLessThan5Kg() throws FruitUnavailableException {
        // add less than 5kg of different fruits to the cart
        cart.addItem(Fruit.ORANGE, 2, catalog);
        cart.addItem(Fruit.PEAR, 1, catalog);
        // calculate the total price
        double totalPrice = cart.getTotalPrice(catalog);
        // assert that the discount is not applied
        assertThat(totalPrice).isEqualTo(catalog.getPriceOf(Fruit.ORANGE) * 2 + catalog.getPriceOf(Fruit.PEAR));
    }

}
