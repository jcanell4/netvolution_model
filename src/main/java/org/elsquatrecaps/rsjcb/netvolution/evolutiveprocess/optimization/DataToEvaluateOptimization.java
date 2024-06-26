/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.util.List;

/**
 *
 * @author josep
 */
public class DataToEvaluateOptimization {
    
    private int posMinPerformace;
    private int posMaxPerformace;
    protected int worstAgentCounter;
    private List<AgentOptimizationValuesForReproduction> bestAgents;

    public DataToEvaluateOptimization(int posMinPerformace, int posMaxPerformace, List<AgentOptimizationValuesForReproduction> bestAgents, int worstAgentCounter) {
        this.posMinPerformace = posMinPerformace;
        this.posMaxPerformace = posMaxPerformace;
        this.bestAgents = bestAgents;
        this.worstAgentCounter = worstAgentCounter;
    }

    /**
     * @return the posMinPerformace
     */
    public int getPosMinPerformace() {
        return posMinPerformace;
    }

    /**
     * @return the posMaxPerformace
     */
    public int getPosMaxPerformace() {
        return posMaxPerformace;
    }

    /**
     * @return the bestAgents
     */
    public List<AgentOptimizationValuesForReproduction> getBestAgents() {
        return bestAgents;
    }

    /**
     * @return the worstAgentsCounter
     */
    public int getWorstAgentCounter() {
        return worstAgentCounter;
    }
    
}
