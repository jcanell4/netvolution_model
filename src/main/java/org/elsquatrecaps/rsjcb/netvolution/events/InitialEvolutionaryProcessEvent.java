package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
public class InitialEvolutionaryProcessEvent extends EvolutionaryEvent{
    private static final long serialVersionUID = 2478541542732890004L;    
    public static final String eventType ="InitializedProcess";
    private double averagePerformanceForStopping;
    private double desiredPerformance;
    private int maxTimes;
    private PtpNeuralNetwork[] initialPopulation;   

    protected InitialEvolutionaryProcessEvent(int id, String type, double averagePerformanceForStopping, double desiredPerformance, int maxTimes, PtpNeuralNetwork[] initialPopulation) {
        super(id, eventType);
        this.averagePerformanceForStopping = averagePerformanceForStopping;
        this.desiredPerformance = desiredPerformance;
        this.maxTimes = maxTimes;
        this.initialPopulation = initialPopulation;
    }

    protected InitialEvolutionaryProcessEvent(String type, double averagePerformanceForStopping, double desiredPerformance, int maxTimes, PtpNeuralNetwork[] initialPopulation) {
        super(eventType);
        this.averagePerformanceForStopping = averagePerformanceForStopping;
        this.desiredPerformance = desiredPerformance;
        this.maxTimes = maxTimes;
        this.initialPopulation = initialPopulation;
    }
    
    public InitialEvolutionaryProcessEvent(int id, double averagePerformanceForStopping, double desiredPerformance, int maxTimes, PtpNeuralNetwork[] initialPopulation) {
        this(id, eventType, averagePerformanceForStopping, desiredPerformance, maxTimes, initialPopulation);
    }

    public InitialEvolutionaryProcessEvent(double averagePerformanceForStopping, double desiredPerformance, int maxTimes, PtpNeuralNetwork[] initialPopulation) {
        this(eventType, averagePerformanceForStopping, desiredPerformance, maxTimes, initialPopulation);
    }
    
    /**
     * @return the averagePerformanceForStopping
     */
    public double getAveragePerformanceForStopping() {
        return averagePerformanceForStopping;
    }

    /**
     * @return the desiredPerformance
     */
    public double getDesiredPerformance() {
        return desiredPerformance;
    }

    /**
     * @return the maxTimes
     */
    public int getMaxTimes() {
        return maxTimes;
    }

    /**
     * @return the initialPopulation
     */
    public PtpNeuralNetwork[] getInitialPopulation() {
        return initialPopulation;
    }

    /**
     * @param initialPopulation the initialPopulation to set
     */
    public void setInitialPopulation(PtpNeuralNetwork[] initialPopulation) {
        this.initialPopulation = initialPopulation;
    }
}
