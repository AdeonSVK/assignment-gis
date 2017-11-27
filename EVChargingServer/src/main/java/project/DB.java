package project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DB {

    private Connection conn;

    public DB(){
        try {
            String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=postgres";
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ResultSet test(){
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM Persons");
            //st.setInt(1, foovalue);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertPoi(POI poi){
        String SQLPoi = "INSERT INTO poi(name, town, address, way) VALUES (?,?,?,ST_SetSRID(ST_MakePoint(?, ?),4326))";
        String SQLChs = "INSERT INTO charging_station(poi_id, operationalstatus, usage, usagecost, operator, operatorweb, numberofpoints) VALUES (?,?,?,?,?,?,?)";
        String SQLC = "INSERT INTO connection(chs_id, name, connectiontypeid, quantity, amps, voltage, powerkw, currenttype) VALUES (?,?,?,?,?,?,?,?)";
        long id = 0;
        try {
            // inserting poi
            PreparedStatement pstmt = conn.prepareStatement(SQLPoi,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, poi.getName());
            pstmt.setString(2, poi.getTown());
            pstmt.setString(3, poi.getAddress());
            pstmt.setDouble(4, poi.getLongitude());
            pstmt.setDouble(5, poi.getLatitude());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
            }
            // inserting charging station
            PreparedStatement pstmt2 = conn.prepareStatement(SQLChs,
                    Statement.RETURN_GENERATED_KEYS);
            long id2 = 0;

            pstmt2.setLong(1, id);
            pstmt2.setString(2, poi.getStation().getOperationalStatus());
            pstmt2.setString(3, poi.getStation().getUsage());
            pstmt2.setString(4, poi.getStation().getUsageCost());
            pstmt2.setString(5, poi.getStation().getOperator());
            pstmt2.setString(6, poi.getStation().getOperatorWeb());
            pstmt2.setInt(7, poi.getStation().getNumberOfPoints());

            int affectedRows2 = pstmt2.executeUpdate();
            // check the affected rows
            if (affectedRows2 > 0) {
                // get the ID back
                ResultSet rs = pstmt2.getGeneratedKeys();
                if (rs.next()) {
                    id2 = rs.getLong(1);
                }
            }

            // inserting connections
            for(int i=0; i<poi.getStation().getConnections().size(); i++){
                PreparedStatement pstmt3 = conn.prepareStatement(SQLC,
                        Statement.RETURN_GENERATED_KEYS);
                long id3;

                pstmt3.setLong(1, id2);
                pstmt3.setString(2, poi.getStation().getConnections().get(i).getName());
                pstmt3.setInt(3, poi.getStation().getConnections().get(i).getConnectionTypeId());
                pstmt3.setInt(4, poi.getStation().getConnections().get(i).getQuantity());
                pstmt3.setInt(5, poi.getStation().getConnections().get(i).getAmps());
                pstmt3.setInt(6, poi.getStation().getConnections().get(i).getVoltage());
                pstmt3.setDouble(7, poi.getStation().getConnections().get(i).getPowerKW());
                pstmt3.setString(8, poi.getStation().getConnections().get(i).getCurrentType());

                int affectedRows3 = pstmt3.executeUpdate();
                // check the affected rows
                if (affectedRows3 > 0) {
                    // get the ID back
                    ResultSet rs = pstmt3.getGeneratedKeys();
                    if (rs.next()) {
                        id3 = rs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public POI createPOI(int id){
        try {
            PreparedStatement st = conn.prepareStatement("SELECT name,town,address,ST_X(way),ST_Y(way),operationalstatus,usage,usagecost,operator,operatorweb,numberofpoints,poi_id, chs.id FROM poi JOIN charging_station chs ON poi.id = chs.poi_id WHERE poi.id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            rs.next();

            int poi_id, chs_id;

            // get poi
            POI poi = new POI();
            poi.setName(rs.getString(1));
            poi.setTown(rs.getString(2));
            poi.setAddress(rs.getString(3));
            poi.setLongitude(rs.getDouble(4));
            poi.setLatitude(rs.getDouble(5));

            // get charging station
            ChargingStation chs = new ChargingStation();
            chs.setOperationalStatus(rs.getString(6));
            chs.setUsage(rs.getString(7));
            chs.setUsageCost(rs.getString(8));
            chs.setOperator(rs.getString(9));
            chs.setOperatorWeb(rs.getString(10));
            chs.setNumberOfPoints(rs.getInt(11));
            poi_id = rs.getInt(12);
            chs_id = rs.getInt(13);

            // get all connections for charging station
            PreparedStatement st2 = conn.prepareStatement("SELECT * FROM connection where chs_id = ?");
            st2.setInt(1, chs_id);
            ResultSet rs2 = st2.executeQuery();

            chs.setConnections(new ArrayList<ConnectionType>());

            while (rs2.next())
            {
                ConnectionType connection = new ConnectionType();
                connection.setName(rs2.getString(3));
                connection.setConnectionTypeId(rs2.getInt(4));
                connection.setQuantity(rs2.getInt(5));
                connection.setAmps(rs2.getInt(6));
                connection.setVoltage(rs2.getInt(7));
                connection.setPowerKW(rs2.getDouble(8));
                connection.setCurrentType(rs2.getString(9));
                chs.getConnections().add(connection);
            }

            poi.setStation(chs);


            rs2.close();
            st2.close();
            return poi;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<POI> getPOIs(String query, String filter){
        try {
            PreparedStatement st = conn.prepareStatement(query + filter);
            ResultSet rs = st.executeQuery();
            ArrayList<POI> pois = new ArrayList<POI>();

            while (rs.next())
            {
                pois.add(createPOI(rs.getInt(1)));
            }

            rs.close();
            st.close();

            return pois;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
