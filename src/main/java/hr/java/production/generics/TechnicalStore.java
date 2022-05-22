package hr.java.production.generics;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.model.Technical;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TechnicalStore<T extends Technical> extends Store {
    private List<T> listOfTechnicalItems;

    public TechnicalStore(String name, String webAddress, Set<Item> items, List<T> listOfTechnicalItems, Long id) {
        super(name, webAddress, (TreeSet<Item>) items, id);
        this.listOfTechnicalItems = listOfTechnicalItems;
    }

    public List<T> getListOfTechnicalItems() {
        return listOfTechnicalItems;
    }

    public void setListOfTechnicalItems(List<T> listOfTechnicalItems) {
        this.listOfTechnicalItems = listOfTechnicalItems;
    }
}
