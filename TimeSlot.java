public class TimeSlot{
    private int start; //start time in military time 
    private int end;   //end time in military time
    private boolean taken; //indicates if timeslot is taken
    private String student; //name of student if taken (empty if free)

    TimeSlot(int start, int end){
        this.start = start;
        this.end = end;
        this.taken = false;
        this.student = "";
    }

    public void setTaken(String student_name, boolean taken){
        this.taken = taken;
        this.student = student_name;
    }

    @Override
    public String toString(){
        return start + " - " + end;
    }
}