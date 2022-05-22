package hr.java.production.model;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Stores information about Item
 */
public class Item extends NamedEntity implements Serializable {


    private Category category;
    private BigDecimal width, height, length, productionCost, sellingPrice;
    private Discount discount;
    private final BigDecimal volume;



    /**
     * constructor for Item class
     *
     * @param name           name of Item
     * @param category       category of Item
     * @param width          width of Item
     * @param height         height of Item
     * @param length         length of Item
     * @param productionCost productionCost of Item
     * @param sellingPrice   sellingPrice of Item
     * @param discount       discount of Item
     */
    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, Long id) {

        super(name, id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.volume = width.multiply(height.multiply(length));
    }

    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Long id) {

        super(name, id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = new Discount(BigDecimal.ZERO);
        this.volume = width.multiply(height.multiply(length));
    }

    @Override
    public String toString() {
        return super.getName();
    }

    public BigDecimal getVolume() {
        return volume;
    }


    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public BigDecimal getSellingPrice() {
        return this.sellingPrice.subtract((this.sellingPrice
        ).multiply(this.getDiscount().discountAmount().divide(BigDecimal.valueOf(100.0))));
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(category, item.category) && Objects.equals(width, item.width) && Objects.equals(height,
                item.height) && Objects.equals(length, item.length)
                && Objects.equals(productionCost, item.productionCost)
                && Objects.equals(sellingPrice, item.sellingPrice) && Objects.equals(discount, item.discount)
                && Objects.equals(getName(), item.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, width, height, length, productionCost, sellingPrice, discount, getName());
    }



}
