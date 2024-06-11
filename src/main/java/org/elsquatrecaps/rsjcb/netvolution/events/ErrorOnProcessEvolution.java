package org.elsquatrecaps.rsjcb.netvolution.events;

/**
 *
 * @author josep
 */
public class ErrorOnProcessEvolution extends EvolutionaryEvent{
    private static final long serialVersionUID = 2478541542732890001L;    
    public static final String eventType ="ErrorOnProcessEvent";
    private Exception ex;

    protected ErrorOnProcessEvolution(int id, String type, Exception ex) {
        super(id, type);
        this.ex=ex;
    }

    protected ErrorOnProcessEvolution(String type, Exception ex) {
        super(type);
        this.ex=ex;
    }

    public ErrorOnProcessEvolution(int id, Exception ex) {
        this(id, eventType, ex);
    }

    public ErrorOnProcessEvolution(Exception ex) {
        this(eventType, ex);
    }

    /**
     * @return the ex
     */
    public Exception getException() {
        return ex;
    }
    
    
}
