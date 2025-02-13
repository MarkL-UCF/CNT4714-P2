package Project;

//this class implements the sharable/lockable/synchronized
//switch objects shared by the trains moving through the train yard
public class Switch {
    //define class variables as needed
    protected int switchNum;


    //constructor method
    //switch objects
    public Switch(int switchNumArg) {
        this.switchNum = switchNumArg;
    }

    //method for trains to acquire the switch locks
    public boolean lockSwitch() {
        return true; //testing line - returns true if train asks for the lock
    }

    //method for trains to release switch locks they hold
    public void unlockSwitch() {
        //lock is released by the train currently holding the lock
    }
}
