package hr.java.production.model;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

/**
 * used to store String name
 */
public abstract class NamedEntity {
    private String name;
    private Long id;


    /**
     * Constructor for NamedEntity class
     *
     * @param name The name
     * @param id The name
     */
    public NamedEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
