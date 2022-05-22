package hr.java.production.model;

import hr.java.production.enums.Cities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * Stores information about Address
 */
public class Address implements Serializable {

    private String street, HouseNumber, cityString;
    private Cities city;
    private Long id, postalCode;
    private static String PATH_TO_ADDRESS_FILE = "dat/addresses.txt";

    private Address() {
    }

    public static String getPathToAddressFile()
    {
        return PATH_TO_ADDRESS_FILE;
    }

    public static void setPathToAddressFile(String pathToAddressFile) {
        PATH_TO_ADDRESS_FILE = pathToAddressFile;
    }

    /**
     * Creates a new Adress object using a builder pattern from user input
     *
     * @param addrID id of the wanted address
     * @return New Address object
     */
    public static Address insertNewAdress(Long addrID) {

        String street = "", HouseNumber = "", line;
        Optional<Cities> city = Optional.empty();
        boolean found = false;
        int valueAddedCounter = 0;
        int lineCounter = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_ADDRESS_FILE))) {

            while ((line = br.readLine()) != null) {
                if (lineCounter % 4 == 1 && !found) {
                    if (line.matches("\\d*") && !line.matches("-\\d*(\\.?\\d+)")
                            && Integer.parseInt(line) == addrID) {
                        found = true;
                        valueAddedCounter++;
                    }

                } else if (found) {


                    if (lineCounter % 4 == 2) {
                        city = Optional.ofNullable(Arrays.stream(Cities.values()).toList()
                                .get(Integer.parseInt(line) - 1));
                        valueAddedCounter++;
                    }

                    if (lineCounter % 4 == 0) {
                        HouseNumber = line;
                        valueAddedCounter++;
                    }


                    if (lineCounter % 4 == 3) {
                        street = line;
                        valueAddedCounter++;
                    }

                }
                if(valueAddedCounter == 4)
                {
                    break;

                }else
                {
                    lineCounter++;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Address.Builder().setId(addrID).setStreet(street).setHouseNumber(HouseNumber)
                .setCity(city.orElse(null)).build();

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return HouseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.HouseNumber = houseNumber;
    }

    public Cities getCity() {
        return city;
    }

    public String getCityString() {
        if(city != null)
        {
            return city.getCityName();
        }
        return cityString;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCity(Cities city) {
        this.city = city;
    }

    public void setCityString(String city) {
        this.cityString = city;
    }

    public Long getId() {
        return id;
    }

    public Long getPostalCode() {
        return postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        if (city == null)
        {
            return Objects.equals(street, address.street) && Objects.equals(HouseNumber, address.HouseNumber) &&
                    Objects.equals(cityString, address.cityString);
        }else
        {
            return Objects.equals(street, address.street) && Objects.equals(HouseNumber, address.HouseNumber)
                    && city == address.city;
        }

    }



    @Override
    public int hashCode() {
        return Objects.hash(street, HouseNumber, city);
    }

    /**
     * Builder pattern class for creating Address objects
     */
    public static class Builder {

        private String street, HouseNumber, cityString;
        private Cities city;
        private Long id, postalCode;

        /**
         * Address object builder
         *
         * @return object of Address class
         */
        public Address build() {
            Address address = new Address();
            address.street = this.street;
            address.city = this.city;
            address.HouseNumber = this.HouseNumber;
            address.id = this.id;
            address.postalCode = this.postalCode;
            address.cityString = this.cityString;
            return address;

        }



        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }


        public Builder setHouseNumber(String houseNumber) {
            this.HouseNumber = houseNumber;
            return this;

        }

        public Builder setCity(Cities city) {
            this.city = city;
            return this;
        }

        public Builder setCityString(String cityString) {
            this.cityString = cityString;
            return this;
        }

        public Builder setPostalCode(Long postalCode) {
            this.postalCode = postalCode;
            return this;
        }
    }
}
