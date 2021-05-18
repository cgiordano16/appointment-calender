package web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CalendarDate {
	private LocalDate calendarDate;
	private String displayDate;
	
	public static final int lengthOfAppointment = 15;
	
	private Map<LocalDateTime, Appointment> appointmentMap;

	public Map<LocalDateTime, Appointment> getAppointmentMap() {
		return appointmentMap;
	}

	public List<Appointment> getAppointList() {
		List<Appointment> apptList = new ArrayList<Appointment>(appointmentMap.values());
		Collections.sort(apptList);
		return apptList;
	}
	
	public LocalDate getCalendarDate() {
		return calendarDate;
	}
	
	public String getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}
	
	public CalendarDate(LocalDate calendarDate) {
		this.calendarDate = calendarDate;
		
		// LinkedHashMap maintains the order the objects are inserted
		// Since this map will be populated in sorted order, any updates to the map will maintain the order
		appointmentMap = new LinkedHashMap<>();
		if (calendarDate == null) {
			displayDate = " ";
		} else {
			displayDate = String.format("%s", calendarDate.getDayOfMonth());
		}
	}

	public void addAppointment(Appointment appointment) {
		if (calendarDate != null) {
			appointmentMap.put(appointment.getStartingTime(), appointment);
		}
	}
}
