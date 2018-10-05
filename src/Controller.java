import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Controller implements Runnable{
    private static final String filepath = (System.getProperty("user.dir")+"\\textfile.txt");
    private Model model;
    private View view;
    private boolean notQuit;
    private final int waitTimeMillis = 50;

    private Controller()throws Exception{
        model = new Model(filepath);
        view = new View(model.getOldValues());
        notQuit = true;
    }

    public static void main (String [] args) throws Exception{
        System.out.println("Starting program");
        Controller controller = new Controller();
        Thread controllerThread = new Thread(controller);
        Thread viewTread = new Thread(controller.view);
        viewTread.start();
        controllerThread.start();



        System.out.println("Press enter to terminate.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        controller.quit(); //Starts shutdowns process
        controllerThread.join();
        System.out.println("Program Ended");
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
            }   catch (Exception e){
                System.out.println("Exception at controller.run");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void loadChangesToView(){
        view.update(model.getNewValues());
    }

    void quit (){
        notQuit = false;
    }

}
