import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;


public class CalendarReader{
    public static void main(String[] args) throws FileNotFoundException{
        TimeSlot new_timeslot = new TimeSlot(1400, 1500);
        Date new_date = new Date(4, 28);
        new_date.addTimeSlot(new_timeslot);
        System.out.println(new_date);
    }    
}