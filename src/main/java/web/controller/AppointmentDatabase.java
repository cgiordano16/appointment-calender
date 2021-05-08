package Calendar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppointmentDatabase {

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
	
	public void init() throws IOException {
		deserializeDBFromDisk();
	}
	
	// This function is synchronized to restrict updates to one user request at a time
	public synchronized void update(LocalDateTime appointmentTime, Appointment appointment) throws IOException, SlotTakenException {
		
		if (appointmentDB.containsKey(appointmentTime.toString())) {
			Appointment takenAppointment = find(appointmentTime);
			String excpText = String.format("Appointment at %s is occupied by %s", appointmentTime.toString(), takenAppointment.getDisplayText());
			throw new SlotTakenException(excpText);
		}
		
		appointmentDB.put(appointmentTime.toString(), appointment);		
		serializeDBToDisk();
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
	
	public List<Appointment> getAllFoMonth(int monthValue) {
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
	
	void deserializeDBFromDisk() throws IOException {
		byte[] dbContents = Files.readAllBytes(pathToDbFile);
		loadHashMap(new String(dbContents));
	}
	
	void serializeDBToDisk() throws IOException {
		String jsonAppointmentData = gson.toJson(appointmentDB, type);
		Files.write(pathToDbFile, jsonAppointmentData.getBytes());
	}
}
