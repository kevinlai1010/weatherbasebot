package ubibots.weatherbase.model;

import java.util.ArrayList;

public class BeanTabMessage {
    public int count = 0;
    private ArrayList<Double> temperature;
    private ArrayList<Double> humidity;
    private ArrayList<Double> air;
    private ArrayList<String> date;

    public BeanTabMessage(ArrayList<Double> temperature, ArrayList<Double> humidity, ArrayList<Double> air, ArrayList<String> date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.air = air;
        this.date = date;
    }

    public ArrayList<Double> getTemperature() {
        return temperature;
    }

    public ArrayList<Double> getHumidity() {
        return humidity;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public ArrayList<Double> getAir() {
        return air;
    }
}
