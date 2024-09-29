/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.AgentOptimizationValuesForReproduction;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.SurviveOptimizationMethodValues;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkSinglePropertyCalculator;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.SinglePropertyCalculatorItems;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.lineage.ParentLine;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.AverageInizializationForOptimizationMethod;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.DataToEvaluateOptimization;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.OptimizationMethod;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.OptimizeMethodItems;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.util.random.RandomFactory;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkMutationProcessor;

/**
 *
 * @author josep
 */
public class PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor {
    private final ParentLine<PtpNeuralNetwork>[] progenyLines;
    private final PtpNeuralNetwork[] population;
    private PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator;
    private final PtpNeuralNetworkMutationProcessor mutationProcessor;
    private Map<String, PtpNeuralNetworkSinglePropertyCalculator> propertiesToReport = new HashMap<>();
    private OptimizationMethod optimizationMethod;
//    private SurviveOptimizationMethodValues surviveOptimizationMethod;
    private double survivalRateForOptimizationMethod=0.5;
//    private double maxSurvivalRateForOptimizationMethod=1.0;
    boolean keepProgenyLines;



    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
            PtpNeuralNetwork[] population, 
            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
            PtpNeuralNetworkMutationProcessor mutationProcessor, 
            List<String> propertiesToFollow,
            OptimizationMethod optimizationMethod,
//            SurviveOptimizationMethodValues surviveOptimizationMethod, 
            boolean keepProgenyLines) {
        this(population, performanceCalculator, mutationProcessor, propertiesToFollow, optimizationMethod, 0.1, keepProgenyLines);
    }
    
    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
            PtpNeuralNetwork[] population, 
            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
            PtpNeuralNetworkMutationProcessor mutationProcessor, 
            List<String> propertiesToFollow,
            OptimizationMethod optimizationMethod,
//            SurviveOptimizationMethodValues surviveOptimizationMethod, 
            double survivalRate,
