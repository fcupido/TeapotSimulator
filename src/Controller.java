import java.util.Scanner;

/**
 * Class that controls what the model is doing and ensures that the view keeps rendering.
 * @author Felipe Cupido
 */



public class Controller implements Runnable{
    private String [] oldVals;
    private String [] newVals;
    private MyFileReader fileReader;
    private boolean notQuit;
    public Controller (String filePath) throws Exception{
        notQuit = true;
        fileReader = new MyFileReader(filePath);
        oldVals = fileReader.getFileContents();
        for (String line: oldVals) {
            System.out.println(line);
        }
        //view = new view();
        //view.loadContents(oldVals);
        //view.run();
    }

    private boolean fileChanged (){
        for(String oldValsa: oldVals)
        System.out.println("Old Vals: " + oldValsa);
        for(String newValsb: newVals)
        System.out.println("New Vals: " + newValsb);
        if(oldVals.length != newVals.length){
            return true;
        } else {
            for (int i = 0; i < oldVals.length; i++){
                if((oldVals[i].compareTo(newVals[i])) != 0){
                    return true;
                }
            }
        }
        return false;
    }

    public void quit (){
        notQuit = false;
    }

    public static void main (String [] args) throws Exception{

        System.out.println("Starting program");
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller("C:\\Users\\Felipe\\OneDrive\\Documents\\School\\ENCM 511\\Animation\\textfile.txt");
        controller.run();
        scanner.nextLine();
        controller.quit();
        System.out.println("Program Ended");
    }


    @Override
    public void run(){
        try {
            while (notQuit) {
                newVals = fileReader.getFileContents();
                if(fileChanged()){
                    //view.load(newVals);
                    System.out.println("File Has Changed");
                } else {
                    System.out.println("No change detected");
                }
                oldVals = newVals;
                System.out.println(" ");
                Thread.sleep(500);
            }
        } catch (Exception e){
            System.err.println("Exception at controller.run()");
        }
    }
}
