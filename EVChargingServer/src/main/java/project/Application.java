package project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static DB db;

    public static void main(String[] args) {
        HttpClient http = new HttpClient();
        db = new DB();

        // retrieve charging station data from external api
        //http.sendGet(db);

        SpringApplication.run(Application.class, args);
    }

    public static boolean fetchEVChargingData(){

        return false;
    }
}