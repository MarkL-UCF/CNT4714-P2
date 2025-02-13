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
        System.out.println("TRAIN " + trainNum + " begins moving through the train yard");
        try{
            Thread.sleep(MOVETIME);
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
    }
}
