package hr.java.production.model;

import hr.java.production.exception.CopiesOfSameCategory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Stores information about Category
 */
public class Category extends NamedEntity implements Serializable {
    //private static final Logger logger = LoggerFactory.getLogger(Category.class);
    private String description;
    private static final int  NUMBER_OF_VARIABLES = 3;
    private static final String PATH_TO_CATEGORY_FILE = "dat/categories.txt";

    /**
     * constructor for Category class
     *
     * @param name        Category name
     * @param description Category Description
     */
    public Category(String name, String description, Long id) {
        super(name,id);
        this.description = description;
    }

    public static String getPathToCategoryFile() {
        return PATH_TO_CATEGORY_FILE;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(description, category.description) && Objects.equals(getName(), category.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
