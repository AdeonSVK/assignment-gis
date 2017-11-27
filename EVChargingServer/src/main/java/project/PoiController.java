package project;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import static project.Application.db;

@RestController
public class PoiController {

    int count;

    @RequestMapping("/pois")
    public ArrayList<POI> pois(@RequestParam(value="mennekes", defaultValue="false") boolean mennekes,
                               @RequestParam(value="chademo", defaultValue="false") boolean chademo,
                               @RequestParam(value="ccs", defaultValue="false") boolean ccs,
                               @RequestParam(value="cee5", defaultValue="false") boolean cee5,
                               @RequestParam(value="cee75", defaultValue="false") boolean cee75,
                               @RequestParam(value="mennekest", defaultValue="false") boolean mennekest,
                               @RequestParam(value="iec", defaultValue="false") boolean iec,
                               @RequestParam(value="tesla", defaultValue="false") boolean tesla,
                               @RequestParam(value="j1772", defaultValue="false") boolean j1772,
                               @RequestParam(value="kwfrom", defaultValue="0") double kwfrom,
                               @RequestParam(value="kwto", defaultValue="0") double kwto){
        count = 0;

        String filter = " WHERE ( ";
        if(mennekes) filter += insertOr() +"connectionTypeId = 25";
        if(chademo) filter += insertOr() + "connectionTypeId = 2";
        if(ccs) filter += insertOr() +"connectionTypeId = 33";
        if(cee5) filter += insertOr() +"connectionTypeId = 17";
        if(cee75) filter += insertOr() +"connectionTypeId = 23";
        if(mennekest) filter += insertOr() +"connectionTypeId = 1036";
        if(iec) filter += insertOr() +"connectionTypeId = 35";
        if(tesla) filter += insertOr() +"connectionTypeId = 27";
        if(j1772) filter += insertOr() +"connectionTypeId = 1";
        filter += " )";

        if(kwfrom > 0)filter += " AND powerkw >= " + kwfrom;
        if(kwto > 0)filter += " AND powerkw <= " + kwto;

        String query = "SELECT DISTINCT poi.id FROM poi JOIN charging_station chs ON poi.id = chs.poi_id JOIN connection c ON c.chs_id = chs.id ";
        return db.getPOIs(query, filter);
    }


