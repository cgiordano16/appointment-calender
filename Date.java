import java.util.List;
import java.util.ArrayList;

public class Date{
    private int month;
    private int day;
    List<TimeSlot> time_slots = new ArrayList<>();

    Date(int month, int day){
        this.month = month;
        this.day = day;
    }

    public void addTimeSlot(TimeSlot new_slot){
        this.time_slots.add(new_slot);
    }

    @Override
    public String toString(){
        return time_slots.toString();
    }

} 