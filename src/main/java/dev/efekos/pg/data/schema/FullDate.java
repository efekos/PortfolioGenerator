package dev.efekos.pg.data.schema;

public class FullDate {
    private int day;
    private int month;
    private int year;
    private int minute;
    private int hour;
    private int second;

    public FullDate(int day, int month, int year, int minute, int hour, int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.minute = minute;
        this.hour = hour;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
