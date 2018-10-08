import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MyFileReader {
    MyFileReader(String file_path){
        fileName = file_path;
    }
    private String fileName;
    private BufferedReader reader;

    private void reopen () throws Exception{
        if(reader != null){
            reader.close();
        }
        reader = new BufferedReader(new FileReader(fileName));
        reader.mark(1000);
    }
    private int getLineCount() throws IOException{
        reader.reset();
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        return count;
    }

    String [] getFileContents()throws IOException, ArrayIndexOutOfBoundsException{
        reader = new BufferedReader(new FileReader(fileName));
        reader.mark(1000);
        String[] fileContents = new String[getLineCount()];
        String line;
        reader.reset();
        for (int i = 0; (line = reader.readLine()) != null; i++) {
            fileContents[i] = line;
        }
        reader.close();
        return fileContents;
    }

    public static void main(String [] args) throws Exception {
        String line;
        BufferedReader reader = new BufferedReader(new java.io.FileReader("C:\\Users\\Felipe\\OneDrive\\Documents\\School\\ENCM 511\\Animation\\textfile.txt"));
        reader.mark(1000);
        for (int i = 0; i<60; i++) {

            reader.reset();
            System.out.println("Reading Once");
            while ((line = reader.readLine()) != null) {
                System.out.println(i + ": " +line);
            }

            reader.reset();
            System.out.println("reading twice");
            while ((line = reader.readLine()) != null) {
                System.out.println(i + ": " +line);
            }


            //Thread.sleep(3000);
        }
        reader.close();
    }
}
