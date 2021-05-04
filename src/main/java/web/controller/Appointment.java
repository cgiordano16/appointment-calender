package web.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

public class Appointment implements Comparable<Appointment> {
	private String displayText;
	public String getDisplayText() { 
		return displayText;
	}
	
	private LocalDateTime startingTime;
	public LocalDateTime getStartingTime() {
		return startingTime;
	}
	
	public String getStartingTimeFormatted() {
		return String.format("%02d:%02d", startingTime.getHour(), startingTime.getMinute());
	}

	public Appointment(String displayText, int appointmentYear, int appointmentMonth, int appointmentDay, int appointmentHour, int appointmentMinute)
	{
		this.displayText = displayText;
		startingTime = LocalDateTime.of(appointmentYear, Month.of(appointmentMonth), appointmentDay,  appointmentHour,  appointmentMinute);
	}

	@Override
	public int hashCode() {
		return Objects.hash(displayText, startingTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Appointment other = (Appointment) obj;
		return Objects.equals(displayText, other.displayText) && Objects.equals(startingTime, other.startingTime);
	}

	@Override
	public int compareTo(Appointment o) {
		return startingTime.compareTo(o.getStartingTime());
	}
	
}
