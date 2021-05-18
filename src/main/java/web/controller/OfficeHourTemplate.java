package web.controller; 

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public class OfficeHourTemplate {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public OfficeHourTemplate parse(String inputString) {
        String[] ohInputs = inputString.split(" ");
        OfficeHourTemplate newTemplate = new OfficeHourTemplate();
        DayOfWeek dayToEnum = DayOfWeek.valueOf(ohInputs[0].toUpperCase());
        newTemplate.setDayOfWeek(dayToEnum);
        newTemplate.setStartTime(LocalTime.parse(ohInputs[1]));
        newTemplate.setEndTime(LocalTime.parse(ohInputs[2]));
        return newTemplate;
    }
    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, endTime, startTime);
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
        OfficeHourTemplate other = (OfficeHourTemplate) obj;
        return dayOfWeek == other.dayOfWeek && Objects.equals(endTime, other.endTime)
                && Objects.equals(startTime, other.startTime);
    }
    @Override
    public String toString() {
    return "OfficeHourTemplate [dayOfWeek=" + dayOfWeek + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }

}
