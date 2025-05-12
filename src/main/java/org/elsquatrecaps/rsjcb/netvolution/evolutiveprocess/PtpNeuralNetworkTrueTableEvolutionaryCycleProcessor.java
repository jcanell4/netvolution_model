package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.AgentOptimizationValuesForReproduction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.EnvironmentInfoPropertyCalculator;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.EnvironmentInfoPropertyCalculatorItems;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkSinglePropertyCalculator;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.SinglePropertyCalculatorItems;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.AverageInizializationForOptimizationMethod;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.OptimizationMethod;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.util.random.RandomFactory;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkMutationProcessor;

/**
 *
 * @author josep
 */
public class PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor {
    private final PtpNeuralNetwork[] population;
    private PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator;
    private final PtpNeuralNetworkMutationProcessor mutationProcessor;
    private Map<String, PtpNeuralNetworkSinglePropertyCalculator> propertiesToReport = new HashMap<>();
    private Map<String, EnvironmentInfoPropertyCalculator> infoPropertiesToFollow = new HashMap<>();
    private OptimizationMethod optimizationMethod;
    private double survivalRateForOptimizationMethod=0.5;
    private SelectionBestCandidateMethod selectionBestCandidateMethod = SelectionBestCandidateMethod.ALL_THE_BEST_EQUALLY;

    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
                PtpNeuralNetwork[] population, 
                PtpNeuralNetworkMutationProcessor mutationProcessor){
        this.population = population;
        this.mutationProcessor = mutationProcessor;
    }

//    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
//            PtpNeuralNetwork[] population, 
//            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
//            PtpNeuralNetworkMutationProcessor mutationProcessor, 
//            List<String> propertiesToFollow,
//            OptimizationMethod optimizationMethod,
////            SurviveOptimizationMethodValues surviveOptimizationMethod, 
//            boolean keepProgenyLines) {
//        this(population, performanceCalculator, mutationProcessor, propertiesToFollow, optimizationMethod, 0.1, keepProgenyLines);
//    }
    
//    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
//            PtpNeuralNetwork[] population, 
//            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
//            PtpNeuralNetworkMutationProcessor mutationProcessor, 
//            List<String> propertiesToFollow,
//            OptimizationMethod optimizationMethod,
////            SurviveOptimizationMethodValues surviveOptimizationMethod, 
//            double survivalRate,
////            double maxSurvivalRate,
//            boolean keepProgenyLines) {
//        SinglePropertyCalculatorItems item;
//        this.population = population;
//        this.performanceCalculator = performanceCalculator;
//        this.mutationProcessor = mutationProcessor;
//        this.optimizationMethod = optimizationMethod;
////        this.surviveOptimizationMethod = surviveOptimizationMethod;
//        this.survivalRateForOptimizationMethod = survivalRate;
////        this.maxSurvivalRateForOptimizationMethod = maxSurvivalRate
    /// @return ;
//        this.keepProgenyLines = keepProgenyLines;
//        if(keepProgenyLines){
//            progenyLines = new ParentLine[population.length];
//            for(int i=0; i<population.length; i++){
//                progenyLines[i] = new ParentLine(null, population[i]);
//            }
//        }else{
//            progenyLines = null;
//        }
//        for(String k: propertiesToFollow){
//            item = SinglePropertyCalculatorItems.getItem(k);
//            this.propertiesToReport.put(item.getId(),  item.getInstance());
//        }        
//    }
    
