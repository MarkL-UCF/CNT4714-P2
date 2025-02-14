package Project;

import java.util.concurrent.locks.ReentrantLock;

//this class implements the sharable/lockable/synchronized
//switch objects shared by the trains moving through the train yard
public class Switch {
    //define class variables as needed
    protected int switchNum;
    private final ReentrantLock lock;

    //constructor method
    //switch objects
    public Switch(int switchNumArg) {
        this.switchNum = switchNumArg;
        this.lock = new ReentrantLock();
    }

    //method for trains to acquire the switch locks
    public boolean lockSwitch() {
        return lock.tryLock();
    }

    //method for trains to release switch locks they hold
    public void unlockSwitch() {
        lock.unlock();
    }
}
