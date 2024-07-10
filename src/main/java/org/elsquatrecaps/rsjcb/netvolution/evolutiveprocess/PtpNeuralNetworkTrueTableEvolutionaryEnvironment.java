/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.SurviveOptimizationMethodValues;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;
import org.elsquatrecaps.rsjcb.netvolution.events.CompletedEvolutionaryProcessEvent;
import org.elsquatrecaps.rsjcb.netvolution.events.EvolutionaryProcessObserver;
import org.elsquatrecaps.rsjcb.netvolution.events.FinishedEvolutionaryCycleEvent;
import org.elsquatrecaps.rsjcb.netvolution.events.InitialEvolutionaryProcessEvent;
import org.elsquatrecaps.rsjcb.netvolution.events.ProgenyLinesEvent;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.OptimizationMethod;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkMutationProcessor;
import org.elsquatrecaps.utilities.concurrence.Monitor;

/**
 *
 * @author josep
 */
public class PtpNeuralNetworkTrueTableEvolutionaryEnvironment {
    
    private final PtpNeuralNetwork[] population;
    private PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator;
    private final PtpNeuralNetworkMutationProcessor mutationProcessor;
    private float averagePerformanceForStopping;
    private float desiredPerformance;
    private int maxTimes;
    private PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor cycleProcessor = null;
    private EvolutionaryProcessObserver observer=null;
    private boolean endProcess = false;
    private Monitor<Boolean> endedProcess = new Monitor<>(false);
    private StopProcessMonitor stopProcess = new StopProcessMonitor();

    public PtpNeuralNetworkTrueTableEvolutionaryEnvironment(PtpNeuralNetwork[] population,
            PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator, 
            PtpNeuralNetworkMutationProcessor mutationProcessor, 
            List<String> propertiesToFollow, 
            OptimizationMethod optimizationMethod,
//            SurviveOptimizationMethodValues surviveOptimizationMethodValues, 
            double survivalRate,
//            double maxSurvivalRate,
            boolean keepProgenyLines
    ) {
//       SinglePropertyCalculatorItems item;
       this.population = population;
       this.performanceCalculator = performanceCalculator;
       this.mutationProcessor = mutationProcessor;
       this.cycleProcessor = new PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(
               population, 
               performanceCalculator, 
               mutationProcessor, 
               propertiesToFollow,
               optimizationMethod,
//               surviveOptimizationMethodValues,
               survivalRate,
//               maxSurvivalRate,
               keepProgenyLines
       );
    }

    public void init(float averagePerformanceForStopping, float desiredPerformance, int maxTimes) {
        this.averagePerformanceForStopping = averagePerformanceForStopping;
        this.desiredPerformance = desiredPerformance;
        this.maxTimes = maxTimes;
    }
    
    public void setEvolutionaryProcessObserver(EvolutionaryProcessObserver observer){
        this.observer=observer;
    }
    
    public void finishProcess(){
        endProcess=true;
    }
    
    public boolean isFinishing(){
        return endProcess && !endedProcess.getValue();
    }
    
    public boolean isFinished(){
        return endedProcess.getValue();
    }
    
    public void waitForFinished(){
        synchronized (endedProcess) {
            while(!endedProcess.getValue()){
                try {
                    endedProcess.wait(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Error finishg editor", ex);
                }
            }
        }
    }
    
    public void stopProcess(boolean value){
        synchronized (stopProcess) {
            stopProcess.setStop(value);
            stopProcess.notifyAll();
        }        
    }
    
    private void stopProcessEvaluation(){
        synchronized (stopProcess) {
            while (stopProcess.evalStop()) {
                try {                
                    stopProcess.wait(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PtpNeuralNetworkTrueTableEvolutionaryEnvironment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private PtpNeuralNetwork[] clonePopulation(){
        PtpNeuralNetwork[] ret = new PtpNeuralNetwork[this.population.length];
        for(int i=0; i< ret.length; i++){
            try {
                ret[i] = (PtpNeuralNetwork) population[i].clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(PtpNeuralNetworkTrueTableEvolutionaryEnvironment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }
    
    public void process() {
        //F1
        int reason;
        int times=0;
        endProcess = false;
        endedProcess.setValue(false);
        boolean goal_achieved = false;
        getObserver().updateEvent(new InitialEvolutionaryProcessEvent(
                    getAveragePerformanceForStopping(), 
                            getDesiredPerformance(), 
                                    getMaxTimes(), 
                             clonePopulation()));
        for (times = 0; !endProcess && !goal_achieved && times < getMaxTimes(); times++) {
            stopProcessEvaluation();
            EvolutionaryCycleInfo info = getCycleProcessor().evaluateAndRenewPopulation();
            goal_achieved = info.getAvgPerformance()>=getAveragePerformanceForStopping() && info.getMaxPerformance()>=getDesiredPerformance();
            getObserver().updateEvent(new FinishedEvolutionaryCycleEvent(times, info));
        }
        if(goal_achieved){
            int extraCycles = (int) (times + Math.min(1000, times*0.1));
            for(;times<extraCycles; times++){
                stopProcessEvaluation();
                EvolutionaryCycleInfo info = getCycleProcessor().evaluateAndRenewPopulation();
                getObserver().updateEvent(new FinishedEvolutionaryCycleEvent(times, info));
            }
        }
        if(getCycleProcessor().getProgenyLines()!=null){
            getObserver().updateEvent(new ProgenyLinesEvent(getCycleProcessor().getProgenyLines()));
        }
        if(goal_achieved){
            reason=CompletedEvolutionaryProcessEvent.GOAL_ACHIEVED_BY_EVOLUTION;
        }else if(endProcess){
            reason = CompletedEvolutionaryProcessEvent.PROCESS_CANCELED_BY_USER;
        }else{
            reason = CompletedEvolutionaryProcessEvent.MAXIMUM_CYCLE_NUMBER_REACHED;
        }
        getObserver().updateEvent(new CompletedEvolutionaryProcessEvent(population, reason));  
//        getObserver().close();
        synchronized(endedProcess){
            endedProcess.setValue(true);
            endedProcess.notifyAll();
        }

    }

    public float getAveragePerformanceForStopping() {
        return averagePerformanceForStopping;
    }

    public void setAveragePerformanceForStopping(float averagePerformanceForStopping) {
        this.averagePerformanceForStopping = averagePerformanceForStopping;
    }

    public float getDesiredPerformance() {
        return desiredPerformance;
    }

    public void setDesiredPerformance(float desiredPerformance) {
        this.desiredPerformance = desiredPerformance;
    }

    public int getMaxTimes() {
        return maxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
    }

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
     * @return the mutationProcessor
     */
    public PtpNeuralNetworkMutationProcessor getMutationProcessor() {
        return mutationProcessor;
    }

    /**
     * @return the cycleProcessor
     */
    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor getCycleProcessor() {
        return cycleProcessor;
    }

    /**
     * @return the observer
     */
    public EvolutionaryProcessObserver getObserver() {
        return observer;
    }
    
    static private class StopProcessMonitor{
        Boolean stop;

        public StopProcessMonitor() {
            this(false);
        }

        public StopProcessMonitor(Boolean stop) {
            this.stop = stop;
        }
        
        public void setStop(boolean val){
            stop=val;
        }
        
        public void stopProcess(){
            stop=true;
        }
        
        public void runProcess(){
            stop=false;
        }
        
        public boolean evalStop(){
            return stop;
        }        
    }    
}
