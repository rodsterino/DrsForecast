package org.example.drsforecast;

public class RowData {

    private String hour;
    private int monCount;
    private int tueCount;
    private int wedCount;
    private int thuCount;
    private int friCount;
    private int satCount;
    private int sunCount;

    public RowData(String hour, int monCount, int tueCount, int wedCount, int thuCount,
                   int friCount, int satCount, int sunCount) {
        this.hour = hour;
        this.monCount = monCount;
        this.tueCount = tueCount;
        this.wedCount = wedCount;
        this.thuCount = thuCount;
        this.friCount = friCount;
        this.satCount = satCount;
        this.sunCount = sunCount;
    }

    // Getters
    public String getHour() {
        return hour;
    }

    public int getMonCount() {
        return monCount;
    }

    public int getTueCount() {
        return tueCount;
    }

    public int getWedCount() {
        return wedCount;
    }

    public int getThuCount() {
        return thuCount;
    }

    public int getFriCount() {
        return friCount;
    }

    public int getSatCount() {
        return satCount;
    }

    public int getSunCount() {
        return sunCount;
    }
}
