/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.AgentOptimizationValuesForReproduction;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.SurviveOptimizationMethodValues;
import java.math.BigDecimal;
import java.math.MathContext;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkSinglePropertyCalculator;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.SinglePropertyCalculatorItems;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.lineage.ParentLine;
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
    private SurviveOptimizationMethodValues surviveOptimizationMethod;
    private double survivalRateForOptimizationMethod=0.5;
    boolean keepProgenyLines;



    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
            PtpNeuralNetwork[] population, 
            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
            PtpNeuralNetworkMutationProcessor mutationProcessor, 
            List<String> propertiesToFollow,
            SurviveOptimizationMethodValues surviveOptimizationMethod, 
            boolean keepProgenyLines) {
        this(population, performanceCalculator, mutationProcessor, propertiesToFollow, surviveOptimizationMethod, 0.5, keepProgenyLines);
    }
    
    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
            PtpNeuralNetwork[] population, 
            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
            PtpNeuralNetworkMutationProcessor mutationProcessor, 
            List<String> propertiesToFollow,
            SurviveOptimizationMethodValues surviveOptimizationMethod, 
            double survivalRate,
            boolean keepProgenyLines) {
        SinglePropertyCalculatorItems item;
        this.population = population;
        this.performanceCalculator = performanceCalculator;
        this.mutationProcessor = mutationProcessor;
        this.surviveOptimizationMethod = surviveOptimizationMethod;
        this.survivalRateForOptimizationMethod = survivalRate;
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
        BigDecimal sumPerf=BigDecimal.ZERO;
        AgentOptimizationValuesForReproduction[] positiveResults = new AgentOptimizationValuesForReproduction[population.length];
        for(int p=0; p<population.length; p++){
            PtpNeuralNetwork agent = population[p];
            positiveResults[p]=new AgentOptimizationValuesForReproduction(p, performanceCalculator.calculate(agent));
            sumPerf = sumPerf.add(positiveResults[p].getPerformance());
//            sumPerf += positiveResults[p].getPerformance();
            propertiesToReport.forEach((String k, PtpNeuralNetworkSinglePropertyCalculator c) -> {                
                Float f = extraData.get(k);
                if(f==null){
                    f=0f;
                }
                f = f + c.calculate(agent).floatValue();
                extraData.put(k, (float) f);
            });
        }
        BigDecimal perf = sumPerf.divide(new BigDecimal(population.length), MathContext.DECIMAL128);
        EvolutionaryCycleInfo ret = renewPopulation(positiveResults, perf/*, repAdv*/);        

        extraData.forEach((String k, Float u) -> {
            ret.setExtraInfo(k, u/population.length);
        });
        return ret;
    }
    
    private EvolutionaryCycleInfo renewPopulation(AgentOptimizationValuesForReproduction[] evolutiveValues, BigDecimal averagePerformance) {
        EvolutionaryCycleInfo ret;
        int toKill;
        int killed= 0;
        OptimizeMethodItems factory = OptimizeMethodItems.getItem(surviveOptimizationMethod.getValue());
        OptimizationMethod om = factory.getInstance();
        om.init(averagePerformance);
        DataToEvaluateOptimization dataToEvaluateOptimization = om.getDataToEvaluateOptimization(evolutiveValues);
        List<AgentOptimizationValuesForReproduction> bestAgents = dataToEvaluateOptimization.getBestAgents();
        
       
//        BigDecimal sumRepAdv=BigDecimal.ZERO;
//        int posMinPerformace=0;
//        int posMaxPerformace=0;

//        Arrays.sort(evolutiveValues);
//        List<AgentOptimizationValuesForReproduction> bestAgents = new ArrayList<>();
//        for(int i=0; i<evolutiveValues.length; i++){
//            if(evolutiveValues[i].getPerformance().compareTo(averagePerformance)>=0){
//                bestAgents.add(evolutiveValues[i]);
//            }
//            if(evolutiveValues[i].getPerformance().compareTo(evolutiveValues[posMinPerformace].getPerformance())<0){
//                posMinPerformace=i;
//            }
//            if(evolutiveValues[i].getPerformance().compareTo(evolutiveValues[posMaxPerformace].getPerformance())>0){
//                posMaxPerformace=i;
//            }
//        }
//        for(int i=0; i<bestAgents.size(); i++){
//            BigDecimal ra = bestAgents.get(i).getReporductiveAdvantageValue().subtract(bestAgents.get(0).getReporductiveAdvantageValue());
//            sumRepAdv = sumRepAdv.add(ra);
//        }
//        for(int i=1; i<bestAgents.size(); i++){
//            //bestAgents.get(i).reproductionRate = bestAgents.get(i-1).reproductionRate + 
//                //(bestAgents.get(i).getReporductiveAdvantageValue()-bestAgents.get(0).getReporductiveAdvantageValue())/sumRepAdv;
//            BigDecimal dif = bestAgents.get(i).getReporductiveAdvantageValue().subtract(bestAgents.get(0).getReporductiveAdvantageValue());
//            if(dif.compareTo(BigDecimal.ZERO)==0){
//                bestAgents.get(i).reproductionRate = bestAgents.get(i-1).reproductionRate;
//            }else{
//                bestAgents.get(i).reproductionRate = 
//                        bestAgents.get(i-1).reproductionRate.add(dif.divide(sumRepAdv,MathContext.DECIMAL128));
//            }
//        }
        toKill = Math.min(bestAgents.size(), Math.max(1,(int) (evolutiveValues.length*(1 - this.survivalRateForOptimizationMethod))));
        for(int i=0; killed < toKill && i<evolutiveValues.length; i++){
            if(evolutiveValues[i].getPerformance().compareTo(averagePerformance)<=0){
                killed++;
                int pos = selectBetterPosFromRandom(bestAgents, 0);
                PtpNeuralNetwork mutated = mutationProcessor.muteFrom(population[pos]);
                if(keepProgenyLines){
                    progenyLines[evolutiveValues[i].getId()] = new ParentLine<>(progenyLines[pos], mutated);
                }
                population[evolutiveValues[i].getId()] = mutated;
            }
        }
        ret = new EvolutionaryCycleInfo(killed, averagePerformance.doubleValue(), 
                evolutiveValues[dataToEvaluateOptimization.getPosMaxPerformace()].getPerformance().doubleValue(), 
                evolutiveValues[dataToEvaluateOptimization.getPosMinPerformace()].getPerformance().doubleValue(),
                population[evolutiveValues[dataToEvaluateOptimization.getPosMaxPerformace()].getId()], 
                population[evolutiveValues[dataToEvaluateOptimization.getPosMinPerformace()].getId()]);
        return ret;
    }
    
