package Project;

import java.util.Random;

public class Train implements Runnable {
    protected Random gen = new Random();
    protected final static int MAXSLEEP = 500; //maximum sleep time is 500ms
    protected final static int MAXWAITONLOCK = 1000; //maximum time to wait after lock request is denied before trying again
    protected final static int MOVETIME = 300; //simulated time to move train through train yard

    protected int trainNum;
    protected int inboundTrackNum;
    protected int outboundTrackNum;
    protected Switch firstSwitch;
    protected Switch secondSwitch;
    protected Switch thirdSwitch;
    protected boolean dispatched = false; //a dispatched train has moved from its inbound track, through the yard and onto its outbound track
    protected int dispatchSequence; //keep track of the order which the trains in the fleet are dispatched through the yard
    protected boolean hold;

    protected boolean allLocks = false;
    protected static int counter = 0; //generic counter

    //train constructor method
    //a train has a...
    public Train(int trainNumArg, int inboundTrackNumArg, int outboundTrackNumArg, Switch switch1Arg, Switch switch2Arg, Switch switch3Arg, boolean dispatchedArg, boolean holdArg){
        this.trainNum = trainNumArg;
        this.inboundTrackNum = inboundTrackNumArg;
        this.outboundTrackNum = outboundTrackNumArg;
        this.firstSwitch = switch1Arg;
        this.secondSwitch = switch2Arg;
        this.thirdSwitch = switch3Arg;
        this.dispatched = dispatchedArg;
        this.dispatchSequence = 0;
        this.hold = holdArg;
        //call debugging routing to print this train's data
        //printTrain();
    }

    //debugging method - print a train's data
    private void printTrain() {
        System.out.println("Train Data From Train Class - Immediately after constructing a train...");
        System.out.println("Train Number " + trainNum + " assigned.  ");
        System.out.println("Train number            Inbound Track           Outbound Track          Hold            Dispatched          Dispatch Sequence");
        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.println(" " + this.trainNum + "\t\t\t" + this.inboundTrackNum + "\t\t\t" + this.outboundTrackNum + "\t\t\t" + firstSwitch.switchNum + "\t\t\t" +
                secondSwitch.switchNum + "\t\t\t" + thirdSwitch.switchNum + "\t\t\t" + hold + "\t\t\t" + dispatched + "\t\t\t" + dispatchSequence);
        System.out.println();
        System.out.println();

    }

    //method for trains to "idle" (thread goes to sleep) for some random amount of time waiting to try lock request again
    public void waitTimeForLockRequest(){
        try{
            Thread.sleep(gen.nextInt(MAXWAITONLOCK));
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    //moveTrain simulates the train moving from its inbound track through the switches in the yard to the outbound track
    //you may need to play around with this number a bit
    public void moveTrain() {
        System.out.println("Train " + trainNum + ": HOLDS ALL NEEDED SWITCH LOCKS - Train movement begins.\n");
        try{
            Thread.sleep(MOVETIME);
            System.out.println("Train " + trainNum + ": Clear of yard control.");
            System.out.println("Train " + trainNum + ": Releasing all switch locks.");
            firstSwitch.unlockSwitch();
            System.out.println("Train " + trainNum + ": Unlocks/release lock on Switch " + firstSwitch.switchNum + ".");
            secondSwitch.unlockSwitch();
            System.out.println("Train " + trainNum + ": Unlocks/release lock on Switch " + secondSwitch.switchNum + ".");
            thirdSwitch.unlockSwitch();
            System.out.println("Train " + trainNum + ": Unlocks/release lock on Switch " + thirdSwitch.switchNum + ".");
            dispatched = true;
            dispatchSequence = TrainMovementSimulator.getDispatchCounter();
            System.out.println("Train " + trainNum + ": Has been dispatched and moves on down the line out of yard control into CTC.\n");
            System.out.println("@ @ @ TRAIN " + trainNum + ": DISPATCHED @ @ @\n");
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        //this is what a train does
        //method loops until train is dispatched - leaves yard control
            //acquire switch locks
            //if all locks acquired then move train
            //else release all locks held - wait for a while and try again

        while(!dispatched && !hold) {
            //handle first switch
            if(firstSwitch.lockSwitch()) {
                System.out.println("Train " + trainNum + ": HOLDS LOCK on Switch " + firstSwitch.switchNum + ".\n");

                //handle second switch
                if(secondSwitch.lockSwitch()){
                    System.out.println("Train " + trainNum + ": HOLDS LOCK on Switch " + secondSwitch.switchNum + ".\n");

                    //handle third switch
                    if(thirdSwitch.lockSwitch()){
                        System.out.println("Train " + trainNum + ": HOLDS LOCK on Switch " + thirdSwitch.switchNum + ".\n");
                        moveTrain(); //train has all needed switches to move
                    }
                    else{
                        System.out.println("Train " + trainNum + ": UNABLE TO LOCK third required switch: Switch " + secondSwitch.switchNum + ".");
                        System.out.println("Train " + trainNum + ": Releasing lock on first and second required switches: Switch " + firstSwitch.switchNum + "and Switch " + secondSwitch.switchNum + ". Train will wait...\n");
                        secondSwitch.unlockSwitch();
                        firstSwitch.unlockSwitch();

                        waitTimeForLockRequest(); //wait
                    }
                }
                else{
                    System.out.println("Train " + trainNum + ": UNABLE TO LOCK second required switch: Switch " + secondSwitch.switchNum + ".");
                    System.out.println("Train " + trainNum + ": Releasing lock on first required switch: Switch " + firstSwitch.switchNum + ". Train will wait...\n");
                    firstSwitch.unlockSwitch();

                    waitTimeForLockRequest(); //wait
                }
            }
            else{
                System.out.println("Train " + trainNum + ": UNABLE TO LOCK first required switch: Switch " + firstSwitch.switchNum + ". Train will wait...\n");
                waitTimeForLockRequest();
            }
        }
        //check to see if on hold
        if(hold) {
            System.out.println("*************");
            System.out.println("Train " + trainNum + " is on permanent hold and cannot be dispatched.");
            System.out.println("*************\n");
        }
    }
}
