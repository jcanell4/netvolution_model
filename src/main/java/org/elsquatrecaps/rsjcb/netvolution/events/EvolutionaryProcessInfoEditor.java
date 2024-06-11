package org.elsquatrecaps.rsjcb.netvolution.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elsquatrecaps.utilities.concurrence.ConcurrentLinkedQueue;
import org.elsquatrecaps.utilities.concurrence.ConcurrentQueue;
import org.elsquatrecaps.utilities.concurrence.Monitor;

/**
 *
 * @author josep
 */
public class EvolutionaryProcessInfoEditor implements Runnable, ThreadableEvolutionaryProcessEditorService{
    private boolean exit=false;
    private final Monitor<Boolean> finished=new Monitor(false);
    private EvolutionaryProcessEventStore store;
    private final HashMap<String, ArrayList<EvolutionaryProcesSubscriptor>> subscribers = new HashMap<>();
    private String name;
    Thread f;

    public EvolutionaryProcessInfoEditor() {
    }

    public EvolutionaryProcessInfoEditor(EvolutionaryProcessEventStore store) {
        this.store = store;
    }
    
    @Override
    public void run() {
        //F2
        finished.setValue(false);
        subscribers.forEach((s, l) -> {
            for(EvolutionaryProcesSubscriptor c: l){
                c.start();
//                System.out.println(String.format("STARTING -SUBSC-%s-TH(%s)", ((DataBufferedSubscriber)c).name, Thread.currentThread().getName()));
            }
        });
        while(!exit){
            EvolutionaryEvent event = getStore().getNextEvent();
            publish(event.getEventType(), event);
            if(event.getEventType().equals(CompletedEvolutionaryProcessEvent.eventType)
                    || event.getEventType().equals(ErrorOnProcessEvolution.eventType)){
                exit = true;
                store.notifyLastEvent();
            }
        }
        subscribers.forEach((s, l) -> {
            for(EvolutionaryProcesSubscriptor c: l){
                c.notifyLastEvent();                
                c.join();
            }
        });
        synchronized (finished) {
            finished.setValue(true);
            finished.notifyAll();
        }
//            System.out.println(String.format("END - EDITOR-TH(%s)", Thread.currentThread().getName()));

    }
    
    @Override
    public void subscribe(String type, EvolutionaryProcesSubscriptor s){
        if(!subscribers.containsKey(type)){
            subscribers.put(type, new ArrayList<>());
        }
        subscribers.get(type).add(s);
    }
    
    @Override
    public void subscribe(String type, Consumer<EvolutionaryEvent> s){
        DataBufferedSubscriber bs = new DataBufferedSubscriber("EDITOR-CON-SUBS-1", s);
        bs.name = type;
        subscribe(type, bs);
    }
    
    
    
    @Override
    public void publish(String type, EvolutionaryEvent event){
        //F2
        publishOneType(EvolutionaryEvent.eventType, event);
        publishOneType(type, event);
    }
    
    private void publishOneType(String type, EvolutionaryEvent event){
        //F2
        if(subscribers.containsKey(type)){
            ArrayList<EvolutionaryProcesSubscriptor> l = subscribers.get(type);
            for(EvolutionaryProcesSubscriptor c: l){
                c.updateEvent(event);
            }
        }    
    }

    @Override
    public void start() {
        f = new Thread(this);
        f.start();
    }
    
    @Override
    public void start(String name) {
        this.name = name;
        f = new Thread(this, name);
        f.start();
    }

    @Override
    public void finish(){
        exit=true;
    }

    @Override
    public boolean isFinshRequired() {
        return exit;
    }    
       

    
    /**
     * @return the store
     */
    public EvolutionaryProcessEventStore getStore() {
        return store;
    }

    /**
     * @param store the store to set
     */
    public void setStore(EvolutionaryProcessEventStore store) {
        this.store = store;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    @Override
    public void join(){
        try {
            f.join();
            //this.getStore().close(name)
        } catch (InterruptedException ex) {
            Logger.getLogger(EvolutionaryProcessInfoEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

      
    public static class DataBufferedSubscriber implements EvolutionaryProcesSubscriptor, Runnable{
        private boolean stop = false;
        private final Monitor<Boolean> finished = new Monitor<>(false);
        ConcurrentQueue<EvolutionaryEvent> blockingQueue;
        Consumer<EvolutionaryEvent> action;
        EvolutionaryEvent lastUpdated;
        private String name;
        private boolean exitIfEmpty = false;
        private String keyForClose=null;
        Thread f;

        
        

        public DataBufferedSubscriber(String keyForClose, Consumer<EvolutionaryEvent> action) {
            this.keyForClose=keyForClose;
            this.blockingQueue = new ConcurrentLinkedQueue<>(String.format("%s-P", keyForClose));
            this.action = action;
        }
        
        @Override
        public void updateEvent(EvolutionaryEvent ev){
            //fil proveidor (F2)
            try {
                blockingQueue.put(ev);
                lastUpdated=ev;
            } catch (InterruptedException ex) {
                throw new RuntimeException("Error processing envent.", ex);
            }
        }

        @Override
        public void run() {
            //fil consumidor (F3 --FN)
            finished.setValue(false);
            while(!stop && tryToTake()){
                try {
                    EvolutionaryEvent ev = blockingQueue.take();
                    if(ev!=null){
                        action.accept(ev);
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Error processing envent.", ex);
                }
            }
            synchronized (finished) {
                finished.setValue(true);
                finished.notifyAll();
            }
            
//            System.out.println(String.format("END -SUBSC-%s-TH(%s)", name, Thread.currentThread().getName()));
        }
        
        @Override
        public boolean isFinshRequired() {
            return stop;
        }
        
        public void finish(){
            stop=true;
        }

        @Override
        public void start() {
//            f = threadPool.submit(this);
            f = new Thread(this);
            f.start();
        }

        @Override
        public void start(String name) {
            this.name = name;
//            f = threadPool.submit(this);
            f = new Thread(this, name);
            f.start();
        }
        
        @Override
        public void join(){
            this.blockingQueue.close(String.format("%s-P", keyForClose));
            try {
                f.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(EvolutionaryProcessInfoEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        @Override
        public EvolutionaryEvent lastEventUpdated() {
            return lastUpdated;
        }        

        @Override
        public void notifyLastEvent() {
            exitIfEmpty = true;
            this.blockingQueue.preventNextlocking();
        }
        
        private boolean tryToTake(){
            return !exitIfEmpty || !blockingQueue.isEmpty();
        }

//        @Override
//        public boolean close(String keyForClose) {
//            if(!this.keyForClose.equals(keyForClose)){
//                throw new RuntimeException("Error. close method is calling from the same thread");
//            }
//            finish();
//            this.blockingQueue.close(String.format("%s-P", keyForClose));
//            return this.blockingQueue.isEmpty();
//        }
    }    
}
