package web.controller;

import java.util.Calendar;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

public class HomeController implements IGTVGController {

	private static final String SESSION_DB = "db";
	private static final String SESSION_ERROR_STRING = "es";
	private static final String SESSION_CURRENT_MONTH = "cm";
	
	private static final String REQUEST_ACTION = "action";
	private static final String REQUEST_ACTION_APPOINTMENT = "update";
	private static final String REQUEST_ACTION_UPDATE_MONTH = "updateMonth";
	private static final String REQUEST_APPOINTMENT_NAME = "name";
	private static final String REQUEST_APPOINTMENT_TIMESTAMP = "ts";
	
    public HomeController() {
        super();
    }
    
    public void process(
            final HttpServletRequest request, final HttpServletResponse response,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
            throws Exception {
    	
    	WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
    	
    	HttpSession session = request.getSession(true);
    	Integer monthToDisplay;
    	
    	//Clear out any errors from previous pages
    	session.setAttribute(SESSION_ERROR_STRING, "");
    	
    	//If there is a month stored in session then use it else default to the current month
    	if (session.getAttribute(SESSION_CURRENT_MONTH) != null) {
    		monthToDisplay = (Integer) session.getAttribute(SESSION_CURRENT_MONTH);
    	} else {
    		monthToDisplay = LocalDateTime.now().getMonthValue();
    		session.setAttribute(SESSION_CURRENT_MONTH, monthToDisplay);
    	}
        
    	//Get the database object. Set up if necessary
    	AppointmentDatabase appointmentDB = setupDB(session);
    	
    	//Check to see if an appointment needs to be updated in the DB
		System.out.println("request action: " + (String) request.getParameter(REQUEST_ACTION));
		System.out.println("request name: " + (String) request.getParameter(REQUEST_APPOINTMENT_NAME));
		System.out.println("request ts: " + (String) request.getParameter(REQUEST_APPOINTMENT_TIMESTAMP));
    	if (request.getParameter(REQUEST_ACTION) != null) {
    		String actionToTake = (String) request.getParameter(REQUEST_ACTION);
    		if (actionToTake.equals(REQUEST_ACTION_APPOINTMENT)) {    			
    			processUpdateAppointment(request, ctx, appointmentDB);
    		}
    		
    		if (actionToTake.equals(REQUEST_ACTION_UPDATE_MONTH)) {
    			monthToDisplay = processUpdateMonthToDisplay(request, session);
    		}
    	}
    	
        CalendarMonth month = new CalendarMonth(monthToDisplay);
		List<Appointment> appointmentList = appointmentDB.getAllFoMonth(monthToDisplay);
		for (Appointment appointment : appointmentList) {
			month.addAppointmentDate(appointment.getDisplayText(), appointment.getStartingTime().getYear(), appointment.getStartingTime().getMonthValue(), appointment.getStartingTime().getDayOfMonth(), appointment.getStartingTime().getHour(), appointment.getStartingTime().getMinute());
		}

        ctx.setVariable("today", Calendar.getInstance());
        ctx.setVariable("week1", month.getWeek1());
        ctx.setVariable("week2", month.getWeek2());
        ctx.setVariable("week3", month.getWeek3());
        ctx.setVariable("week4", month.getWeek4());
        ctx.setVariable("week5", month.getWeek5());
        ctx.setVariable("week6", month.getWeek6());
        
        templateEngine.process("home", ctx, response.getWriter());
    }

	private Integer processUpdateMonthToDisplay(HttpServletRequest request, HttpSession session) {
		//Get the new month to be processed
		Integer newMonth = Integer.parseInt(request.getParameter(REQUEST_ACTION_UPDATE_MONTH));
		
		//Set the session variable for subsequent calls
		session.setAttribute(REQUEST_ACTION_UPDATE_MONTH, newMonth);
		
		return newMonth;
	}

	private void processUpdateAppointment(final HttpServletRequest request, WebContext ctx, AppointmentDatabase appointmentDB) {
		//Get the name on the appointment and the timestamp
		String name = (String) request.getParameter(REQUEST_APPOINTMENT_NAME);
		String timestampValue = (String) request.getParameter(REQUEST_APPOINTMENT_TIMESTAMP);
		
		LocalDateTime appointmentTime = LocalDateTime.parse(timestampValue);
		Appointment appointment = new Appointment(name, appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth(), appointmentTime.getHour(), appointmentTime.getMinute());
		
		try {
			appointmentDB.update(LocalDateTime.parse(timestampValue), appointment);
		} catch (IOException excpIO) {
			ctx.setVariable(SESSION_ERROR_STRING, excpIO.getMessage());
		} catch (SlotTakenException excpSlotTake) {
			ctx.setVariable(SESSION_ERROR_STRING, excpSlotTake.getMessage());
		}
	}


	private AppointmentDatabase setupDB(HttpSession session) throws IOException {
		//Get the instance of the DB. If none exists, read in data from disk (if any) and initialize the DB
    	AppointmentDatabase appointmentDB = (AppointmentDatabase) session.getAttribute(SESSION_DB);
    	if (appointmentDB == null) {
    		appointmentDB = new AppointmentDatabase();
    		
    		//Read in any existing appointments from disk
    		appointmentDB.init();
    		
    		//Store reference for future use
    		session.setAttribute(SESSION_DB, appointmentDB);
    	}
		return appointmentDB;
	}
}
