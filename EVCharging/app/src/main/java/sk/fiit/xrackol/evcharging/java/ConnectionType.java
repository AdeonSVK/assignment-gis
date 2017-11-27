package sk.fiit.xrackol.evcharging.java;

public class ConnectionType {

    private String name;
    private double powerKW;
    private int amps;
    private int voltage;
    private int connectionTypeId;
    private String currentType;
    private int quantity;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public int getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(int connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(double powerKW) {
        this.powerKW = powerKW;
    }

    public int getAmps() {
        return amps;
    }

    public void setAmps(int amps) {
        this.amps = amps;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

}
