import java.util.Scanner;

/**
 * Class that controls what the model is doing and ensures that the view keeps rendering.
 * @author Felipe Cupido
 */



public class Model{
    private String [] oldValues;
    private String [] newValues;
    private MyFileReader fileReader;
    private boolean notQuit;

    String[] getNewValues() {
        return newValues;
    }

    Model(String filePath) throws Exception{
        notQuit = true;
        fileReader = new MyFileReader(filePath);
        oldValues = fileReader.getFileContents();
    }

    private boolean diffOldNew(){
        if(oldValues.length != newValues.length){
            return true;
        } else {
            for (int i = 0; i < oldValues.length; i++){
                if((oldValues[i].compareTo(newValues[i])) != 0){
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkForFileChange ()throws Exception{
        newValues = fileReader.getFileContents();
        boolean returnValue = diffOldNew();
        oldValues = newValues;
        return returnValue;
    }
}
