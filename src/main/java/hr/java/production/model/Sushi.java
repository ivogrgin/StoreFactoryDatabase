package hr.java.production.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Stores information about Sushi
 */
public class Sushi extends Item implements Edible {
    private final int caloriesPerKg = 140;
    private BigDecimal weight;

    /**
     * constructor for Chicken class
     *
     * @param name           name of Edible Sushi item
     * @param category       category of Edible Sushi item
     * @param width          width of Edible Sushi item
     * @param height         height of Edible Sushi item
     * @param length         length of Edible Sushi item
     * @param productionCost productionCost of Edible Sushi item
     * @param sellingPrice   sellingPrice of Edible Sushi item per kilogram
     * @param weight         weight of Edible Sushi item
     * @param discount       discount of Edible Sushi item
     */

    public Sushi(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                 BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weight, Discount discount, Long id) {

        super(name, category, width, height, length, productionCost, sellingPrice, discount, id);
        this.weight = weight;
    }

    public Sushi(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                 BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weight, Long id) {

        super(name, category, width, height, length, productionCost, sellingPrice, id);
        this.weight = weight;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Override
    public int calculateKilocalories() {
        return BigDecimal.valueOf(caloriesPerKg).multiply(weight).intValue();
    }

    @Override
    public BigDecimal calculatePrice() {
        return (super.getSellingPrice()).multiply(weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sushi sushi = (Sushi) o;
        return caloriesPerKg == sushi.caloriesPerKg && Objects.equals(weight, sushi.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), caloriesPerKg, weight);
    }
}
