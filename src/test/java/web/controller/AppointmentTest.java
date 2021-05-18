package web.controller;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AppointmentTest {
	
	private Appointment appointment;
	
	@Before
	public void setup() {
		appointment = new Appointment("Test String", 2021, 5, 24, 13, 15);
	}

	@Test
	public void testGetDisplayText() {
		assertEquals("Test String", appointment.getDisplayText());
	}

	@Test
	public void testGetStartingTime() {
		LocalDateTime ldt = LocalDateTime.of(2021, 5, 24, 13, 15);
		assertEquals(ldt, appointment.getStartingTime());
	}

	@Test
	public void testGetStartingTimeFormatted() {
		assertEquals("13:15", appointment.getStartingTimeFormatted());
	}

	@Test
	public void testSort() {
		Appointment appointment2 = new Appointment("Test String", 2021, 5, 24, 12, 15);
		List<Appointment> appointmentList = new ArrayList<>();
		
		appointmentList.add(appointment);
		appointmentList.add(appointment2);
		
		// List is not sorted. First element is at 13:15, 2nd element is at 12:15
		assertFalse(appointmentList.get(0).compareTo(appointment2) < 0);
		
		Collections.sort(appointmentList);
		
		// Now the list is sorted
		assertTrue(appointmentList.get(0).compareTo(appointment) < 0);		
	}
}