    // ============================================nearby============================================
    @RequestMapping("/nearby")
    public ArrayList<POI> nearby(@RequestParam(value="mennekes", defaultValue="false") boolean mennekes,
                               @RequestParam(value="chademo", defaultValue="false") boolean chademo,
                               @RequestParam(value="ccs", defaultValue="false") boolean ccs,
                               @RequestParam(value="cee5", defaultValue="false") boolean cee5,
                               @RequestParam(value="cee75", defaultValue="false") boolean cee75,
                               @RequestParam(value="mennekest", defaultValue="false") boolean mennekest,
                               @RequestParam(value="iec", defaultValue="false") boolean iec,
                               @RequestParam(value="tesla", defaultValue="false") boolean tesla,
                               @RequestParam(value="j1772", defaultValue="false") boolean j1772,
                               @RequestParam(value="kwfrom", defaultValue="0") double kwfrom,
                               @RequestParam(value="kwto", defaultValue="0") double kwto,
                               @RequestParam(value="lat") double lat,
                               @RequestParam(value="lng") double lng,
                               @RequestParam(value="radius") int radius){
        count = 0;

        String filter = " AND ( ";
        if(mennekes) filter += insertOr() +"connectionTypeId = 25";
        if(chademo) filter += insertOr() + "connectionTypeId = 2";
        if(ccs) filter += insertOr() +"connectionTypeId = 33";
        if(cee5) filter += insertOr() +"connectionTypeId = 17";
        if(cee75) filter += insertOr() +"connectionTypeId = 23";
        if(mennekest) filter += insertOr() +"connectionTypeId = 1036";
        if(iec) filter += insertOr() +"connectionTypeId = 35";
        if(tesla) filter += insertOr() +"connectionTypeId = 27";
        if(j1772) filter += insertOr() +"connectionTypeId = 1";
        filter += " )";

        if(kwfrom > 0)filter += " AND powerkw >= " + kwfrom;
        if(kwto > 0)filter += " AND powerkw <= " + kwto;

        String query = "SELECT DISTINCT poi.id FROM poi " +
                "JOIN charging_station chs ON poi.id = chs.poi_id " +
                "JOIN connection c ON c.chs_id = chs.id " +
                "CROSS JOIN ST_SetSRID(ST_MakePoint("+lng+", "+lat+"),4326) " +
                "WHERE ST_DISTANCE(poi.way::geography, st_setsrid::geography) < "+radius;

        return db.getPOIs(query, filter);
    }
    // ============================================along============================================
    @RequestMapping("/along")
    public ArrayList<POI> along(@RequestParam(value="mennekes", defaultValue="false") boolean mennekes,
                               @RequestParam(value="chademo", defaultValue="false") boolean chademo,
                               @RequestParam(value="ccs", defaultValue="false") boolean ccs,
                               @RequestParam(value="cee5", defaultValue="false") boolean cee5,
                               @RequestParam(value="cee75", defaultValue="false") boolean cee75,
                               @RequestParam(value="mennekest", defaultValue="false") boolean mennekest,
                               @RequestParam(value="iec", defaultValue="false") boolean iec,
                               @RequestParam(value="tesla", defaultValue="false") boolean tesla,
                               @RequestParam(value="j1772", defaultValue="false") boolean j1772,
                               @RequestParam(value="kwfrom", defaultValue="0") double kwfrom,
                               @RequestParam(value="kwto", defaultValue="0") double kwto,
                               @RequestParam(value="radius") int radius,
                               @RequestParam(value="road") String road ){
        count = 0;

        String filter = " AND ( ";
        if(mennekes) filter += insertOr() +"connectionTypeId = 25";
        if(chademo) filter += insertOr() + "connectionTypeId = 2";
        if(ccs) filter += insertOr() +"connectionTypeId = 33";
        if(cee5) filter += insertOr() +"connectionTypeId = 17";
        if(cee75) filter += insertOr() +"connectionTypeId = 23";
        if(mennekest) filter += insertOr() +"connectionTypeId = 1036";
        if(iec) filter += insertOr() +"connectionTypeId = 35";
        if(tesla) filter += insertOr() +"connectionTypeId = 27";
        if(j1772) filter += insertOr() +"connectionTypeId = 1";
        filter += " )";

        if(kwfrom > 0)filter += " AND powerkw >= " + kwfrom;
        if(kwto > 0)filter += " AND powerkw <= " + kwto;


        String query = "SELECT DISTINCT poi.id FROM poi " +
                "JOIN charging_station chs ON poi.id = chs.poi_id " +
                "JOIN connection c ON c.chs_id = chs.id " +
                "CROSS JOIN planet_osm_line d WHERE highway = 'motorway' " +
                "AND ref = '"+road+"' " +
                "AND ST_DISTANCE(poi.way::geography, d.way::geography) < "+radius;
        return db.getPOIs(query, filter);
    }
    // ============================================somewhere============================================
    @RequestMapping("/somewhere")
    public ArrayList<POI> somewhere(@RequestParam(value="mennekes", defaultValue="false") boolean mennekes,
                               @RequestParam(value="chademo", defaultValue="false") boolean chademo,
                               @RequestParam(value="ccs", defaultValue="false") boolean ccs,
                               @RequestParam(value="cee5", defaultValue="false") boolean cee5,
                               @RequestParam(value="cee75", defaultValue="false") boolean cee75,
                               @RequestParam(value="mennekest", defaultValue="false") boolean mennekest,
                               @RequestParam(value="iec", defaultValue="false") boolean iec,
                               @RequestParam(value="tesla", defaultValue="false") boolean tesla,
                               @RequestParam(value="j1772", defaultValue="false") boolean j1772,
                               @RequestParam(value="kwfrom", defaultValue="0") double kwfrom,
                               @RequestParam(value="kwto", defaultValue="0") double kwto,
                               @RequestParam(value="district") String district){
        count = 0;

        String filter = " AND ( ";
        if(mennekes) filter += insertOr() +"connectionTypeId = 25";
        if(chademo) filter += insertOr() + "connectionTypeId = 2";
        if(ccs) filter += insertOr() +"connectionTypeId = 33";
        if(cee5) filter += insertOr() +"connectionTypeId = 17";
        if(cee75) filter += insertOr() +"connectionTypeId = 23";
        if(mennekest) filter += insertOr() +"connectionTypeId = 1036";
        if(iec) filter += insertOr() +"connectionTypeId = 35";
        if(tesla) filter += insertOr() +"connectionTypeId = 27";
        if(j1772) filter += insertOr() +"connectionTypeId = 1";
        filter += " )";

        if(kwfrom > 0)filter += " AND powerkw >= " + kwfrom;
        if(kwto > 0)filter += " AND powerkw <= " + kwto;

        String query = "SELECT DISTINCT poi.id FROM planet_osm_polygon d " +
                "CROSS JOIN poi " +
                "JOIN charging_station chs ON poi.id = chs.poi_id " +
                "JOIN connection c ON c.chs_id = chs.id " +
                "WHERE (admin_level = '4' OR admin_level = '8') " +
                "AND ST_CONTAINS(d.way, poi.way) " +
                "AND d.name = '"+district+"'";
        return db.getPOIs(query, filter);
    }

    public String insertOr(){
        if(count == 0) {count++; return " ";}
        else return " OR ";
    }
}
