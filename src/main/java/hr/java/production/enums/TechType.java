package hr.java.production.enums;

public enum TechType {
    LAPTOP("Laptop");
    private final String type;
    TechType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
