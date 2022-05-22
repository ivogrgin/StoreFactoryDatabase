package hr.java.production.threads;

import hr.java.production.model.Category;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

public class SaveItemToDatabaseThread implements Callable<Long> {
    private String itemNameString;
    private Category itemCategory;
    private BigDecimal width, height, length, productionCost, sellingPrice;

    public SaveItemToDatabaseThread(String itemNameString, Category itemCategory,
                                    BigDecimal width, BigDecimal height, BigDecimal length,
                                    BigDecimal productionCost, BigDecimal sellingPrice) {
        this.itemNameString = itemNameString;
        this.itemCategory = itemCategory;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws SQLException if unable to compute a result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.saveItemToDatabase(itemNameString, itemCategory,
                width,height, length, productionCost,sellingPrice);
    }
}
