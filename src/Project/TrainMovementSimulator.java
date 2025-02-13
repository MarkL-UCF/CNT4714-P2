package Project;

import java.util.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainMovementSimulator {
    static final int MAXTRAINS = 30; //max number of trains to be routed through the train yard
    static final int MAXALIGNMENTS = 60; //max number of track to switch combinations in the train yard
    static final int MAXSWITCHES = 10; //maximum number of switches in the train yard

    public static void main(String[] args) throws InterruptedException {
        int numberOfTrainsInTheSimulationFleet;
        int numberOfPathsThroughTheTrainYard;

        //variables holding the details of the trains
        int trainNumber;
        int inboundTrack;
        int outboundTrack;
        boolean dispatched;
        boolean hold;
        int dispatchSequence;

        //this is a dummy switch which is used to initialize a trains switches when the train object is first created before
        //track assignments are made. The switch with switchNum == 0 does not exist in our train yard simulation
        Switch dummySwitch = new Switch(0);



        //variables holding the details of the train yard
        int[] inboundYardTrack = new int[MAXALIGNMENTS];
        int[] firstYardSwitchNumber = new int[MAXALIGNMENTS];
        int[] secondYardSwitchNumber = new int[MAXALIGNMENTS];
        int[] thirdYardSwitchNumber = new int[MAXALIGNMENTS];
        int[] outboundYardTrack = new int[MAXALIGNMENTS];


        //theFleet contains all the trains in the simulation run that need to move through the train yard
        Train[] theFleet = new Train[MAXTRAINS];


        //define the file, file reader, buffered reader, and scanner objects needed
        File theFleetFile = new File("theFleetFile.csv"); //info on the trains in the sim
        File theYardFile = new File("theYardFile.csv"); //info on the configuration of the yard
        FileReader theFleetFileReader = null;
        FileReader theYardFileReader = null;
        BufferedReader theFleetFileBufferedReader = null;
        BufferedReader theYardFileBufferedReader = null;
        Scanner theFleetFileScanner = null;
        Scanner theYardFileScanner = null;
        String aTrain = null;
        String anAlignment = null;
        int counter; //a generic loop counter to count the number of trains in a simulation run

        try {
            System.out.println("\n Spring 2025 - Project 2 - Train Movement Simulator \n\n");
            System.out.println("\n * * * * * * * * * * INITIALIZATION OF SIMULATION BEGINS * * * * * * * * * * \n");
            System.out.println("\n\n");

            //read in the theFleet.csv file
            //the number of lines in this file indicates the number of trains in the fleet
            //this file contains the info on the trains in the fleet schedule to be simulated
            //the file is a comma separated file with information on each train in the form of
            //train number (int), inbound track (int), outbound track (int)
            //ex: 1,6,4

            //DEBUGGING BLOCK
            System.out.println("\nDetails of the Train Fleet being simulated this run\n");
            System.out.println("Train number            Inbound Track           Outbound Track          Hold            Dispatched          Dispatch Sequence");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            //END DEBUGGING BLOCK

            //read a train's details
            counter = 0;
            theFleetFileReader = new FileReader(theFleetFile);
            theFleetFileBufferedReader = new BufferedReader(theFleetFileReader);
            aTrain = theFleetFileBufferedReader.readLine(); //read a line from TheFleetFile

            while(aTrain != null) {
                theFleetFileScanner = new Scanner(aTrain).useDelimiter("\\s*,\\s*");
                trainNumber = theFleetFileScanner.nextInt();
                inboundTrack = theFleetFileScanner.nextInt();
                outboundTrack = theFleetFileScanner.nextInt();
                dispatched = false;
                hold = false;
                dispatchSequence = 0;

                //DEBUGGING BLOCK
                System.out.println(" " + trainNumber + "\t\t\t" + inboundTrack + "\t\t\t" + outboundTrack + "\t\t\t" + hold + "\t\t\t" + dispatched + "\t\t\t" + dispatchSequence);
                System.out.println();
                //END DEBUGGING BLOCK

                //create the train object
                theFleet[counter] = new Train(trainNumber, inboundTrack, outboundTrack, dummySwitch, dummySwitch, dummySwitch, hold, dispatched);

                counter++; //increment counter

                aTrain = theFleetFileBufferedReader.readLine(); //read in the next line in theFleetFile
            }

            numberOfTrainsInTheSimulationFleet = counter;
            System.out.println("There are " + numberOfTrainsInTheSimulationFleet + " train(s) in the simulation fleet.");

            //Read in the theYardConfiguration.csv File
            //this file contains the information on the train yard configuration. This is how dispatcher knows
            //which tracks are connected to which switches so that trains can be properly routed through the train yard
            //this file is a comma separated file with information on each track/switch connection
            //each line has the form:
            //inbound track number (int), first switch number (int), second switch number (int), third switch number (int), outbound track number (int)
            //example: 1,1,3,4,1
            //read theFleetFile into an array and get the number of lines in the file as well to represent the fleetSize

            //DEBUGGING BLOCK

            //END DEBUGGING BLOCK

            //read the yard details -

            //DEBUGGING BLOCK
            //END DEBUGGING BLOCK

            //create the switch objects

            //make the assignment of switches for each train in the simulation fleet


            //DEBUGGING BLOCK

            //END DEBUGGING BLOCK

        } //end try block
        catch(Exception e){
            e.printStackTrace();
        } //end catch block

        //All trains are now either assigned a route or are on permanent hold (due to no path)

        //DEBUGGING BLOCK
        //System.out.println("\n * * * * * * * * * * SIMULATION CONFIGURATION DETAILS COMPLETE * * * * * * * * * * ");
        //System.out.println("\n\n");
        //System.out.println("\n TRAIN MOVEMENT SIMULATION BEGINS.....\n\n\n");
        //END DEBUGGING BLOCK

        //create the train fleet (a thread pool) of MAXTRAINS size
        ExecutorService TrainFleet = Executors.newFixedThreadPool(MAXTRAINS);
        //start the trains running

        //call to shut down the executor service
        TrainFleet.shutdown(); //start the shutdown process - no new threads will be started after this call

        while(!TrainFleet.isTerminated()) {
            //simulation thread still running
        }
        //System.out.println("Finished all threads");

        //DEBUGGING BLOCK

        //END DEBUGGING BLOCK

        System.out.println("\n ");
        System.out.println("\n\n * % * % * % SIMULATION ENDS % * % * % * \n");

    }
}