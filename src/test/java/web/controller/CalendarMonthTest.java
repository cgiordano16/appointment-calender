package web.controller;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonObject;

public class CalendarMonthTest {

	@Test
	public void test() {
		CalendarMonth calMonth = new CalendarMonth(5);
		
		// May has 6 "Calendar" weeks
		assertTrue(calMonth.getWeek1().size() > 0);
		assertTrue(calMonth.getWeek2().size() > 0);
		assertTrue(calMonth.getWeek3().size() > 0);
		assertTrue(calMonth.getWeek4().size() > 0);
		assertTrue(calMonth.getWeek5().size() > 0);
		assertTrue(calMonth.getWeek6().size() > 0);
		
		// June has 5 "Calendar" weeks
		calMonth = new CalendarMonth(6);
		assertTrue(calMonth.getWeek1().size() > 0);
		assertTrue(calMonth.getWeek2().size() > 0);
		assertTrue(calMonth.getWeek3().size() > 0);
		assertTrue(calMonth.getWeek4().size() > 0);
		assertTrue(calMonth.getWeek5().size() > 0);
		assertTrue(calMonth.getWeek6().isEmpty());
		
		calMonth.addAppointmentDate("Test Text", 2021, 6, 8, 10, 30);
		
		CalendarDate june8th = calMonth.getWeek2().get(2);
		assertEquals(1, june8th.getAppointList().size());

		CalendarDate june7th = calMonth.getWeek2().get(1);
		assertEquals(0 , june7th.getAppointList().size());
		
		 calMonth = new CalendarMonth(4);
		 Map<DayOfWeek, JsonObject> officeDays = calMonth.getOhDaysForMonth(4);
		 assertEquals(2, officeDays.keySet().size());
	}

}
