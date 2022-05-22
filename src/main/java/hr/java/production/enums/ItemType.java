package hr.java.production.enums;

public enum ItemType {
    EDIBLE("Jestiv"), TEHNOLOGICAL("Tehnološki"), OTHER("Ostalo");
    private final String type;
    ItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
