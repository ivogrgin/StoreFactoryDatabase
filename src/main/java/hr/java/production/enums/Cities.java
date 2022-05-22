package hr.java.production.enums;

public enum Cities {
    ZAGREB("Zagreb", "10000"), OSJEK("Osjek", "31000"),
    RIJEKA("Rijeka", "51000"), PULA("Pula", "52100"), SPLIT("Split", "21000"),
    ROVINJ("Rovinj", "52210"), KARLOVAC("Karlovac", "47000"),
    DOUBROVNIK("Dubrovnik", "20000"), KOPRIVNICA("Koprivnica", "48000");

    private String cityName, postalCode;

    Cities(String cityName, String postalCode) {
        this.cityName = cityName;
        this.postalCode = postalCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
