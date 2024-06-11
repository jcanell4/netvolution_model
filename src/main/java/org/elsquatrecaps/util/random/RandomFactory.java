package org.elsquatrecaps.util.random;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josepcanellas
 */
public class RandomFactory {
    
    private static boolean debug = false;
    private static int debugInstance=-1;
    private static final List<Random> randomInstances = new ArrayList();

    public static void setDebugState(boolean pdebug) {
        debug = pdebug;
        if(debug){
            creteDebugRandomInstance();
        }
    }

    public static Random getRandomInstance() {
        if(debug){
            return getRandomInstance(debugInstance);
        }else{
            return getRandomInstance(0);
        }
    }
    
    public static Random getRandomInstance(int i) {
        if(i>=randomInstances.size()){
            if(System.getProperties().getProperty("os.name").toLowerCase().matches(".*[na][iu]x.*")){
              randomInstances.add(i, new Random());
            }else{
              randomInstances.add(i, new Random(System.nanoTime()+System.currentTimeMillis()));                
            }
        }
        return randomInstances.get(i);
    }    
    
    private static void creteDebugRandomInstance(){
        if(debugInstance==-1){
            debugInstance = randomInstances.size();
            randomInstances.add(new Random(151983452019167l));
        }
    }
}
