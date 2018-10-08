import java.io.*;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Controller implements Runnable{
    private Model model;
    private View view;
    private boolean notQuit;
    private final int waitTimeMillis = 50;

    /**
     * Constructor that initializes the model and view.
     * The view gets constructed with the results of the first file read.
     * @param filepath The filepath where the file to be interpreted is.
     */
    private Controller(String filepath){
        try {
            model = new Model(filepath);
            view = new View(model.getOldValues());
            notQuit = true;
        } catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("Not enough Teapots: Make sure the file at:\n "+filepath+"\n is not empty.");
            System.exit(1);
        }
    }

    public static void main (String [] args){
        System.out.println("Starting program");
        args = verifyArguments(args);
        Controller controller = new Controller(args[0]);
        Thread controllerThread = new Thread(controller);
        Thread viewTread = new Thread(controller.view);
        viewTread.start();
        controllerThread.start();


        System.out.println("Press enter to terminate.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        controller.quit(); //Starts shutdowns process
        try {
            controllerThread.join();
        } catch (InterruptedException ignored){}
        System.out.println("Program Ended");
        System.exit(0);
    }

    private static String[] verifyArguments(String[] args) {
        if(args.length < 1){
            File temp = new File("C:\\__CCES\\SCADA_TRANSMISSIONS\\coffeepot_remote_SCADAtransmission.info");
            if(temp.exists()){
                args = new String[]{"C:\\__CCES\\SCADA_TRANSMISSIONS\\coffeepot_remote_SCADAtransmission.info"};
            } else {
                System.err.println("No file found at: C:\\__CCES\\SCADA_TRANSMISSIONS\\coffeepot_remote_SCADAtransmission.info and no arguments provided");
                System.err.println("Usage:");
                System.err.println("java -jar Animation.jar <file path>");
                System.err.println("Example: java -jar Animation.jar \"C:\\Users\\your_user\\Documents\\School\\ENCM 511\\textfile.txt\"");
                System.err.println("You could also place the file at C:\\__CCES\\SCADA_TRANSMISSIONS\\coffeepot_remote_SCADAtransmission.info");

                System.exit(1);
            }
        }
        return args;
    }

    public void run(){
        int fileMisses = 0;
        while (notQuit){
            try {
                if (model.checkForFileChange()) {
                    loadChangesToView();
                }
                sleep(waitTimeMillis);
            }catch (java.io.FileNotFoundException ex){
                if(fileMisses++ > 1000){
                    System.out.println("File not found too many times");
                    ex.printStackTrace();
                    System.exit(2);
                }
            }   catch (IOException ex2){
                System.out.println("Exception at controller.run");
                ex2.printStackTrace();
                System.exit(1);
            } catch (InterruptedException | ArrayIndexOutOfBoundsException ignored){}
        }
    }

    private void loadChangesToView(){
        view.update(model.getNewValues());
    }

    void quit (){
        notQuit = false;
    }

}
