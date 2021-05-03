import java.io.*;
import java.util.*;
import com.google.gson.*;


public class Main{
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        //test code for Date structure
        TimeSlot new_timeslot = new TimeSlot(1400, 1500);
        TimeSlot new_timeslot2 = new TimeSlot(1600, 1700);
        
        Date new_date = new Date(4, 28, 2021);
        Date new_date2 = new Date(4, 29, 2021);
        
        new_date2.addTimeSlot(new_timeslot);
        new_date2.addTimeSlot(new_timeslot2);
        
        new_date.addTimeSlot(new_timeslot);
        new_date.addTimeSlot(new_timeslot2);
         
        List<Date> calendar_output = new ArrayList<>(); 
        calendar_output.add(new_date2);
        calendar_output.add(new_date);
        Collections.sort(calendar_output);

        for(Date date : calendar_output){
            System.out.println(date);
        }

        //write list of dates to json file
        final Gson gson = new Gson();
        BufferedWriter writer = new BufferedWriter(new FileWriter("json_objects.json"));
        for (Date date : calendar_output){
            gson.toJson(date, writer);
            writer.newLine();
        }        
        writer.close();

        //Read File back
        List<String> json_input = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("calendar.json"));
        
        String line;
        while ((line = reader.readLine()) != null) {
          json_input.add(line);
        }

        reader.close();

        List<Date> calendar_input = new ArrayList<>();

        for(String date_json : json_input){
            calendar_input.add(gson.fromJson(date_json, Date.class));
        }

        Collections.sort(calendar_input);

        for(Date date : calendar_input){
            System.out.println(date);
        }

    }    
}