package hr.java.production.model;

import hr.java.production.model.Client;
import hr.java.production.model.Item;
import hr.java.production.model.NamedEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class Transaction extends NamedEntity {

    private final Client client;
    private final LocalDate transactionDate;
    private final Set<Item> items;

    public Transaction(String name, Long id, Client client, LocalDate transactionDate, Set<Item> items) {
        super(name, id);
        this.client = client;
        this.transactionDate = transactionDate;
        this.items = items;
    }

    public Client getClient() {
        return client;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public Set<Item> getItems() {
        return items;
    }

    public BigDecimal sumItemSellingPrice(){
       return items.stream().map(Item::getSellingPrice).reduce(BigDecimal::add).get();

    }
}
