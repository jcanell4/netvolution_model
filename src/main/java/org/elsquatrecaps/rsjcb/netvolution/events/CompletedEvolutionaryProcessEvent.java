package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
public class CompletedEvolutionaryProcessEvent extends EvolutionaryEvent{
    private static final long serialVersionUID = 2478541542732890007L;
    public static final int GOAL_ACHIEVED_BY_EVOLUTION=0;
    public static final int MAXIMUM_CYCLE_NUMBER_REACHED=1;
    public static final int PROCESS_CANCELED_BY_USER=2;
    public static final String eventType ="CompletedProcess";
    private int terminationReason;
    private PtpNeuralNetwork[] finalPopulation;   

    
    protected CompletedEvolutionaryProcessEvent(String type, PtpNeuralNetwork[] finalPopulation) {
        super(type);
        this.finalPopulation = finalPopulation;
        terminationReason = GOAL_ACHIEVED_BY_EVOLUTION;        
    }

    protected CompletedEvolutionaryProcessEvent(String type, PtpNeuralNetwork[] finalPopulation, int terminationReason) {
        super(type);
        this.finalPopulation = finalPopulation;
        this.terminationReason = terminationReason;        
    }

    protected CompletedEvolutionaryProcessEvent(int id, String type, PtpNeuralNetwork[] finalPopulation) {
        super(id, type);
        this.finalPopulation = finalPopulation;
        terminationReason = GOAL_ACHIEVED_BY_EVOLUTION;        

    }

    protected CompletedEvolutionaryProcessEvent(int id, String type, PtpNeuralNetwork[] finalPopulation, int terminationReason) {
        super(id, type);
        this.finalPopulation = finalPopulation;
        this.terminationReason = terminationReason;        

    }


    public CompletedEvolutionaryProcessEvent(PtpNeuralNetwork[] finalPopulation) {
        this(eventType, finalPopulation);
    }

    public CompletedEvolutionaryProcessEvent(PtpNeuralNetwork[] finalPopulation, int terminationReason) {
        this(eventType, finalPopulation, terminationReason);
    }

    public CompletedEvolutionaryProcessEvent(int id, PtpNeuralNetwork[] finalPopulation) {
        this(id, eventType, finalPopulation);
    }
    
    public CompletedEvolutionaryProcessEvent(int id, PtpNeuralNetwork[] finalPopulation, int terminationReason) {
        this(id, eventType, finalPopulation, terminationReason);
    }
    
    public PtpNeuralNetwork[] getFinalPopulation() {
        return finalPopulation;
    }
    
    public int getTerminationReason(){
        return terminationReason;
    }
}
