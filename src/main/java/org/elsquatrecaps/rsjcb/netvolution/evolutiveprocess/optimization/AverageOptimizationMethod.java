/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author josep
 */
@OptimizationMethodInfo(id=SurviveOptimizationMethodValues.AVERAGE,  needAveragePerformanceValue = true)
public class AverageOptimizationMethod extends OptimizationMethod{

    @Override
    public DataToEvaluateOptimization getDataToEvaluateOptimization(AgentOptimizationValuesForReproduction[] evolutiveValues){
        int posMinPerformace=0;
        int posMaxPerformace=0;
        int worstCounter=0;
        BigDecimal sumRepAdv=BigDecimal.ZERO;
        Arrays.sort(evolutiveValues);
        List<AgentOptimizationValuesForReproduction> bestAgents = new ArrayList<>();
        for(int i=0; i<evolutiveValues.length; i++){
            if(evolutiveValues[i].getVitalAdvantage().compareTo(this.getAveragePerformance())>0){
                bestAgents.add(evolutiveValues[i]);
            }else if(evolutiveValues[i].getVitalAdvantage().compareTo(this.getAveragePerformance())==0){
                bestAgents.add(evolutiveValues[i]);
                worstCounter++;
            }else{
                worstCounter++;
            }
            if(evolutiveValues[i].getVitalAdvantage().compareTo(evolutiveValues[posMinPerformace].getVitalAdvantage())<0){
                posMinPerformace=i;
            }
            if(evolutiveValues[i].getVitalAdvantage().compareTo(evolutiveValues[posMaxPerformace].getVitalAdvantage())>0){
                posMaxPerformace=i;
            }
        }
        BigDecimal perOne = BigDecimal.valueOf(1.0/bestAgents.size());
        for(int i=1; i<bestAgents.size(); i++){
            BigDecimal ra = bestAgents.get(i).getReporductiveAdvantageValue().subtract(bestAgents.get(0).getReporductiveAdvantageValue());
            sumRepAdv = sumRepAdv.add(ra.add(perOne));
            bestAgents.get(i).reproductionRate = sumRepAdv;
        }
        for(int i=1; i<bestAgents.size(); i++){
            bestAgents.get(i).reproductionRate = bestAgents.get(i).reproductionRate.divide(sumRepAdv, 30, RoundingMode.HALF_UP);
        }
        return new DataToEvaluateOptimization(posMinPerformace, posMaxPerformace, bestAgents, worstCounter);
    }    
}