//    public  ParentLine<PtpNeuralNetwork>[] getProgenyLines(){
//        return progenyLines;
//    }
    
    public EvolutionaryCycleInfo evaluateAndRenewPopulation(){
        Map<String, Float> extraData = new HashMap<>();
                
//        Float sumRepAdv=0f;
        BigDecimal sumVitalAdv=BigDecimal.ZERO;
        BigDecimal sumPerf=BigDecimal.ZERO;
        BigDecimal minPerf=BigDecimal.valueOf(Long.MAX_VALUE);
        BigDecimal maxPerf=BigDecimal.valueOf(Long.MIN_VALUE);
        AgentOptimizationValuesForReproduction[] positiveResults = new AgentOptimizationValuesForReproduction[getPopulation().length];
        for(int p=0; p<getPopulation().length; p++){
            PtpNeuralNetwork agent = getPopulation()[p];
            positiveResults[p]=new AgentOptimizationValuesForReproduction(p, getPerformanceCalculator().calculate(agent));
            sumVitalAdv = sumVitalAdv.add(positiveResults[p].getVitalAdvantage());
            sumPerf = sumPerf.add(positiveResults[p].getPerformance());
            if(minPerf.compareTo(positiveResults[p].getPerformance())>0){
                minPerf= positiveResults[p].getPerformance();
            }
            if(maxPerf.compareTo(positiveResults[p].getPerformance())<0){
                maxPerf = positiveResults[p].getPerformance();
            }
            getPropertiesToReport().forEach((String k, PtpNeuralNetworkSinglePropertyCalculator c) -> {                
                Float f = extraData.get(k);
                if(f==null){
                    f=0f;
                }
                f = f + c.calculate(agent).floatValue();
                extraData.put(k, (float) f);
            });
        }
        BigDecimal vitalAdv = sumVitalAdv.divide(new BigDecimal(getPopulation().length), 18, RoundingMode.HALF_UP);
        BigDecimal perf = sumPerf.divide(new BigDecimal(getPopulation().length), 18, RoundingMode.HALF_UP);
        EvolutionaryCycleInfo ret = renewPopulation(positiveResults, new AverageInizializationForOptimizationMethod(vitalAdv));   
        ret.setAvgPerformance(perf.doubleValue());
        ret.setMaxPerformance(maxPerf.doubleValue());
        ret.setMinPerformance(minPerf.doubleValue());
        extraData.forEach((String k, Float u) -> {
            ret.setExtraInfo(k, u/getPopulation().length);
        });
        getInfoPropertiesToReport().forEach((String k, EnvironmentInfoPropertyCalculator c) -> {                
            ret.setExtraInfo(k, c.calculate(ret, population).doubleValue());

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
        getOptimizationMethod().initProcess(averageVitalAdv);
        getOptimizationMethod().updateDataToEvaluateOptimization(evolutiveValues);
        List<AgentOptimizationValuesForReproduction> bestAgents = getOptimizationMethod().getBestAgents();

        toKill = Math.min(getOptimizationMethod().getWorstAgentCounter(), Math.max(1,(int) (evolutiveValues.length*(1 - this.getSurvivalRateForOptimizationMethod()))));
        for(int i=0; killed < toKill && i<evolutiveValues.length; i++){
            if(getOptimizationMethod().mustDeath(i)){
                killed++;
                int pos = selectBetterPosFromRandom(bestAgents, 0);
                PtpNeuralNetwork mutated = getMutationProcessor().muteFrom(getPopulation()[pos]);
//                if(isKeepProgenyLines()){
//                    getProgenyLines()[evolutiveValues[i].getId()] = new ParentLine<>(getProgenyLines()[pos], mutated);
//                }
                population[evolutiveValues[i].getId()] = mutated;
            }
        }
        ret = new EvolutionaryCycleInfo(killed, averageVitalAdv.getAveragePerformance().doubleValue(), 
                evolutiveValues[getOptimizationMethod().getPosMaxPerformace()].getVitalAdvantage().doubleValue(), //[TODO: REVISAR AIXÒ S'ordena per vitalAdvantage i potser no és el maxim perfrmance]
                evolutiveValues[getOptimizationMethod().getPosMinPerformace()].getVitalAdvantage().doubleValue(),//[TODO: REVISAR AIXÒ S'ordena per vitalAdvantage i potser no és el mínim perfrmance]
                getPopulation()[evolutiveValues[getOptimizationMethod().getPosMaxPerformace()].getId()], 
                getPopulation()[evolutiveValues[getOptimizationMethod().getPosMinPerformace()].getId()]);
        return ret;
    }
    
    private int selectBetterPosFromRandom(List<AgentOptimizationValuesForReproduction> positiveResults, int fromPos){
        int ret;
        if(getSelectionBestCandidateMethod().equals(SelectionBestCandidateMethod.ALL_THE_BEST_EQUALLY)){
            ret = selectAllBetterMethod(positiveResults, fromPos);
        }else{
            ret = selectProportionalBetterMethod(positiveResults, fromPos);
        }
        return ret;
    }
    
    private int selectProportionalBetterMethod(List<AgentOptimizationValuesForReproduction> positiveResults, int fromPos){
        int pos=positiveResults.size()-1;
        double p = RandomFactory.getRandomInstance().nextDouble();
        while(pos>fromPos && p<positiveResults.get(pos-1).getReproductionRate().doubleValue()){
            pos--;
        }
        return positiveResults.get(pos).getId();
    }    

    private int selectAllBetterMethod(List<AgentOptimizationValuesForReproduction> positiveResults, int fromPos){
        int pos=positiveResults.size();
        int p = RandomFactory.getRandomInstance().nextInt(fromPos, pos);
        return positiveResults.get(p).getId();
    }    

//    /**
//     * @param progenyLines the progenyLines to set
//     */
//    public void setProgenyLines(ParentLine<PtpNeuralNetwork>[] progenyLines) {
//        this.setProgenyLines(progenyLines);
//    }
//
//    /**
//     * @param progenyLines the progenyLines to set
//     */
//    public void setProgenyLines(ParentLine<PtpNeuralNetwork>[] progenyLines) {
//        this.progenyLines = progenyLines;
//    }

    /**
     * @return the population
     */
    public PtpNeuralNetwork[] getPopulation() {
        return population;
    }

    /**
     * @return the performanceCalculator
     */
    public PtpNeuralNetworkTrueTableGlobalCalculator getPerformanceCalculator() {
        return performanceCalculator;
    }

    /**
     * @param performanceCalculator the performanceCalculator to set
     * @return 
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor setPerformanceCalculator(PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator) {
        this.performanceCalculator = performanceCalculator;
        return this;
    }

    /**
     * @return the mutationProcessor
     */
    public PtpNeuralNetworkMutationProcessor getMutationProcessor() {
        return mutationProcessor;
    }

    /**
     * @return the propertiesToReport
     */
    public Map<String, PtpNeuralNetworkSinglePropertyCalculator> getPropertiesToReport() {
        return propertiesToReport;
    }

    /**
     * @param propertiesToFollow the properties to follow during the evolution process
     * @return 
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor setPropertiesToFollow(List<String> propertiesToFollow) {
        for(String k: propertiesToFollow){
            SinglePropertyCalculatorItems item = SinglePropertyCalculatorItems.getItem(k);
            this.propertiesToReport.put(item.getId(),  item.getInstance());
        } 
        return this;
    }
    
    public Map<String, EnvironmentInfoPropertyCalculator> getInfoPropertiesToReport() {
        return infoPropertiesToFollow;
    }

    /**
     * @param propertiesToFollow the properties to follow during the evolution process
     * @return 
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor setInfoPropertiesToFollow(List<String> propertiesToFollow) {
        for(String k: propertiesToFollow){
            EnvironmentInfoPropertyCalculatorItems item = EnvironmentInfoPropertyCalculatorItems.getItem(k);
            this.infoPropertiesToFollow.put(item.getId(),  item.getInstance());
        } 
        return this;
    }    

    /**
     * @return the optimizationMethod
     */
    public OptimizationMethod getOptimizationMethod() {
        return optimizationMethod;
    }

    /**
     * @param optimizationMethod the optimizationMethod to set
     * @return 
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor setOptimizationMethod(OptimizationMethod optimizationMethod) {
        this.optimizationMethod = optimizationMethod;
        return this;
    }

    /**
     * @return the survivalRateForOptimizationMethod
     */
    public double getSurvivalRateForOptimizationMethod() {
        return survivalRateForOptimizationMethod;
    }

    /**
     * @param survivalRateForOptimizationMethod the survivalRateForOptimizationMethod to set
     * @return 
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor setSurvivalRateForOptimizationMethod(double survivalRateForOptimizationMethod) {
        this.survivalRateForOptimizationMethod = survivalRateForOptimizationMethod;
        return this;
    }

    /**
     * @return the selectionBestCandidateMethod
     */
    protected SelectionBestCandidateMethod getSelectionBestCandidateMethod() {
        return selectionBestCandidateMethod;
    }

    /**
     * @param selectionBestCandidateMethod the selectionBestCandidateMethod to set
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor setSelectionBestCandidateMethod(SelectionBestCandidateMethod selectionBestCandidateMethod) {
        this.selectionBestCandidateMethod = selectionBestCandidateMethod;
        return this;
    }
}
