package org.example.drsforecast;

public class RowData {
    private String hour;
    private int monCount, tueCount, wedCount, thuCount, friCount, satCount, sunCount;

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

    public String getHour() { return hour; }
    public int getMonCount() { return monCount; }
    public int getTueCount() { return tueCount; }
    public int getWedCount() { return wedCount; }
    public int getThuCount() { return thuCount; }
    public int getFriCount() { return friCount; }
    public int getSatCount() { return satCount; }
    public int getSunCount() { return sunCount; }
}
