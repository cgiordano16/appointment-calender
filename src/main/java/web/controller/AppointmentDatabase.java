package web.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppointmentDatabase {

	public static final String DEFAULT_APPOINTMENT_TEXT = "<Available Slot>";
	public static final int LENGTH_OF_APPOINTMENT = 15;
	private Map<String, Appointment> appointmentDB;
	private Gson gson;
	Type type;
	Path pathToDbFile;
	
	public AppointmentDatabase() {
		appointmentDB = new HashMap<>();
		gson = new Gson();
		type = new TypeToken<Map<String, Appointment>>(){}.getType();
		pathToDbFile = Paths.get("src/main/resources/appointmentDB.json");
	}
	
	public String init() {
		return deserializeDBFromDisk();
	}
	
	// This function is synchronized to restrict updates to one user request at a time
	public synchronized String update(LocalDateTime appointmentTime, Appointment appointment) {
		if (appointmentDB.containsKey(appointmentTime.toString())) {
			Appointment takenAppointment = find(appointmentTime);
			if (!takenAppointment.getDisplayText().equals(DEFAULT_APPOINTMENT_TEXT)) {
				return String.format("Appointment at %s is occupied by %s", appointmentTime.toString(), takenAppointment.getDisplayText());
			}
		}
		
		appointmentDB.put(appointmentTime.toString(), appointment);		
		return serializeDBToDisk();
	}
	
	void loadHashMap(String inputJsonData) {
		appointmentDB = gson.fromJson(inputJsonData, type);
	}
	
	public List<Appointment> getAll() {
		return new ArrayList<Appointment>(appointmentDB.values());
	}
	
	public Appointment find(LocalDateTime appointmentTime) {
		if (appointmentDB.containsKey(appointmentTime.toString())) {
			return appointmentDB.get(appointmentTime.toString());
		}
		
		return null;
	}
	
	public List<Appointment> getAllForMonth(int monthValue) {
		return getAllForMonth(Month.of(monthValue));
	}
	
	public List<Appointment> getAllForMonth(Month inputMonth) {
		List<Appointment> allAppointmentsForMonth = new ArrayList<>();
		for (String appointmentTime : appointmentDB.keySet()) {
			LocalDateTime apptTime = LocalDateTime.parse(appointmentTime);
			Month appointmentMonth = apptTime.getMonth();
			if (appointmentMonth == inputMonth) {
				Appointment appointment = appointmentDB.get(appointmentTime);
				allAppointmentsForMonth.add(appointment);
			}
		}
	
		return allAppointmentsForMonth;
	}

	public String createAppointments(LocalDate apptDate, int startHour, int startMinute, int endingHour, int endingMinute) {
		
		LocalTime startingTime = LocalTime.of(startHour, startMinute);
		LocalDateTime startingAppt = LocalDateTime.of(apptDate, startingTime);
		
		// Skip Sat and Sun
		if (startingAppt.getDayOfWeek() == DayOfWeek.SUNDAY || startingAppt.getDayOfWeek() == DayOfWeek.SATURDAY) {
			return "";
		}

		LocalTime endingTime = LocalTime.of(endingHour, endingMinute);
		LocalDateTime endingAppt = LocalDateTime.of(apptDate, endingTime);
		
		LocalDateTime apptTimeSlot = startingAppt;
		while(apptTimeSlot.isBefore(endingAppt)) {
			String apptTimeKey = apptTimeSlot.toString();
			
			// If there is an existing appointment for this slot, skip it
			if (appointmentDB.containsKey(apptTimeKey)) {
				continue;
			}
			
			appointmentDB.put(apptTimeKey, new Appointment(DEFAULT_APPOINTMENT_TEXT, apptTimeSlot.getYear(), apptTimeSlot.getMonthValue(), apptTimeSlot.getDayOfMonth(), apptTimeSlot.getHour(), apptTimeSlot.getMinute()));
			apptTimeSlot = apptTimeSlot.plusMinutes(LENGTH_OF_APPOINTMENT);
		}
		
		return serializeDBToDisk();
	}
	
	String deserializeDBFromDisk() {
		String errorString = "";
		try {
			byte[] dbContents = Files.readAllBytes(pathToDbFile);
			loadHashMap(new String(dbContents));			
		} catch (IOException ex) {
			errorString = ex.getMessage();
		}
		
		return errorString;
	}
	
	String serializeDBToDisk() {
		String errorString = "";
		try {
			String jsonAppointmentData = gson.toJson(appointmentDB, type);
			Files.write(pathToDbFile, jsonAppointmentData.getBytes());
		} catch (IOException ex) {
			errorString = ex.getMessage();
		}
		
		return errorString;
	}
}
