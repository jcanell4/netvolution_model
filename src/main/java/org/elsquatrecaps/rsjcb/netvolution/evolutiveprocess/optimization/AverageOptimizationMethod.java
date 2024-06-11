/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.SinglePropertyCalculatorInfo;

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
        BigDecimal sumRepAdv=BigDecimal.ZERO;
        Arrays.sort(evolutiveValues);
        List<AgentOptimizationValuesForReproduction> bestAgents = new ArrayList<>();
        for(int i=0; i<evolutiveValues.length; i++){
            if(evolutiveValues[i].getPerformance().compareTo(this.getAveragePerformance())>=0){
                bestAgents.add(evolutiveValues[i]);
            }
            if(evolutiveValues[i].getPerformance().compareTo(evolutiveValues[posMinPerformace].getPerformance())<0){
                posMinPerformace=i;
            }
            if(evolutiveValues[i].getPerformance().compareTo(evolutiveValues[posMaxPerformace].getPerformance())>0){
                posMaxPerformace=i;
            }
        }
        for(int i=0; i<bestAgents.size(); i++){
            BigDecimal ra = bestAgents.get(i).getReporductiveAdvantageValue().subtract(bestAgents.get(0).getReporductiveAdvantageValue());
            sumRepAdv = sumRepAdv.add(ra);
        }
        for(int i=1; i<bestAgents.size(); i++){
            //bestAgents.get(i).reproductionRate = bestAgents.get(i-1).reproductionRate + 
                //(bestAgents.get(i).getReporductiveAdvantageValue()-bestAgents.get(0).getReporductiveAdvantageValue())/sumRepAdv;
            BigDecimal dif = bestAgents.get(i).getReporductiveAdvantageValue().subtract(bestAgents.get(0).getReporductiveAdvantageValue());
            if(dif.compareTo(BigDecimal.ZERO)==0){
                bestAgents.get(i).reproductionRate = bestAgents.get(i-1).getReproductionRate();
            }else{
                bestAgents.get(i).reproductionRate = 
                        bestAgents.get(i-1).getReproductionRate().add(dif.divide(sumRepAdv,MathContext.DECIMAL128));
            }
        }
        return new DataToEvaluateOptimization(posMinPerformace, posMaxPerformace, bestAgents);
    }    
}