//    private EvolutionaryCycleInfo renewPopulation(IdAndPerformanceOfAgent[] evolutiveValues, Float averagePerformance) {
//        EvolutionaryCycleInfo ret;
//        int killed= 0;
//        float sum=0;
//        int posMinPerformace=0;
//        int posMaxPerformace=0;
//        List<IdAndPerformanceOfAgent> positiveResults = new ArrayList<>();         
//        for(int i=0; i<evolutiveValues.length; i++){
//            if(evolutiveValues[i].performance>=averagePerformance){
//                positiveResults.add(evolutiveValues[i]);
//                for(int p=positiveResults.size()-1; p>0 && positiveResults.get(p).compareTo(positiveResults.get(p-1))<0; p--){
//                    IdAndPerformanceOfAgent aux = positiveResults.get(p-1);
//                    positiveResults.set(p-1, positiveResults.get(p));
//                    positiveResults.set(p, aux);
//                }
//            }
//            if(evolutiveValues[i].performance<evolutiveValues[posMinPerformace].performance){
//                posMinPerformace=i;
//            }
//            if(evolutiveValues[i].performance>evolutiveValues[posMaxPerformace].performance){
//                posMaxPerformace=i;
//            }
//        }
//        for(int i=0; i<positiveResults.size(); i++){
//            float ra = positiveResults.get(i).reprodutciveAdvantage-positiveResults.get(0).reprodutciveAdvantage;
//            sum+=ra;
//        }
//        for(int i=1; i<positiveResults.size(); i++){
//            positiveResults.get(i).reproductionRate = positiveResults.get(i-1).reproductionRate + (positiveResults.get(i).reprodutciveAdvantage-positiveResults.get(0).reprodutciveAdvantage)/sum;
//        }
//        for(int i=0; i<evolutiveValues.length; i++){
//            if(evolutiveValues[i].performance<=averagePerformance){
//                killed++;
//                int pos = selectBetterPosFromRandom(positiveResults, 0);
//                population[evolutiveValues[i].id] = mutationProcessor.muteFrom(population[pos]);
//            }
//        }
//        ret = new EvolutionaryCycleInfo(killed, averagePerformance, 
//                evolutiveValues[posMaxPerformace].performance, evolutiveValues[posMinPerformace].performance,
//                population[evolutiveValues[posMaxPerformace].id], population[evolutiveValues[posMinPerformace].id]);
//        return ret;
//    }
    
    private int selectBetterPosFromRandom(List<AgentOptimizationValuesForReproduction> positiveResults, int fromPos){
        int pos=positiveResults.size()-1;
        double p = RandomFactory.getRandomInstance().nextDouble();
        while(pos>fromPos && p<positiveResults.get(pos-1).getReproductionRate().doubleValue()){
            pos--;
        }
        return positiveResults.get(pos).getId();
    }    
}
