/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.EvolutionaryCycleInfo;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;


/**
 *
 * @author josep
 */
public class FinishedEvolutionaryCycleEvent extends EvolutionaryEvent{
    private static final long serialVersionUID = 2478541542732890003L;        
    public static final String eventType ="FinishedCycle";
    private EvolutionaryCycleInfo info;
    private int cycleNumber;

    protected FinishedEvolutionaryCycleEvent(String type, int cNumber, EvolutionaryCycleInfo info) {
        super(type);
        this.cycleNumber=cNumber;
        this.info=info;
    }

    protected FinishedEvolutionaryCycleEvent(int id, String type, int cNumber, EvolutionaryCycleInfo info) {
        super(id, type);
        this.info=info;
    }

    
    public FinishedEvolutionaryCycleEvent(int cNumber, EvolutionaryCycleInfo info) {
        this(eventType, cNumber, info);
    }
    
    public FinishedEvolutionaryCycleEvent(int id, int cNumber, EvolutionaryCycleInfo info) {
        this(id, eventType, cNumber, info);
    }
    
    public double getExtraInfo(String key){
        return info.getExtraInfo(key);
    }
    
    public boolean hasExtraInfoKey(String key){
        return info.hasExtraInfoKey(key);
    }
    
    /**
     * @return the replacedAgents
     */
    public int getReplacedAgents() {
        return info.getReplacedAgents();
    }

    /**
     * @return the avgPerformance
     */
    public float getAvgPerformance() {
        return (float) info.getAvgPerformance();
    }

    /**
     * @return the maxPerformance
     */
    public float getMaxPerformance() {
        return (float) info.getMaxPerformance();
    }

    /**
     * @return the minPerformance
     */
    public float getMinPerformance() {
        return (float) info.getMinPerformance();
    }

    /**
     * @return the bestAgent
     */
    public PtpNeuralNetwork getBestAgent() {
        return info.getBestAgent();
    }

    /**
     * @return the worstAgent
     */
    public PtpNeuralNetwork getWorstAgent() {
        return info.getWorstAgent();
    }

    /**
     * @return the cycleNumber
     */
    public int getCycleNumber() {
        return cycleNumber;
    }    
}
