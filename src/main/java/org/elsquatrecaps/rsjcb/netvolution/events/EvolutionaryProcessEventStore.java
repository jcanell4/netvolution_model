package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.utilities.concurrence.ConcurrentDeque;
import org.elsquatrecaps.utilities.concurrence.ConcurrentLinkedDeque;



/**
 *
 * @author josepcanellas
 */
public class EvolutionaryProcessEventStore implements EvolutionaryEventStore<EvolutionaryEvent>{
    ConcurrentDeque<EvolutionaryEvent> blockingQueue;
    EvolutionaryEvent lastEventToken;
    private String keyForClose=null;


    public EvolutionaryProcessEventStore(String keyForClose) {
        this.keyForClose=keyForClose;
        blockingQueue = new ConcurrentLinkedDeque<>(String.format("%s-P", keyForClose));
    }
    
    
    public EvolutionaryProcessEventStore(String keyForClose, int queueCapacity) {
        this.keyForClose = keyForClose;
        blockingQueue = new ConcurrentLinkedDeque<>(String.format("%s-P", keyForClose), queueCapacity);
    }
    
    @Override
    public void updateEvent(org.elsquatrecaps.rsjcb.netvolution.events.EvolutionaryEvent ev){
        //F1 (proveidor)
        try {
            blockingQueue.put(ev);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Event lost because store is full", ex);
        }
    }

    @Override
    public EvolutionaryEvent getNextEvent() {
        //F2  (consumidor)
        EvolutionaryEvent ret; 
        try {
            ret = blockingQueue.take();
            lastEventToken=ret;
        } catch (InterruptedException ex) {
            ret = new EvolutionaryErrorPickingLastEvent(ex);
        }
        return ret;
    }

    @Override
    public EvolutionaryEvent peeklastEvent() {
        EvolutionaryEvent ret=null; 
        ret = blockingQueue.peekLast();
        return ret;
    }

    @Override
    public EvolutionaryEvent lastEventUpdated() {
        EvolutionaryEvent ret; 
        if(blockingQueue.isEmpty()){
            ret = lastEventToken;
        }else{
            ret = peeklastEvent();
        }
        return ret;
    }
    
    @Override
    public void notifyLastEvent(){
        this.blockingQueue.preventNextlocking();
    }
    
//    @Override
    public boolean close(String keyForClose){
        if(!this.keyForClose.equals(keyForClose)){
            throw new RuntimeException("Error. close method is calling from the same thread");
        }
        this.blockingQueue.close(String.format("%s-P", keyForClose));
        return blockingQueue.isEmpty();
    }           
}
