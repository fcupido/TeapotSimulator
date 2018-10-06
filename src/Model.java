import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class that controls what the model is doing and ensures that the view keeps rendering.
 * @author Felipe Cupido
 */


class Model{
    private String [] oldValues;
    private String [] newValues;
    private MyFileReader fileReader;

    String [] getNewValues() {
        return newValues;
    }
    String [] getOldValues() {
        return oldValues;
    }

    Model(String filePath){
        int maxTries = 10;
        for (int i = 0; i <= 10; i++) {
            try {
                fileReader = new MyFileReader(filePath);
                oldValues = fileReader.getFileContents();
                break;
            } catch (FileNotFoundException ex) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored){}
                if(i == maxTries){
                    System.err.println("No file found at: " + filePath);
                    System.err.println("Tried: (" + maxTries+") times." );
                    System.exit(1);
                }
            } catch (IOException ex2) {
                ex2.printStackTrace();
                System.exit(1);
            }
        }
    }

    private boolean isNewDifferentFromOld(){
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

    boolean checkForFileChange ()throws IOException{
        newValues = fileReader.getFileContents();
        boolean returnValue = isNewDifferentFromOld();
        oldValues = newValues;
        return returnValue;
    }
}
