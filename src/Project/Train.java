package Project;

import java.util.Random;

public class Train implements Runnable {
    //define class variables

    //train constructor method
    //a train has a...
    public Train(){
        //define a train object
        //call debugging routing to print this train's data
        //printTrain();
    }

    //method for trains to "idle" (thread goes to sleep) for some random amount of time waiting to try lock request again
    public void waitTimeForLockRequest(){
        /*
        try{
            Thread.sleep(gen.nextInt(MAXWAITONLOCK));
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

         */
    }

    //moveTrain simulates the train moving from its inbound track through the switches in the yard to the outbound track
    //you may need to play around with this number a bit
    public void moveTrain() {
        /*
        System.out.println("TRAIN " + trainNum + " begins moving through the train yard");
        try{
            Thread.sleep(MOVETIME);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

         */
    }


    public void run() {
        //this is what a train does
    }
}
