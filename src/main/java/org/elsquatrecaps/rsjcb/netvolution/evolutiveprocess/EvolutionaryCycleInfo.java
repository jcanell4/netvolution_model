/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;


/**
 *
 * @author josep
 */
public class EvolutionaryCycleInfo implements Serializable{
    private static final long serialVersionUID = 2478541542732890009L;        
    private final int replacedAgents;
    private final double avgPerformance;
    private final double maxPerformance;
    private final double minPerformance;
    private final PtpNeuralNetwork bestAgent;
    private final PtpNeuralNetwork worstAgent;
    final private Map<String, Double> extraInfo= new HashMap<>();

    public EvolutionaryCycleInfo(int replacedAgents, double avgPerformance, double MaxPerformance, double MinPerformance, PtpNeuralNetwork bestAgent, PtpNeuralNetwork worstAgent) {
        this.replacedAgents = replacedAgents;
        this.avgPerformance = avgPerformance;
        this.maxPerformance = MaxPerformance;
        this.minPerformance = MinPerformance;
        this.bestAgent = bestAgent;
        this.worstAgent = worstAgent;
    }
    
    public EvolutionaryCycleInfo(int replacedAgents, float avgPerformance, float MaxPerformance, float MinPerformance, PtpNeuralNetwork bestAgent, PtpNeuralNetwork worstAgent) {
        this.replacedAgents = replacedAgents;
        this.avgPerformance = avgPerformance;
        this.maxPerformance = MaxPerformance;
        this.minPerformance = MinPerformance;
        this.bestAgent = bestAgent;
        this.worstAgent = worstAgent;
    }
    
    public void setExtraInfo(String key, double value){
        this.extraInfo.put(key, value);
    }

    public double getExtraInfo(String key){
        return this.extraInfo.get(key);
    }

    public boolean hasExtraInfoKey(String key){
        return this.extraInfo.containsKey(key);
    }

    /**
     * @return the replacedAgents
     */
    public int getReplacedAgents() {
        return replacedAgents;
    }

    /**
     * @return the avgPerformance
     */
    public double getAvgPerformance() {
        return avgPerformance;
    }

    /**
     * @return the maxPerformance
     */
    public double getMaxPerformance() {
        return maxPerformance;
    }

    /**
     * @return the minPerformance
     */
    public double getMinPerformance() {
        return minPerformance;
    }

    /**
     * @return the bestAgent
     */
    public PtpNeuralNetwork getBestAgent() {
        return bestAgent;
    }

    /**
     * @return the worstAgent
     */
    public PtpNeuralNetwork getWorstAgent() {
        return worstAgent;
    }
    
}
