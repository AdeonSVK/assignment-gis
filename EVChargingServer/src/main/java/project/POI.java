package project;

public class POI {
    private String name;
    private double longitude;
    private double latitude;
    private String town;
    private String address;
    private ChargingStation station;


    public POI(){

    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ChargingStation getStation() {
        return station;
    }

    public void setStation(ChargingStation station) {
        this.station = station;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

}
