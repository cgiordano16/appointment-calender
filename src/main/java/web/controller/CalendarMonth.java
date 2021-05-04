package web.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class CalendarMonth {
	private Month appointmentMonth;
	private List<CalendarDate> appointmentDates;
	LocalDate startingDate;
	LocalDate lastDate;

	int firstWeekBlankDateCount;
	int lastWeekBlankDateCount;
	
	public List<CalendarDate> getWeek1() {
		return appointmentDates.subList(0, 7);
	}
	
	public List<CalendarDate> getWeek2() {
		return appointmentDates.subList(7, 14);
	}
		
	public List<CalendarDate> getWeek3() {
		return appointmentDates.subList(14, 21);
	}
	
	public List<CalendarDate> getWeek4() {
		return appointmentDates.subList(21, 28);
	}
	
	public List<CalendarDate> getWeek5() {
		return appointmentDates.subList(28, 35);
	}
	
	public List<CalendarDate> getWeek6() {
		return appointmentDates.subList(35, 42);
	}

	public CalendarMonth(int month) {
		appointmentMonth = Month.of(month);
		appointmentDates = new ArrayList<>();

		LocalDate now = LocalDate.now();
		startingDate = LocalDate.of(now.getYear(), appointmentMonth, 1);
		lastDate = LocalDate.of(now.getYear(), appointmentMonth, startingDate.lengthOfMonth());
		intializeMonth(now);
	}
	
	private void intializeMonth(LocalDate now) {
		firstWeekBlankDateCount = getBlankDatesForFirstWeek();
		
		// Pad blanks for first week
		for (int i = 0; i < firstWeekBlankDateCount; i++) {
			appointmentDates.add(new CalendarDate(null));
		}
		
		// Add dates for appointments
		for (int i = 1; i <= startingDate.lengthOfMonth(); i++) {
			LocalDate appointmentDate = LocalDate.of(now.getYear(), appointmentMonth, i);
			appointmentDates.add(new CalendarDate(appointmentDate));
		}
		
		// Pad blanks for first week		
		lastWeekBlankDateCount = getBlankDatesForLastWeek();
		for (int i = 0; i < lastWeekBlankDateCount; i++) {
			appointmentDates.add(new CalendarDate(null));
		}
	}

	private int getBlankDatesForLastWeek() {
		switch(lastDate.getDayOfWeek()) {
			case SUNDAY:
				return 6;
			case MONDAY:
				return 5;
			case TUESDAY:
				return 4;
			case WEDNESDAY:
				return 3;
			case THURSDAY:
				return 2;
			case FRIDAY:
				return 1;
			case SATURDAY:
				return 0;
		}
		return 0;
	}

	private int getBlankDatesForFirstWeek() {
		switch(startingDate.getDayOfWeek()) {
			case SUNDAY:
				return 0;
			case MONDAY:
				return 1;
			case TUESDAY:
				return 2;
			case WEDNESDAY:
				return 3;
			case THURSDAY:
				return 4;
			case FRIDAY:
				return 5;
			case SATURDAY:
				return 6;
		}
		
		return 0;
	}

	public void addAppointmentDate(String displayText, int appointmentYear, int appointmentMonth, int appointmentDay, int appointmentHour, int appointmentMinute) {
		int calendarIndex = appointmentDay + firstWeekBlankDateCount - 1;
		CalendarDate calendarDate = appointmentDates.get(calendarIndex);
		calendarDate.addAppointment(new Appointment(displayText, appointmentYear, appointmentMonth, appointmentDay, appointmentHour, appointmentMinute));
	}
	
	public void printMonth() {
		System.out.println(appointmentMonth + "/" + startingDate.getYear());
		System.out.println("' S' ' M' ' T' ' W' ' T' ' F' ' S'");
		printWeek(getWeek1());
		System.out.println();
		printWeek(getWeek2());
		System.out.println();
		printWeek(getWeek3());
		System.out.println();
		printWeek(getWeek4());
		System.out.println();
		printWeek(getWeek5());
		System.out.println();
		if (appointmentDates.size() > 35) {
			printWeek(getWeek6());
			System.out.println();
		}
	}
	
	public void printWeek(List<CalendarDate> datesInWeek) {
		for (CalendarDate dateInWeek : datesInWeek) {
			String output = String.format("'%2s' ", dateInWeek.getDisplayDate());
			System.out.print(output);
		}
	}
}
