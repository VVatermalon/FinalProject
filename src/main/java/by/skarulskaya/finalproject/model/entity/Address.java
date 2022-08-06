package by.skarulskaya.finalproject.model.entity;

import java.util.Optional;

public class Address extends CustomEntity {
    public enum AvailableCountries {
        USA, RUSSIA, CANADA, CHINA, BRAZIL, AUSTRALIA, INDIA, ARGENTINA,
        KAZAKHSTAN, FRANCE, UKRAINE, THAILAND, SPAIN, SWEDEN, JAPAN,
        GERMANY, POLAND, ITALY, BELARUS;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    private AvailableCountries country;
    private String city;
    private String address;
    private Optional<String> apartment;
    private String postalCode;

    public Address(AvailableCountries country, String city, String address, Optional<String> apartment, String postalCode) {
        this.country = country;
        this.city = city;
        this.address = address;
        this.apartment = apartment;
        this.postalCode = postalCode;
    }

    public Address(int id, AvailableCountries country, String city, String address, Optional<String> apartment, String postalCode) {
        super(id);
        this.country = country;
        this.city = city;
        this.address = address;
        this.apartment = apartment;
        this.postalCode = postalCode;
    }

    public AvailableCountries getCountry() {
        return country;
    }

    public void setCountry(AvailableCountries country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Optional<String> getApartment() {
        return apartment;
    }

    public void setApartment(Optional<String> apartment) {
        this.apartment = apartment;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country=" + country +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", apartment=" + apartment +
                ", postalCode='" + postalCode + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;

        Address that = (Address) o;

        if (country != that.country) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (!apartment.equals(that.apartment)) return false;
        return postalCode != null ? postalCode.equals(that.postalCode) : that.postalCode == null;
    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + apartment.hashCode();
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }
}
