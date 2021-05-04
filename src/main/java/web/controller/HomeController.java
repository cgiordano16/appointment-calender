package web.controller;

import java.util.Calendar;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
        
        CalendarMonth month = new CalendarMonth(5);
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

    public static List<String> getDates(int beginDay, int lastDay) {
        LocalDate today = LocalDate.now();
        List<String> days = new ArrayList<>();
        int currentMonth = today.getMonthValue();
        LocalDate beginDate = LocalDate.of(today.getYear(), currentMonth, beginDay);
        if (beginDate.getDayOfWeek() == DayOfWeek.THURSDAY) {
            days.add(" ");
            days.add(" ");
            days.add(" ");
            days.add(" ");
        }
        for (int i = beginDay; i <= lastDay; i++) {
            LocalDate day = LocalDate.of(today.getYear(), currentMonth, i);
            days.add(String.format("%s", day.getDayOfMonth()));
        }
        return days;
    }
}
