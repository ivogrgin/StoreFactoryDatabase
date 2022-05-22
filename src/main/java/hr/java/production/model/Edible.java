package hr.java.production.model;

import java.math.BigDecimal;


/**
 * Interface that is used to calculate price and kilocalories for Edible items
 */
public interface Edible {
    


    /**
     * Calculates Kilocalories
     *
     * @return kilocalories of the Edible Item
     */
    int calculateKilocalories();

    /**
     * Calculates price of the item by multiplying weight with price by kilogram
     *
     * @return the recalculated price for Edible
     */
    BigDecimal calculatePrice();


}
