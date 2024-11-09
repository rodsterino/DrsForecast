package org.example.drsforecast;

public class HourlyData {
    private String hour;
    private int count;

    public HourlyData(String hour, int count) {
        this.hour = hour;
        this.count = count;
    }

    public String getHour() {
        return hour;
    }

    public int getCount() {
        return count;
    }
}

