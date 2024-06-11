
package org.elsquatrecaps.rsjcb.netvolution.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author josepcanellas
 */
public class EvolutionaryEvent implements Serializable{
    private static final long serialVersionUID = 2478541542732890000L;        
    public static final String eventType ="Any";
    public static int counter=0;
    private String internalEventType="Unknown";
    private int id;

    synchronized private static int getCounter(){
        return ++counter;
    }
    
    public EvolutionaryEvent(String type) {
        id= getCounter();
        this.internalEventType = type;
    }
    
    public EvolutionaryEvent(int id, String type) {
        getCounter();
        this.id = id;
        this.internalEventType = type;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the eventType
     */
    public String getEventType() {
        return internalEventType;
    }
}
