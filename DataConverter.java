import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;

/**
 * Write a description of class DataConverter here.
 * 
 * @author Connie and Ariel 
 * @version 04/26/16
 */
public class DataConverter
{
    // instance variables - replace the example below with your own

    private static File dataFile = new File("BreastCancer.txt");
    private static int totalPatients = 56;
    private static int totalAttributes = 10;
    private static double[] inputs;
    private static double[] outputs;
    private static int[] patientID;


    
    /**
     * Constructor for objects of class DataConverter
     */
    public DataConverter()
    {
    }
    
    /**
     * The fileReader() reads the dataFile with the breast cancer information and stores
     * the inputs as doubles in an arrayList called records.
     */
     public static void fileReader()
    {
        String line = null;
        int patientCount = 0; // keeps track of the totalPatients
        int inputCount = 0; //keeps track of the attributes of all totalPatients (10 attributes for each patient)
        
        inputs = new double[totalPatients*30];
        outputs = new double[totalPatients]; //will store the diagnosis of each patient.
        patientID = new int[totalPatients];
        
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            while ( ((line = bufferedReader.readLine()) != null) && patientCount < totalPatients)
            {
                boolean newLine = true; 
                for (String str: line.split(","))
                {
                    if(str.equals("M"))
                    {
                        outputs[patientCount] = 1.0;
                        patientCount++;
                    }
                    else if( str.equals("B"))
                    {
                        outputs[patientCount] = 0.0;
                        patientCount++;
                    }
                    else
                    {
                        if( newLine)
                        {
                            int num = Integer.parseInt(str);
                            patientID[patientCount] = num;
                            newLine = false;
                        }
                        else
                        {
                            double num = new Double(str).doubleValue();
                            inputs[inputCount] = num;
                            inputCount++;
                        }
                    }
                    
                } 
            }
            bufferedReader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("File does not exist please try again. ");
        }
        //call the constructor from the example class to pass the required objects
        //put example object into array of example objects
        //return that array
    }
    

    public static int getID( int num )
    {
        return patientID[num];
    }
    
    
    /**
     * Converting the data into a range of 0-1
     * (val - min) / (max - min)
     */
    public Example[] getInputs()
    {
        fileReader();
        double maxAttr = 0.0;
        double minAttr = 0.0;
        int index = 0;
        Example[] examples = new Example[totalPatients];
        double[] attributes = new double[ totalAttributes*totalPatients];

        //attributes array stores the first attribute of all the patients and then the second attribute of all patients and then the third and so on. 
        for(int column = 0; column < totalAttributes; column++) //It looks at the attributes (columns)
        {
            maxAttr = max(column);
            minAttr = min(column);
            for(int i = column; i < inputs.length; i+=30) //It looks at each patient (Row!)
            {
                attributes[index] = (inputs[i] - minAttr)/(maxAttr - minAttr); 
                index++;
            }
        }
        
        
        int count = 0;
        //We have to keep in mind that the first attribute is stored for all patients ( Ex: attribute 1 is stored for all 56 patients.
        //attribute 2 is stored for all 56 patients. and so on.)
        for( int i = 0; i < totalPatients; i++)// Looks at 1 patient at a time
        {
            double[] arr = new double[totalAttributes];
            index = 0;
            for( int j = i; j < attributes.length; j+=totalPatients)// looks at the attributes for each patient (totalAttributes)
            {
                arr[index] = attributes[j];
                index++;
            }
            double[] temp = new double[] { outputs[count]};
            Example ex = new Example (arr, temp);
            examples[count] = ex;
            count++;
        }

        //size of the array is ten
        //example array
        //still need double[] to pass as arguments to example constructor
        //an array of doubles for inputs and an array of doubles for the outputs
        return examples;

    }
    
    /**
     * 
     */
    public static double max(int column)
    {
        double temp = 0.0;
        for(int i = column; i < inputs.length; i+=30)
        {
            if( inputs[i] > temp)
            {
                temp = inputs[i];
            }
        }
        return temp;
    }
    
     /**
     * 
     * 
     */
    public static double min(int column)
    {
        double temp = inputs[column];
        for(int i = column; i < inputs.length; i+=30)
        {
            if( inputs[i] < temp)
            {
                temp = inputs[i];
            }
        }
        return temp;
    }
    
    
    
    public static void testThing()
    {
        fileReader();
        for(int i = 0; i < inputs.length; i++)
        {
            System.out.println( i + " inputs: " + inputs[i]);
        }
        
        for(int i = 0; i < outputs.length; i++)
        {
            System.out.println(i + " ID: " + patientID[i]);
            System.out.println(i + " outputs: " + outputs[i]);
        }
    }
}