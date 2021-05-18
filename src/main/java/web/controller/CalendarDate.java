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
			// createAppointments();
		}
	}
	
	// public static String createAppointments(AppointmentDatabase appointmentDb, LocalDate calendarDate, int startHour, int startMinute, int endingHour, int endingMinute) {
	// 	LocalTime startingTime = LocalTime.of(startHour, startMinute);
	// 	LocalDateTime startingAppt = LocalDateTime.of(calendarDate, startingTime);
		
	// 	// Skip Sat and Sun
	// 	if (startingAppt.getDayOfWeek() == DayOfWeek.SUNDAY || startingAppt.getDayOfWeek() == DayOfWeek.SATURDAY) {
	// 		return "";
	// 	}
		
	// 	LocalTime endingTime = LocalTime.of(endingHour, endingMinute);
	// 	LocalDateTime endingAppt = LocalDateTime.of(calendarDate, endingTime);
		
	// 	LocalDateTime apptTimeSlot = startingAppt;
	// 	Appointment appt = appointmentDb.find(apptTimeSlot);
	// 	LocalDateTime ldt = appt.getStartingTime();
	// 	Map<LocalDateTime, Appointment> apptMap = ldt.getAppointmentMap();
	// 	while(apptTimeSlot.isBefore(endingAppt)) {
	// 		Appointment appt = new Appointment("<Available Slot>", apptTimeSlot.getYear(), apptTimeSlot.getMonthValue(), apptTimeSlot.getDayOfMonth(), apptTimeSlot.getHour(), apptTimeSlot.getMinute());
	// 		String errorString = appointmentDb.update(apptTimeSlot, appt);
	// 		if (!errorString.isEmpty()) {
	// 			return errorString;
	// 		}
	// 		apptMap.put(apptTimeSlot, appt);
	// 		apptTimeSlot = apptTimeSlot.plusMinutes(CalendarDate.lengthOfAppointment);
	// 		return "";
	// 	}
	// }

	public void addAppointment(Appointment appointment) {
		if (calendarDate != null) {
			appointmentMap.put(appointment.getStartingTime(), appointment);
		}
	}
}
