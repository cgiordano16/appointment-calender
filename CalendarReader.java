import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;


public class CalendarReader{
    public static void main(String[] args) throws FileNotFoundException{
        Scanner file_scanner = new Scanner(new File("Calendar.txt"));
        while (file_scanner.hasNextLine()){
            String line = file_scanner.nextLine();
            line = line.replaceAll("\\s", "");
            System.out.println(line);
            Scanner line_scanner = new Scanner(line);
            line_scanner.useDelimiter("/|,|-|:|[MTHWF]");
            while(line_scanner.hasNext()){
                System.out.println(line_scanner.next());
            }
            line_scanner.close();
        }
        file_scanner.close();
    }
}