//            double maxSurvivalRate,
            boolean keepProgenyLines) {
        SinglePropertyCalculatorItems item;
        this.population = population;
        this.performanceCalculator = performanceCalculator;
        this.mutationProcessor = mutationProcessor;
        this.optimizationMethod = optimizationMethod;
//        this.surviveOptimizationMethod = surviveOptimizationMethod;
        this.survivalRateForOptimizationMethod = survivalRate;
//        this.maxSurvivalRateForOptimizationMethod = maxSurvivalRate;
        this.keepProgenyLines = keepProgenyLines;
        if(keepProgenyLines){
            progenyLines = new ParentLine[population.length];
            for(int i=0; i<population.length; i++){
                progenyLines[i] = new ParentLine(null, population[i]);
            }
        }else{
            progenyLines = null;
        }
        for(String k: propertiesToFollow){
            item = SinglePropertyCalculatorItems.getItem(k);
            this.propertiesToReport.put(item.getId(),  item.getInstance());
        }        
    }
    
    public  ParentLine<PtpNeuralNetwork>[] getProgenyLines(){
        return progenyLines;
    }
    
    public EvolutionaryCycleInfo evaluateAndRenewPopulation(){
        Map<String, Float> extraData = new HashMap<>();
                
//        Float sumRepAdv=0f;
        BigDecimal sumVitalAdv=BigDecimal.ZERO;
        BigDecimal sumPerf=BigDecimal.ZERO;
        BigDecimal minPerf=BigDecimal.valueOf(Long.MAX_VALUE);
        BigDecimal maxPerf=BigDecimal.valueOf(Long.MIN_VALUE);
        AgentOptimizationValuesForReproduction[] positiveResults = new AgentOptimizationValuesForReproduction[population.length];
        for(int p=0; p<population.length; p++){
            PtpNeuralNetwork agent = population[p];
            positiveResults[p]=new AgentOptimizationValuesForReproduction(p, performanceCalculator.calculate(agent));
            sumVitalAdv = sumVitalAdv.add(positiveResults[p].getVitalAdvantage());
            sumPerf = sumPerf.add(positiveResults[p].getPerformance());
            if(minPerf.compareTo(positiveResults[p].getPerformance())>0){
                minPerf= positiveResults[p].getPerformance();
            }
            if(maxPerf.compareTo(positiveResults[p].getPerformance())<0){
                maxPerf = positiveResults[p].getPerformance();
            }
            propertiesToReport.forEach((String k, PtpNeuralNetworkSinglePropertyCalculator c) -> {                
                Float f = extraData.get(k);
                if(f==null){
                    f=0f;
                }
                f = f + c.calculate(agent).floatValue();
                extraData.put(k, (float) f);
            });
        }
        BigDecimal vitalAdv = sumVitalAdv.divide(new BigDecimal(population.length), 18, RoundingMode.HALF_UP);
        BigDecimal perf = sumPerf.divide(new BigDecimal(population.length), 18, RoundingMode.HALF_UP);
        EvolutionaryCycleInfo ret = renewPopulation(positiveResults, new AverageInizializationForOptimizationMethod(vitalAdv));   
        ret.setAvgPerformance(perf.doubleValue());
        ret.setMaxPerformance(maxPerf.doubleValue());
        ret.setMinPerformance(minPerf.doubleValue());
        extraData.forEach((String k, Float u) -> {
            ret.setExtraInfo(k, u/population.length);
        });
        return ret;
    }
    
    private EvolutionaryCycleInfo renewPopulation(
            AgentOptimizationValuesForReproduction[] evolutiveValues
            , AverageInizializationForOptimizationMethod averageVitalAdv) {
        EvolutionaryCycleInfo ret;
        int toKill;
        int killed= 0;
//        OptimizeMethodItems factory = OptimizeMethodItems.getItem(surviveOptimizationMethod.getValue());
//        OptimizationMethod om = factory.getInstance();
        optimizationMethod.initProcess(averageVitalAdv);
        optimizationMethod.updateDataToEvaluateOptimization(evolutiveValues);
        List<AgentOptimizationValuesForReproduction> bestAgents = optimizationMethod.getBestAgents();

        toKill = Math.min(optimizationMethod.getWorstAgentCounter(), Math.max(1,(int) (evolutiveValues.length*(1 - this.survivalRateForOptimizationMethod))));
        for(int i=0; killed < toKill && i<evolutiveValues.length; i++){
            if(optimizationMethod.mustDeath(i)){
                killed++;
                int pos = selectBetterPosFromRandom(bestAgents, 0);
                PtpNeuralNetwork mutated = mutationProcessor.muteFrom(population[pos]);
                if(keepProgenyLines){
                    progenyLines[evolutiveValues[i].getId()] = new ParentLine<>(progenyLines[pos], mutated);
                }
                population[evolutiveValues[i].getId()] = mutated;
            }
        }
        ret = new EvolutionaryCycleInfo(killed, averageVitalAdv.getAveragePerformance().doubleValue(), 
                evolutiveValues[optimizationMethod.getPosMaxPerformace()].getVitalAdvantage().doubleValue(), //[TODO: REVISAR AIXÒ S'ordena per vitalAdvantage i potser no és el maxim perfrmance]
                evolutiveValues[optimizationMethod.getPosMinPerformace()].getVitalAdvantage().doubleValue(),//[TODO: REVISAR AIXÒ S'ordena per vitalAdvantage i potser no és el mínim perfrmance]
                population[evolutiveValues[optimizationMethod.getPosMaxPerformace()].getId()], 
                population[evolutiveValues[optimizationMethod.getPosMinPerformace()].getId()]);
        return ret;
    }
    
    private int selectBetterPosFromRandom(List<AgentOptimizationValuesForReproduction> positiveResults, int fromPos){
        int pos=positiveResults.size()-1;
        double p = RandomFactory.getRandomInstance().nextDouble();
        while(pos>fromPos && p<positiveResults.get(pos-1).getReproductionRate().doubleValue()){
            pos--;
        }
        return positiveResults.get(pos).getId();
    }    
}
