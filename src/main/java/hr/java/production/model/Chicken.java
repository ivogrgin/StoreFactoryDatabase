package hr.java.production.model;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * Stores information about Chicken
 */
public class Chicken extends Item implements Edible {

    private final int caloriesPerKg = 200;
    private BigDecimal weight;

    /**
     * constructor for Chicken class
     *
     * @param name           name of Edible chicken item
     * @param category       category of Edible chicken item
     * @param width          width of Edible chicken item
     * @param height         height of Edible chicken item
     * @param length         length of Edible chicken item
     * @param productionCost productionCost of Edible chicken item
     * @param sellingPrice   sellingPrice of Edible chicken item per kilogram
     * @param weight         weight of Edible chicken item
     * @param discount       discount of Edible chicken item
     */
    public Chicken(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                   BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weight, Discount discount, Long id) {

        super(name, category, width, height, length, productionCost, sellingPrice, discount, id);
        this.weight = weight;
    }

    public Chicken(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
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
        return getSellingPrice().multiply(weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Chicken chicken = (Chicken) o;
        return caloriesPerKg == chicken.caloriesPerKg && weight.equals(chicken.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), caloriesPerKg, weight);
    }
}
