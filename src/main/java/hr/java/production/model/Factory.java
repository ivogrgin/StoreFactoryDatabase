package hr.java.production.model;

import java.io.Serializable;
import java.util.*;


/**
 * Stores information about Factory
 */
public class Factory extends NamedEntity implements Serializable {

    private Address address;
    private Set<Item> items;

    /**
     * Constructor for Factory class
     *
     * @param name    name of factory
     * @param address address of Factory
     * @param items   array of Item objects
     */
    public Factory(String name, Address address, TreeSet<Item> items, Long id) {
        super(name, id);
        this.address = address;
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAdress(Address address) {
        this.address = address;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Factory factory = (Factory) o;
        return Objects.equals(address, factory.address) && Objects.equals(items, factory.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, items);
    }
}
