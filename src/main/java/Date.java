import java.util.List;
import java.util.ArrayList;

public class Date implements Comparable<Date>{
    private int month;
    private int day;
    private int year;
    List<TimeSlot> time_slots = new ArrayList<>();

    Date(int month, int day, int year){
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public void addTimeSlot(TimeSlot new_slot){
        this.time_slots.add(new_slot);
    }

    @Override
    public int compareTo(Date o){
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