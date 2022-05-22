package hr.java.production.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Laptop extends Item implements Technical {

    private final Integer warranty;

    /**
     * Constructor for Laptop
     *
     * @param name           name of laptop
     * @param category       category of laptop
     * @param width          width of Laptop
     * @param height         height of Laptop
     * @param length         length of Laptop
     * @param productionCost production cost of Laptop
     * @param sellingPrice   selling price of Laptop
     * @param discount       discount of Laptop
     * @param warranty       warranty of Laptop
     */
    public Laptop(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                  BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, Integer warranty, Long id) {
        super(name, category, width, height, length, productionCost, sellingPrice, discount, id);
        this.warranty = warranty;
    }

    public Laptop(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                  BigDecimal productionCost, BigDecimal sellingPrice, Integer warranty, Long id) {
        super(name, category, width, height, length, productionCost, sellingPrice, id);
        this.warranty = warranty;
    }


    @Override
    public Integer getWarranty() {
        return warranty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Laptop laptop = (Laptop) o;
        return warranty == laptop.warranty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), warranty);
    }
}
