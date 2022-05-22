package hr.java.production.generics;

import hr.java.production.model.Edible;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FoodStore<T extends Edible> extends Store {
    private List<T> listOfFood;

    public FoodStore(String name, String webAddress, Set<Item> items, List<T> listOfFood, Long id) {
        super(name, webAddress, (TreeSet<Item>) items, id);
        this.listOfFood = listOfFood;

    }

    public List<T> getListOfFood() {
        return listOfFood;
    }

    public void setListOfFood(List<T> listOfFood) {
        this.listOfFood = listOfFood;
    }
}
