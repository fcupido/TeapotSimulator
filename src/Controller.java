import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Controller implements Runnable{
    private static final String filepath = "C:\\Users\\Felipe\\OneDrive\\Documents\\School\\ENCM 511\\Animation\\textfile.txt";
    private Model model;
    private View view;
    private boolean notQuit;
    private final int waitTimeMillis = 1000;

    private Controller()throws Exception{
        view = new View();
        model = new Model(filepath);
        notQuit = true;
    }

    public static void main (String [] args) throws Exception{
        System.out.println("Starting program");
        Controller controller = new Controller();
        Thread controllerThread = new Thread(controller);
        Thread viewTread = new Thread(controller.view);
        viewTread.start();
        //controllerThread.start();



        System.out.println("Press enter to terminate.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        controller.quit(); //Starts shutdowns process
        controllerThread.join();
        System.out.println("Program Ended");
    }

    public void run(){
        while (notQuit){
            try {
                if (model.checkForFileChange()) {
                    loadToView();
                    //Comment out when done
                    System.out.println("Change Detected. New File Contents");
                    for (String s:model.getNewValues()) {
                        System.out.println(s);
                    }
                    System.out.println();
                }
                sleep(waitTimeMillis);
            } catch (Exception e){
                System.out.println("Exception at controller.run");
                e.printStackTrace();
                System.exit(1);
            }

        }
    }

    void loadToView(){

    }

    void quit (){
        notQuit = false;
    }

}
