package sk.fiit.xrackol.evcharging.java;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {


    public ArrayList<POI> createPoiList(String response){
        try {

            int counter = 0;

            JSONArray data = new JSONArray(response);
            JSONObject station;
            POI poi;
            ArrayList<POI> pois = new ArrayList<POI>();
            for(int i=0; i < data.length(); i++){
                station = data.getJSONObject(i);
                // Fill POI data
                poi = new POI();
                poi.setLatitude(getDoubleVal(station,"latitude"));
                poi.setLongitude(getDoubleVal(station,"longitude"));
                poi.setName(getStringVal(station,"name"));
                poi.setTown(getStringVal(station,"town"));
                poi.setAddress(getStringVal(station,"address"));

                // fill CharginStationData
                ChargingStation chs = new ChargingStation();
                JSONObject chargingStation = station.getJSONObject("station");
                chs.setOperationalStatus(getStringVal(chargingStation, "operationalStatus"));
                chs.setUsage(getStringVal(chargingStation,"usage"));
                chs.setUsageCost(getStringVal(chargingStation,"usageCost"));
                chs.setOperator(getStringVal(chargingStation,"operator"));
                chs.setOperatorWeb(getStringVal(chargingStation,"operatorWeb"));
                chs.setNumberOfPoints(getIntVal(chargingStation,"numberOfPoints"));

                // Fill ConnectionType data
                JSONArray connections = chargingStation.getJSONArray("connections");

                // create connections arraylist
                chs.setConnections(new ArrayList<ConnectionType>());

                for(int j=0; j < connections.length(); j++){
                    JSONObject conn = connections.getJSONObject(j);
                    ConnectionType ct = new ConnectionType();

                    ct.setConnectionTypeId(getIntVal(conn, "connectionTypeID"));
                    ct.setAmps(getIntVal(conn, "amps"));
                    ct.setVoltage(getIntVal(conn, "voltage"));
                    ct.setPowerKW(getDoubleVal(conn, "powerKW"));
                    ct.setName(getStringVal(conn, "name"));
                    ct.setCurrentType(getStringVal(conn, "currentType"));
                    ct.setQuantity(getIntVal(conn, "quantity"));

                    // add connections to charging station
                    chs.getConnections().add(ct);
                }

                //insert station to poi
                poi.setStation(chs);
                pois.add(poi);
                System.out.println(++counter);
            }

            return pois;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getIntVal(JSONObject obj, String attr){
        try {
            return !obj.isNull(attr) ? obj.getInt(attr) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    private static double getDoubleVal(JSONObject obj, String attr){
        try {
            return !obj.isNull(attr) ? obj.getDouble(attr) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    private static String getStringVal(JSONObject obj, String attr){
        try {
            return !obj.isNull(attr) ? obj.getString(attr) : "";
        } catch (JSONException e) {
            return "";
        }
    }

    private static JSONObject getJsonObject(JSONObject obj, String attr){
        try {
            return !obj.isNull(attr) ? obj.getJSONObject(attr) : null;
        } catch (JSONException e) {
            return null;
        }
    }
}
