package web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CalendarDate {
	private LocalDate calendarDate;
	private String displayDate;
	
	private int startingHour = 14;
	private int startingMinute = 00;
	
	private int endingHour = 17;
	private int endingMinute = 00;
	
	private int lengthOfAppointment = 15;
	
	private Map<LocalDateTime, Appointment> appointmentMap;
	public List<Appointment> getAppointList() {
		return new ArrayList<Appointment>(appointmentMap.values());
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
			createAppointments();
		}
	}
	
	private void createAppointments() {
	
		LocalTime startingTime = LocalTime.of(startingHour, startingMinute);
		LocalDateTime startingAppt = LocalDateTime.of(calendarDate, startingTime);
		
		// Skip Sat and Sun
		if (startingAppt.getDayOfWeek() == DayOfWeek.SUNDAY || startingAppt.getDayOfWeek() == DayOfWeek.SATURDAY) {
			return;
		}
		
		LocalTime endingTime = LocalTime.of(endingHour, endingMinute);
		LocalDateTime endingAppt = LocalDateTime.of(calendarDate, endingTime);
		
		LocalDateTime apptTimeSlot = startingAppt;
		while(apptTimeSlot.isBefore(endingAppt)) {
			appointmentMap.put(apptTimeSlot, new Appointment("Available Slot", apptTimeSlot.getYear(), apptTimeSlot.getMonthValue(), apptTimeSlot.getDayOfMonth(), apptTimeSlot.getHour(), apptTimeSlot.getMinute()));			
			apptTimeSlot = apptTimeSlot.plusMinutes(lengthOfAppointment);
		}
	}

	public void addAppointment(Appointment appointment) {
		if (calendarDate != null) {
			appointmentMap.put(appointment.getStartingTime(), appointment);
		}
	}
}
