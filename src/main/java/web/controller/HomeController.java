package web.controller;

import java.util.Calendar;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Month;
import java.time.LocalDate;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

public class HomeController implements ICalendarApptController {

	private static final String SESSION_DB = "db";
	private static final String SESSION_ERROR_STRING = "es";
	private static final String SESSION_CURRENT_MONTH = "cm";
	private static final String SESSION_CURRENT_MONTH_DISPLAY = "cmd";
	
	private static final String REQUEST_ACTION = "action";
	private static final String REQUEST_ACTION_APPOINTMENT = "update";
	private static final String REQUEST_ACTION_MAKE_APPT = "build";
	private static final String REQUEST_ACTION_UPDATE_MONTH = "updateMonth";
	private static final String REQUEST_ACTION_UPDATE_MONTH_NEWMONTH = "newMonth";
	private static final String REQUEST_APPOINTMENT_NAME = "name";
	private static final String REQUEST_APPOINTMENT_TIMESTAMP = "ts";
	private static final String REQUEST_MAKE_APPT_START_HOUR = "sHour";
	private static final String REQUEST_MAKE_APPT_START_MINUTE = "sMinute";
	private static final String REQUEST_MAKE_APPT_END_HOUR = "eHour";
	private static final String REQUEST_MAKE_APPT_END_MINUTE = "eMinute";
	private static final String REQUEST_MAKE_APPT_DATE = "apptDate";
	
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
    	
    	monthToDisplay = processMonth(session);
        
    	//Get the database object. Set up if necessary
    	AppointmentDatabase appointmentDB = setupDB(session);
    	
    	if (appointmentDB != null) {
        	processCalendarAppointRequest(request, ctx, session, monthToDisplay, appointmentDB);
    	}
        
        templateEngine.process("home", ctx, response.getWriter());
    }

	private Integer processMonth(HttpSession session) {
		Integer monthToDisplay;
		//If there is a month stored in session then use it else default to the current month
    	if (session.getAttribute(SESSION_CURRENT_MONTH) != null) {
    		monthToDisplay = (Integer) session.getAttribute(SESSION_CURRENT_MONTH);
    	} else {
    		monthToDisplay = LocalDateTime.now().getMonthValue();
    		session.setAttribute(SESSION_CURRENT_MONTH, monthToDisplay);
    	}
		session.setAttribute(SESSION_CURRENT_MONTH_DISPLAY, Month.of(monthToDisplay).name());
		return monthToDisplay;
	}

	private void processCalendarAppointRequest(final HttpServletRequest request, WebContext ctx, HttpSession session,
	
		Integer monthToDisplay, AppointmentDatabase appointmentDB) {
		//Check to see if an appointment needs to be updated in the DB
    	if (request.getParameter(REQUEST_ACTION) != null) {
    		String actionToTake = (String) request.getParameter(REQUEST_ACTION);
    		if (actionToTake.equals(REQUEST_ACTION_APPOINTMENT)) {    			
    			processUpdateAppointment(request, ctx, appointmentDB, session);
    		}
    		
    		if (actionToTake.equals(REQUEST_ACTION_UPDATE_MONTH)) {
    			monthToDisplay = processUpdateMonthToDisplay(request, session);
    		}

			if (actionToTake.equals(REQUEST_ACTION_MAKE_APPT)) {
				processMakeAppointment(request, appointmentDB, session);
			}
    	}
    	
        CalendarMonth month = new CalendarMonth(monthToDisplay);
		List<Appointment> appointmentList = appointmentDB.getAllForMonth(monthToDisplay);
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
	}



	private Integer processUpdateMonthToDisplay(HttpServletRequest request, HttpSession session) {
		//Get the new month to be processed
		Integer newMonth = Integer.parseInt(request.getParameter(REQUEST_ACTION_UPDATE_MONTH_NEWMONTH));
		
		//Set the session variable for subsequent calls
		session.setAttribute(SESSION_CURRENT_MONTH, newMonth);
		session.setAttribute(SESSION_CURRENT_MONTH_DISPLAY, Month.of(newMonth).name());
		
		return newMonth;
	}

	private void processUpdateAppointment(final HttpServletRequest request, WebContext ctx, AppointmentDatabase appointmentDB, final HttpSession session) {
		//Get the name on the appointment and the timestamp
		String name = (String) request.getParameter(REQUEST_APPOINTMENT_NAME);
		String timestampValue = (String) request.getParameter(REQUEST_APPOINTMENT_TIMESTAMP);
		
		LocalDateTime appointmentTime = LocalDateTime.parse(timestampValue);
		Appointment appointment = new Appointment(name, appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth(), appointmentTime.getHour(), appointmentTime.getMinute());
		
		String errorString = appointmentDB.update(LocalDateTime.parse(timestampValue), appointment);
		session.setAttribute(SESSION_ERROR_STRING, errorString);
	}

	private void processMakeAppointment(final HttpServletRequest request, AppointmentDatabase appointmentDB, final HttpSession session) {
		int startHour = Integer.parseInt(request.getParameter(REQUEST_MAKE_APPT_START_HOUR));
		int startMinute = Integer.parseInt(request.getParameter(REQUEST_MAKE_APPT_START_MINUTE));
		int endHour = Integer.parseInt(request.getParameter(REQUEST_MAKE_APPT_END_HOUR));
		int endMinute = Integer.parseInt(request.getParameter(REQUEST_MAKE_APPT_END_MINUTE));
		LocalDate apptDate = LocalDate.parse(request.getParameter(REQUEST_MAKE_APPT_DATE));

		String errorString = appointmentDB.createAppointments(apptDate, startHour, startMinute, endHour, endMinute);
		session.setAttribute(SESSION_ERROR_STRING, errorString);
	}


	private AppointmentDatabase setupDB(HttpSession session) throws IOException {
		//Get the instance of the DB. If none exists, read in data from disk (if any) and initialize the DB
    	AppointmentDatabase appointmentDB = (AppointmentDatabase) session.getAttribute(SESSION_DB);
    	if (appointmentDB == null) {
    		appointmentDB = new AppointmentDatabase();
    		
    		//Read in any existing appointments from disk
    		String errorString =  appointmentDB.init();
    		session.setAttribute(SESSION_ERROR_STRING, errorString);
    		
    		if (!errorString.isEmpty()) {
    			return null;
    		}
    		
    		//Store reference for future use
    		session.setAttribute(SESSION_DB, appointmentDB);
    	}
		return appointmentDB;
	}
}
