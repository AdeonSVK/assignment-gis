package sk.fiit.xrackol.evcharging.java;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sk.fiit.xrackol.evcharging.MainActivity;
import sk.fiit.xrackol.evcharging.UserLocation;

import static sk.fiit.xrackol.evcharging.MainActivity.location;

public class Settings {

    public static Settings settings;
    public static boolean filterChanged = false;
    public static boolean searchApplied = false;

    private String searchType;
    private int distance;
    private String objectName;

    private boolean mennekes;
    private boolean chademo;
    private boolean ccs;
    private boolean cee5;
    private boolean cee75;
    private boolean mennekest;
    private boolean iec;
    private boolean tesla;
    private boolean j1772;
    private double kwFrom;
    private double kwTo;

    public Settings(){

    }
    public Settings(boolean all){
        if(all){
            this.mennekes = true;
            this.chademo = true;
            this.ccs = true;
            this.cee5 = true;
            this.cee75 = true;
            this.mennekest = true;
            this.iec = true;
            this.tesla = true;
            this.j1772 = true;
        }
    }


    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isMennekest() {
        return mennekest;
    }

    public void setMennekest(boolean mennekest) {
        this.mennekest = mennekest;
    }

    public boolean isMennekes() {
        return mennekes;
    }

    public void setMennekes(boolean mennekes) {
        this.mennekes = mennekes;
    }

    public boolean isChademo() {
        return chademo;
    }

    public void setChademo(boolean chademo) {
        this.chademo = chademo;
    }

    public boolean isCcs() {
        return ccs;
    }

    public void setCcs(boolean ccs) {
        this.ccs = ccs;
    }

    public boolean isCee5() {
        return cee5;
    }

    public void setCee5(boolean cee5) {
        this.cee5 = cee5;
    }

    public boolean isCee75() {
        return cee75;
    }

    public void setCee75(boolean cee75) {
        this.cee75 = cee75;
    }

    public boolean ismennekest() {
        return mennekest;
    }

    public void setmennekest(boolean mennekest) {
        this.mennekest = mennekest;
    }

    public boolean isIec() {
        return iec;
    }

    public void setIec(boolean iec) {
        this.iec = iec;
    }

    public boolean isTesla() {
        return tesla;
    }

    public void setTesla(boolean tesla) {
        this.tesla = tesla;
    }

    public boolean isJ1772() {
        return j1772;
    }

    public void setJ1772(boolean j1772) {
        this.j1772 = j1772;
    }

    public double getKwFrom() {
        return kwFrom;
    }

    public void setKwFrom(double kwFrom) {
        this.kwFrom = kwFrom;
    }

    public double getKwTo() {
        return kwTo;
    }

    public void setKwTo(double kwTo) {
        this.kwTo = kwTo;
    }

    public static String createFilterParams(){
        String filterParams = "";

        if(Settings.settings.mennekes) filterParams += "?mennekes=true"; else filterParams += "?mennekes=false";
        if(Settings.settings.chademo) filterParams += "&chademo=true"; else filterParams += "&chademo=false";
        if(Settings.settings.ccs) filterParams += "&ccs=true"; else filterParams += "&ccs=false";
        if(Settings.settings.cee5) filterParams += "&cee5=true"; else filterParams += "&cee5=false";
        if(Settings.settings.cee75) filterParams += "&cee75=true"; else filterParams += "&cee75=false";
        if(Settings.settings.mennekest) filterParams += "&mennekest=true"; else filterParams += "&mennekest=false";
        if(Settings.settings.iec) filterParams += "&iec=true"; else filterParams += "&iec=false";
        if(Settings.settings.tesla) filterParams += "&tesla=true"; else filterParams += "&tesla=false";
        if(Settings.settings.j1772) filterParams += "&j1772=true"; else filterParams += "&j1772=false";
        if(Settings.settings.kwFrom > 0) filterParams += "&kwfrom="+Settings.settings.kwFrom; else filterParams += "&kwfrom=0";
        if(Settings.settings.kwTo > 0) filterParams += "&kwto="+Settings.settings.kwTo; else filterParams += "&kwto=0";

        if(settings.searchType != null && settings.searchType.equals("nearby")){
            filterParams += "&lat="+ MainActivity.location.myLocation.getLatitude();
            filterParams += "&lng="+ MainActivity.location.myLocation.getLongitude();
            filterParams += "&radius="+ settings.distance;
        }
        if(settings.searchType != null && settings.searchType.equals("along")){
            filterParams += "&radius="+ settings.distance;
            filterParams += "&road="+ settings.objectName;
        }
        if(settings.searchType != null && settings.searchType.equals("somewhere")){
            filterParams += "&district="+ settings.objectName;
        }

        return filterParams;
    }
}
