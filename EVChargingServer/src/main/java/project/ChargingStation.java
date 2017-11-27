package project;

import java.util.ArrayList;

public class ChargingStation {
    private String operationalStatus;
    private String usage;
    private String usageCost;
    private String operator;
    private String operatorWeb;
    private int numberOfPoints;

    // ConnectionType Info
    private ArrayList<ConnectionType> connections;

    public ArrayList<ConnectionType> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<ConnectionType> connections) {
        this.connections = connections;
    }

    public String getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getUsageCost() {
        return usageCost;
    }

    public void setUsageCost(String usageCost) {
        this.usageCost = usageCost;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorWeb() {
        return operatorWeb;
    }

    public void setOperatorWeb(String operatorWeb) {
        this.operatorWeb = operatorWeb;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

}
