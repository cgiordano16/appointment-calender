package web.application;

import java.util.TreeSet;

public class JsonCalendarDate implements Comparable<JsonCalendarDate>{
    private int month;
    private int day;
    private int year;
    TreeSet<TimeSlot> time_slots = new TreeSet<>();

    JsonCalendarDate(int month, int day, int year){
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public TreeSet<TimeSlot> getTimeSlots() {
        return time_slots;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void addTimeSlot(TimeSlot new_slot){
        this.time_slots.add(new_slot);
    }

    public void removeTimeSlot(TimeSlot slot){
        this.time_slots.remove(slot);
    }

    @Override
    public int compareTo(JsonCalendarDate o){
        if(this.year != o.year){
            return this.year - o.year;
        }
        else if(this.month != o.month) {
            return this.month - o.month;
        }
        else {
            return this.day - o.day;
        }
    }

    @Override
    public String toString(){
        return month + "/" + day + "/" + year + "\n" + time_slots.toString();
    }

} 