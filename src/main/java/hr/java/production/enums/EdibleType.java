package hr.java.production.enums;

public enum EdibleType {
    CHICKEN("Piletina"), SUSHI("Sushi");
    private final String type;
    EdibleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
