package web.application;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.*;


public class Main{
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        //test code for Date structure
        JsonCalendarDate new_date = new JsonCalendarDate(4, 28, 2021);
        JsonCalendarDate new_date2 = new JsonCalendarDate(4, 29, 2021);
        
        new_date2.addTimeSlot(new TimeSlot(1400, 1500));
        new_date2.addTimeSlot(new TimeSlot(1600, 1700));
        
        new_date.addTimeSlot(new TimeSlot(1400, 1500));
        new_date.addTimeSlot(new TimeSlot(1600, 1700));
        new_date.addTimeSlot(new TimeSlot(1700, 1730));
        new_date.addTimeSlot(new TimeSlot(1200, 1230));
         
        List<JsonCalendarDate> calendar_output = new ArrayList<>(); 
        calendar_output.add(new_date2);
        calendar_output.add(new_date);
        Collections.sort(calendar_output);

        for(JsonCalendarDate date : calendar_output){
            System.out.println(date);
        }

        //write list of dates to json file, one date per line
        final Gson gson = new Gson();
        BufferedWriter writer = new BufferedWriter(new FileWriter("json_objects.json"));
        for (JsonCalendarDate date : calendar_output){
            gson.toJson(date, writer);
            writer.newLine();
        }        
        writer.close();

        //Read File back as a list of lines
        List<String> json_input = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("calendar.json"));
        
        String line; 
        while ((line = reader.readLine()) != null) {
          json_input.add(line);
        }

        reader.close();
        //remove any empty lines that were read
        json_input.removeIf(String::isEmpty);
        
        //convert list of jsons to list of date objects 
        List<JsonCalendarDate> calendar_input = new ArrayList<>();
        for(String date_json : json_input){
            calendar_input.add(gson.fromJson(date_json, JsonCalendarDate.class));
        }

        //sort new calendar in case date entries were out of order in json file
        Collections.sort(calendar_input);

        //confirm file was read correctly by printing each date
        for(JsonCalendarDate date : calendar_input){  
            System.out.println(date);
        }

    }    
}