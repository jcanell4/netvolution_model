/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.math.BigDecimal;

/**
 *
 * @author josep
 */
public abstract class OptimizationMethod {
    private BigDecimal averagePerformance;
    
    public abstract DataToEvaluateOptimization getDataToEvaluateOptimization(AgentOptimizationValuesForReproduction[] evolutiveValues);
    public void init(BigDecimal v){
        this.averagePerformance = v;
    }

    /**
     * @return the averagePerformance
     */
    public BigDecimal getAveragePerformance() {
        return averagePerformance;
    }
}
