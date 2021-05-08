package web.controller;

import java.util.Calendar;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.io.*;
import web.application.JsonCalendarDate;
import web.application.TimeSlot;
import com.google.gson.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

public class HomeController implements IGTVGController {

    
    public HomeController() {
        super();
    }
    
    
    public void process(
            final HttpServletRequest request, final HttpServletResponse response,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
            throws Exception {
        
        List<JsonCalendarDate> inputDates = getCalendarDates();
        System.out.println(inputDates);
        CalendarMonth month = new CalendarMonth(5);
        updateMonth(month, inputDates);
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("today", Calendar.getInstance());
        ctx.setVariable("week1", month.getWeek1());
        ctx.setVariable("week2", month.getWeek2());
        ctx.setVariable("week3", month.getWeek3());
        ctx.setVariable("week4", month.getWeek4());
        // List<String> lastWeek = getDates(25, 30);
        // lastWeek.add(" ");
        ctx.setVariable("week5", month.getWeek5());
        ctx.setVariable("week6", month.getWeek6());
        
        templateEngine.process("home", ctx, response.getWriter());
        
    }

     private static List<JsonCalendarDate> getCalendarDates() throws FileNotFoundException, IOException{
        final Gson gson = new Gson();
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
        return calendar_input;
     }

     private static void updateMonth(CalendarMonth month, List<JsonCalendarDate> dates) {
        for (JsonCalendarDate inputDate : dates) {
            int year = inputDate.getYear();
            int monthValue = inputDate.getMonth();
            int day = inputDate.getDay();
            TreeSet<TimeSlot> slots = inputDate.getTimeSlots();
            for (TimeSlot slot : slots) {
                String inputText = slot.getStudent();
                int startHour = (int) slot.getStart()/100;
                int startMinute = (int) slot.getStart()%100;
                int endHour = (int) slot.getEnd()/100;
                int endMinute = (int) slot.getEnd()%100;
                month.addAppointmentDate(inputText, year, monthValue, day, startHour, startMinute);
            }
        }
     }
}
