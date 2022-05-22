package hr.java.production.model;

/**
 * Interface that is used to get warranty for Technical items
 */
public sealed interface Technical permits Laptop {

     Integer getWarranty();
}
