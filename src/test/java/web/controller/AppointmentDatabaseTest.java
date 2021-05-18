package web.controller;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AppointmentDatabaseTest {

	private AppointmentDatabase appointmentDB;
	
	@Before
	public void setup() {
		appointmentDB = new AppointmentDatabase();
	}
	
	@Test
	public void testInit() {
		assertEquals("", appointmentDB.init());		
	}

	@Test
	public void testUpdate() {
		LocalDateTime appointmentTime = LocalDateTime.of(2021, 5, 24, 9, 30);
		Appointment appointment = new Appointment("Test Appointment", appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth(), 9, 30);
		int initialDbSize = appointmentDB.getAll().size();
		appointmentDB.update(appointmentTime, appointment);
		int finalDbSize = appointmentDB.getAll().size();
		assertEquals(initialDbSize + 1, finalDbSize);
		
		//Update the DB with the same appointment, the DB size should not change
		String updateOutput = appointmentDB.update(appointmentTime, appointment);
		int dbSizeAfter = appointmentDB.getAll().size();
		assertEquals(finalDbSize, dbSizeAfter);
		
		// Check message from DB
		assertEquals("Appointment at 2021-05-24T09:30 is occupied by Test Appointment", updateOutput);
	}
	
	@Test
	public void testFind() {
		LocalDateTime appointmentTime = LocalDateTime.of(2021, 5, 24, 9, 30);
		assertNull(appointmentDB.find(appointmentTime));
		
		Appointment appointment = new Appointment("Test Appointment", appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth(), 9, 30);
		appointmentDB.update(appointmentTime, appointment);
		assertNotNull(appointmentDB.find(appointmentTime));
	}
	
	@Test
	public void testGetForMonth() {
		List<Appointment> appointmentsForJan = appointmentDB.getAllForMonth(Month.JANUARY);
		assertEquals(0, appointmentsForJan.size());
		
		LocalDateTime appointmentTime = LocalDateTime.of(2021, 1, 24, 9, 30);
		Appointment appointment = new Appointment("Jan Appointment", appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth(), 9, 30);
		appointmentDB.update(appointmentTime, appointment);
		
		appointmentsForJan = appointmentDB.getAllForMonth(Month.JANUARY);
		assertEquals(1, appointmentsForJan.size());
	}
	
	@Test
	public void testGetForMonthValue() {
		List<Appointment> appointmentsForJan = appointmentDB.getAllForMonth(1);
		assertEquals(0, appointmentsForJan.size());
		
		LocalDateTime appointmentTime = LocalDateTime.of(2021, 1, 24, 9, 30);
		Appointment appointment = new Appointment("Jan Appointment", appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth(), 9, 30);
		appointmentDB.update(appointmentTime, appointment);
		
		appointmentsForJan = appointmentDB.getAllForMonth(1);
		assertEquals(1, appointmentsForJan.size());
	}
	
	@Test
	public void testCreateAppointments() {
		LocalDate appointmentDate = LocalDate.of(2021, 5, 24);
		assertEquals(0, appointmentDB.getAll().size());
		String dbSaveOutput = appointmentDB.createAppointments(appointmentDate, 10, 0, 12, 0);
		assertEquals(8, appointmentDB.getAll().size());
		assertEquals("", dbSaveOutput);
	}
}
