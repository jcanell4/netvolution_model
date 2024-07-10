/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author josep
 */
@OptimizationMethodInfo(id=SurviveOptimizationMethodValues.FIX_DEATH_TAX, forInitalizingInstanceFromConfig = {"evolutionarySystem.evolutionProcessConditions.fixDeathTaxValue"})
public class FixDeathTaxOptimizationMethod extends OptimizationMethod{
    private Double deathTax;
    private int maxPosition=-1;
    
    @Override
    public void initInstance(Object... v) {
        if(v.length>0 && v[0]!=null && v[0] instanceof Double){
            deathTax = (Double) v[0];
        }else if(v.length>0 && v[0]!=null && v[0] instanceof Integer){
            deathTax = ((Integer)v[0])/100.0;
        }
        maxPosition=-1;
    }

    @Override
    protected DataToEvaluateOptimization getDataToEvaluateOptimization(AgentOptimizationValuesForReproduction[] evolutiveValues){
        int posMinPerformace=0;
        int posMaxPerformace=0;
        int worstCounter=0;
        BigDecimal sumRepAdv=BigDecimal.ZERO;
        Arrays.sort(evolutiveValues);
        List<AgentOptimizationValuesForReproduction> bestAgents = new ArrayList<>();
        int l = (int) (evolutiveValues.length*deathTax);
        for(int i=0; i<l; i++){
            if(evolutiveValues[i].getVitalAdvantage().compareTo(evolutiveValues[posMinPerformace].getVitalAdvantage())<0){
                posMinPerformace=i;
            }
            if(evolutiveValues[i].getVitalAdvantage().compareTo(evolutiveValues[posMaxPerformace].getVitalAdvantage())>0){
                posMaxPerformace=i;
            }
            worstCounter++;
        }
        for(int i=l; i<evolutiveValues.length; i++){
            bestAgents.add(evolutiveValues[i]);
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
        maxPosition=-1;
        return new DataToEvaluateOptimization(posMinPerformace, posMaxPerformace, bestAgents, worstCounter);
    }    

    @Override
    public boolean mustDeath(int pos) {
        if(maxPosition==-1){
            maxPosition=(int) (this.evolutiveValues.length*this.deathTax);
        }
        return pos<maxPosition;
    }
}
