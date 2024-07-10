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
public abstract class OptimizationMethod {
    private AverageInizializationForOptimizationMethod averageValues;
    protected AgentOptimizationValuesForReproduction[] evolutiveValues;
    protected DataToEvaluateOptimization dataToEvaluateOptimization;
    
    public void updateDataToEvaluateOptimization(AgentOptimizationValuesForReproduction[] evolutiveValues){
        this.evolutiveValues = evolutiveValues;
        dataToEvaluateOptimization = getDataToEvaluateOptimization(this.evolutiveValues);
    }
    protected abstract DataToEvaluateOptimization getDataToEvaluateOptimization(AgentOptimizationValuesForReproduction[] evolutiveValues);
    public void initInstance(Object... v){}
    public void initProcess(AverageInizializationForOptimizationMethod v){
        this.averageValues = v;
    }
    
    public abstract boolean mustDeath(int pos);

    /**
     * @return the averageValues
     */
    public AverageInizializationForOptimizationMethod getAverageValues() {
        return averageValues;
    }
    
    public int getWorstAgentCounter() {
        return this.dataToEvaluateOptimization.getWorstAgentCounter();
    }
    
 /**
     * @return the posMinPerformace
     */
    public int getPosMinPerformace() {
        return dataToEvaluateOptimization.getPosMinPerformace();
    }

    /**
     * @return the posMaxPerformace
     */
    public int getPosMaxPerformace() {
        return dataToEvaluateOptimization.getPosMaxPerformace();
    }

    /**
     * @return the bestAgents
     */
    public List<AgentOptimizationValuesForReproduction> getBestAgents() {
        return dataToEvaluateOptimization.getBestAgents();
    }
    
}
