package util;

import java.util.function.Predicate;
import java.util.function.Supplier;

/*
This class credited to: Sequeira, Alexander S S <assseq@essex.ac.uk>;
Date: 30 Novemeber 2017
 */

public class SpinLock {

    //Continuous checks at interval for condition it is meant and sleeps thread until then
    public static <T> boolean spinLock(int interval, Supplier<T> value, Predicate<T> condition) {
        try {
            for (T t = value.get(); !condition.test(t); t = value.get())
                Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
