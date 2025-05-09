package Project;

import java.util.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("ALL")
public class TrainMovementSimulator {
    static final int MAXTRAINS = 30; //max number of trains to be routed through the train yard
    static final int MAXALIGNMENTS = 60; //max number of track to switch combinations in the train yard
    static final int MAXSWITCHES = 10; //maximum number of switches in the train yard

    static int dispatchCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        int numberOfTrainsInTheSimulationFleet = 0;
        int numberOfPathsThroughTheTrainYard = 0;

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
        File theFleetFile = new File("src/theFleetFile.csv"); //info on the trains in the sim
        File theYardFile = new File("src/theYardFile.csv"); //info on the configuration of the yard
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

            counter = 0;
            theYardFileReader = new FileReader(theYardFile);
            theYardFileBufferedReader = new BufferedReader(theYardFileReader);
            anAlignment = theYardFileBufferedReader.readLine();

            //DEBUGGING BLOCK
            System.out.println("\nDetails of the Train Yard being simulated this run\n");
            System.out.println("Inbound Track           Switch 1            Switch 2            Switch 3            Outbound Track");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            //END DEBUGGING BLOCK

            //read the yard details

            while(anAlignment != null) {
                theYardFileScanner = new Scanner(anAlignment).useDelimiter("\\s*,\\s*");
                inboundYardTrack[counter] = theYardFileScanner.nextInt();
                firstYardSwitchNumber[counter] = theYardFileScanner.nextInt();
                secondYardSwitchNumber[counter] = theYardFileScanner.nextInt();
                thirdYardSwitchNumber[counter] = theYardFileScanner.nextInt();
                outboundYardTrack[counter] = theYardFileScanner.nextInt();

                //DEBUGGING BLOCK
                System.out.println(" " + inboundYardTrack[counter] + "\t\t\t" + firstYardSwitchNumber[counter] + "\t\t\t" + secondYardSwitchNumber[counter] + "\t\t\t" + thirdYardSwitchNumber[counter] + "\t\t\t" + outboundYardTrack[counter]);
                System.out.println();
                //END DEBUGGING BLOCK

                ++counter;
                anAlignment = theYardFileBufferedReader.readLine();
            }

            numberOfPathsThroughTheTrainYard = counter;

            //DEBUGGING BLOCK
            System.out.println("There are " + numberOfPathsThroughTheTrainYard + " path(s) in the simulation yard.");
            //END DEBUGGING BLOCK

            //create the switch objects
            Switch[] switches = new Switch[MAXSWITCHES];

            for(int i = 0; i < MAXSWITCHES; ++i) {
                switches[i] = new Switch(i + 1);
            }

            //make the assignment of switches for each train in the simulation fleet
            for(int i = 0; i < numberOfTrainsInTheSimulationFleet; ++i) {
                boolean hasRoute = false;
                for(int j = 0; j < numberOfPathsThroughTheTrainYard; ++j) {
                    if((inboundYardTrack[j] == theFleet[i].inboundTrackNum) && (outboundYardTrack[j] == theFleet[i].outboundTrackNum)) {
                        theFleet[i].firstSwitch = switches[firstYardSwitchNumber[j] - 1];
                        theFleet[i].secondSwitch = switches[secondYardSwitchNumber[j] - 1];
                        theFleet[i].thirdSwitch = switches[thirdYardSwitchNumber[j] - 1];
                        hasRoute = true;
                        break;
                    }
                }

                if(!hasRoute)
                    theFleet[i].hold = true;
            }
        } //end try block
        catch(Exception e){
            e.printStackTrace();
        } //end catch block

        //All trains are now either assigned a route or are on permanent hold (due to no path)

        //DEBUGGING BLOCK
        System.out.println("\n * * * * * * * * * * SIMULATION CONFIGURATION DETAILS COMPLETE * * * * * * * * * * ");
        System.out.println("\n\n");
        System.out.println("\n TRAIN MOVEMENT SIMULATION BEGINS.....\n\n\n");
        //END DEBUGGING BLOCK

        //create the train fleet (a thread pool) of MAXTRAINS size
        ExecutorService TrainFleet = Executors.newFixedThreadPool(MAXTRAINS);

        //start the trains running
        for(int i = 0; i < numberOfTrainsInTheSimulationFleet; ++i) {
            TrainFleet.execute(theFleet[i]);
        }

        //call to shut down the executor service
        TrainFleet.shutdown(); //start the shutdown process - no new threads will be started after this call


        while(!TrainFleet.isTerminated()) {
            //simulation thread still running
        }


        //DEBUGGING BLOCK
        System.out.println("Finished all threads");
        //END DEBUGGING BLOCK

        System.out.println("\nFinal Details of the Train Fleet being simulated this run\n");

        for(int i = 0; i < numberOfTrainsInTheSimulationFleet; ++i) {
            System.out.println("Train Number\t\t\tInbound Track\t\t\tOutbound Track\t\t\tSwitch 1\t\t\tSwitch 2\t\t\tSwitch 3\t\t\tHold\t\t\tDispatched\t\t\tDispatch Sequence");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println(" " + theFleet[i].trainNum + "\t\t\t" + theFleet[i].inboundTrackNum + "\t\t\t" + theFleet[i].outboundTrackNum + "\t\t\t" + theFleet[i].firstSwitch.switchNum + "\t\t\t" + theFleet[i].secondSwitch.switchNum + "\t\t\t" + theFleet[i].thirdSwitch.switchNum + "\t\t\t" + theFleet[i].hold + "\t\t\t" + theFleet[i].dispatched + "\t\t\t" + theFleet[i].dispatchSequence + "\n\n");
        }

        System.out.println("\n ");
        System.out.println("\n\n * % * % * % SIMULATION ENDS % * % * % * \n");

    }

    public static int getDispatchCounter() {
        ++dispatchCounter;
        return dispatchCounter;
    }
}