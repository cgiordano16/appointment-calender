package web.controller;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class CalendarDateTest {

	@Test
	public void testDisplayDate() {
		LocalDate apptDate = LocalDate.of(20201, 5, 24);
		CalendarDate date1 = new CalendarDate(apptDate);
		assertEquals("24", date1.getDisplayDate());
		assertEquals(apptDate, date1.getCalendarDate());
		
		CalendarDate date2 = new CalendarDate(null);
		assertEquals(" ", date2.getDisplayDate());
		assertEquals(null, date2.getCalendarDate());
	}
	
	@Test
	public void testAddAppointment() {
		Appointment appointment = new Appointment("Test String", 2021, 5, 24, 13, 15);
		LocalDate apptDate = LocalDate.of(20201, 5, 24);
		
		CalendarDate date1 = new CalendarDate(apptDate);
		assertEquals(0, date1.getAppointList().size());
		assertEquals(0, date1.getAppointmentMap().keySet().size());
		
		date1.addAppointment(appointment);
		assertEquals(1, date1.getAppointList().size());
		assertEquals(1, date1.getAppointmentMap().keySet().size());
	}
}
