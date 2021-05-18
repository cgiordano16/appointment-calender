package web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class CalendarMonth {
	private Month appointmentMonth;
	private List<CalendarDate> appointmentDates;
	LocalDate startingDate;
	LocalDate lastDate;
	private Gson gson;
	private JsonObject officeHoursJson;
	private Map<DayOfWeek, JsonObject> officeHoursDays;
	private String officeHourMonthKey;
	private List<String> officeHourDaysList;

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
		if (appointmentDates.size() > 28) {
			return appointmentDates.subList(28, 35);
		}
		return Collections.emptyList();
	}
	
	public List<CalendarDate> getWeek6() {
		if (appointmentDates.size() > 35) {
			return appointmentDates.subList(35, 42);
		}
		return Collections.emptyList();
	}

	public CalendarMonth(int month) {
		Path pathToOhFile = Paths.get("src/main/resources/officeHours.json");
		gson = new Gson();
		String rawOhJson = deserializeOHFromDisk(pathToOhFile);
		officeHoursJson = new JsonParser().parse(rawOhJson).getAsJsonObject();
		officeHoursDays = getOhDaysForMonth(month);
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
			// if (DayOfWeek.equals(jsonDay)) {
			// 	appointmentDates.add(new CalendarDate(jsonDayObj));
			// }
			LocalDate appointmentDate = LocalDate.of(now.getYear(), appointmentMonth, i);
			appointmentDates.add(new CalendarDate(appointmentDate));
			if (officeHoursDays.containsKey(appointmentDate.getDayOfWeek())) {
				JsonObject dowObject = officeHoursDays.get(appointmentDate.getDayOfWeek());
				JsonElement startTimeElement = dowObject.get("StartTime");
				JsonElement endTimeElement = dowObject.get("EndTime");
				String startTimeValue = startTimeElement.getAsString();
				String endTimeValue = endTimeElement.getAsString();
				LocalTime startTime = LocalTime.parse(startTimeValue);
				LocalTime endTime = LocalTime.parse(endTimeValue);

				LocalDateTime startingAppt = LocalDateTime.of(appointmentDate, startTime);
				LocalDateTime endingAppt = LocalDateTime.of(appointmentDate, endTime);

				LocalDateTime apptTimeSlot = startingAppt;
				while(apptTimeSlot.isBefore(endingAppt)) {
					String apptTimeKey = apptTimeSlot.toString();
					
					addAppointmentDate(AppointmentDatabase.DEFAULT_APPOINTMENT_TEXT, apptTimeSlot.getYear(), apptTimeSlot.getMonthValue(), apptTimeSlot.getDayOfMonth(), apptTimeSlot.getHour(), apptTimeSlot.getMinute());
					apptTimeSlot = apptTimeSlot.plusMinutes(AppointmentDatabase.LENGTH_OF_APPOINTMENT);
				}
			}
		}
		
		// Pad blanks for last week
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
	
	public Map<DayOfWeek, JsonObject> getOhDaysForMonth(int month) {
		Month currentMonth = Month.of(month);
		Map<DayOfWeek, JsonObject> ohDays = new HashMap<>();
		for (String officeHoursMonthValue : officeHoursJson.keySet()) {
			Month officeHoursMonth = Month.valueOf(officeHoursMonthValue.toUpperCase());
			//Search to see if this specific month has office hours
			if (officeHoursMonth.equals(currentMonth)) {
				officeHourMonthKey = officeHoursMonthValue;
				JsonObject currentOhMonthJson = officeHoursJson.getAsJsonObject(officeHoursMonthValue);
				for (String officeHoursDayOfWeek : currentOhMonthJson.keySet()) {
					DayOfWeek ohDay = DayOfWeek.valueOf(officeHoursDayOfWeek.toUpperCase());
					ohDays.put(ohDay, currentOhMonthJson.getAsJsonObject(officeHoursDayOfWeek));
				}
			}
		}
		return ohDays;
	}

	String deserializeOHFromDisk(Path pathToOhFile) {
		String errorString = "";
		try {
			byte[] dbContents = Files.readAllBytes(pathToOhFile);
			return new String(dbContents);		
		} catch (IOException ex) {
			errorString = ex.getMessage();
		}
		
		return errorString;
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
