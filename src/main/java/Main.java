import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.google.gson.*;


public class Main{
    public static void main(String[] args) throws FileNotFoundException{
        //test code for Date structure
        TimeSlot new_timeslot = new TimeSlot(1400, 1500);
        TimeSlot new_timeslot2 = new TimeSlot(1600, 1700);
        Date new_date = new Date(4, 28);
        new_date.addTimeSlot(new_timeslot);
        new_date.addTimeSlot(new_timeslot2);
        System.out.println(new_date);
    }    
}