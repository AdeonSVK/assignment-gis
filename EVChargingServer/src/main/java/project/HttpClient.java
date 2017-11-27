package project;

import com.google.gson.Gson;
import com.sun.deploy.net.HttpResponse;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class HttpClient {

    private final String USER_AGENT = "Mozilla/5.0";

    // HTTP GET request
    public void sendGet(DB db) {

        StringBuffer response = null;
        try {
            String url = "https://api.openchargemap.io/v2/poi/?output=json&countrycode=SK";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse received JSON
        try {

            int counter = 0;

            JSONArray data = new JSONArray(response.toString());
            JSONObject station;
            POI poi;
            ArrayList<POI> pois = new ArrayList<POI>();
            for(int i=0; i < data.length(); i++){
                station = data.getJSONObject(i);

                // Fill POI data
                poi = new POI();
                JSONObject addressInfo = station.getJSONObject("AddressInfo");
                poi.setLatitude(getDoubleVal(addressInfo,"Latitude"));
                poi.setLongitude(getDoubleVal(addressInfo,"Longitude"));
                poi.setName(getStringVal(addressInfo,"Title"));
                poi.setTown(getStringVal(addressInfo,"Town"));
                poi.setAddress(getStringVal(addressInfo,"AddressLine1"));

                // fill CharginStationData
                ChargingStation chs = new ChargingStation();
                chs.setUsageCost(getStringVal(station, "UsageCost"));

                JSONObject statusType = station.getJSONObject("StatusType");
                chs.setOperationalStatus(getStringVal(statusType, "Title"));

                JSONObject usageType = station.getJSONObject("UsageType");
                chs.setUsage(getStringVal(usageType,"Title"));

                if (getJsonObject(station,"OperatorInfo") != null) {
                    JSONObject operatorInfo = station.getJSONObject("OperatorInfo");
                    chs.setOperator(getStringVal(operatorInfo,"Title"));
                    chs.setOperatorWeb(getStringVal(operatorInfo,"WebsiteURL"));
                }
                chs.setNumberOfPoints(getIntVal(station, "NumberOfPoints"));

                // Fill ConnectionType data
                JSONArray connections = station.getJSONArray("Connections");

                // create connections arraylist
                chs.setConnections(new ArrayList<ConnectionType>());

                for(int j=0; j < connections.length(); j++){
                    JSONObject conn = connections.getJSONObject(j);
                    ConnectionType ct = new ConnectionType();

                    ct.setConnectionTypeId(getIntVal(conn, "ConnectionTypeID"));
                    ct.setAmps(getIntVal(conn, "Amps"));
                    ct.setVoltage(getIntVal(conn, "Voltage"));
                    ct.setPowerKW(getDoubleVal(conn, "PowerKW"));

                    JSONObject connectionType = conn.getJSONObject("ConnectionType");
                    ct.setName(getStringVal(connectionType, "Title"));

                    if(getJsonObject(conn,"CurrentType") != null){
                        JSONObject currentType = getJsonObject(conn,"CurrentType");
                        ct.setCurrentType(getStringVal(currentType, "Title"));
                    }
                    ct.setQuantity(getIntVal(conn, "Quantity"));

                    // add connections to charging station object
                    chs.getConnections().add(ct);
                }

                //insert charging stations to POI
                poi.setStation(chs);
                pois.add(poi);
                System.out.println(++counter);
            }

            // save pois to DB
            for(int i=0; i<pois.size();i++){
                db.insertPoi(pois.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
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


