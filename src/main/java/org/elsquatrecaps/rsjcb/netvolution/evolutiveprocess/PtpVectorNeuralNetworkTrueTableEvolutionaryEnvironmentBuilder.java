/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import java.util.List;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkConfiguration;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkMutationProcessor;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkRandomInitializer;

/**
 *
 * @author josep
 */
public class PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder extends AbstractPtpNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder<PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder>{
    private int populationSize;
    private PtpNeuralNetworkConfiguration nnConfig;
    private Float[][] environmentInputSet;
    private Float[][] environmentOutputSet;
    private List<String> viAdv;
    private List<String> repAdv;
    private String performaceName=null;
    Float averagePerformanceForStopping=null;
    Float desiredPerformance=null;
    Integer maxTimes=null;    
    
    @Override
    public <T extends PtpNeuralNetworkTrueTableEvolutionaryEnvironment> T build(){
        PtpNeuralNetworkTrueTableEvolutionaryEnvironment ret;
        if(population==null){
            population = new PtpVectorNeuralNetwork[populationSize];
            for(int i=0; i<populationSize; i++){
                PtpVectorNeuralNetwork net = new PtpVectorNeuralNetwork();
                PtpVectorNeuralNetworkRandomInitializer.initialize(net, nnConfig);
                population[i] = net;
            }
        }
        if(mutationProcessor==null){
            mutationProcessor=new PtpVectorNeuralNetworkMutationProcessor();
            mutationProcessor.setConnectionMutationRate(nnConfig.getConnectionMutationRate());
            mutationProcessor.setDisconnectionMutationRate(nnConfig.getDisconnectionMutationRate());
            mutationProcessor.setMaxThresholdExchangeFactorValue(nnConfig.getMaxThresholdExchangeFactorValue());
            mutationProcessor.setMaxWeightExchangevalue(nnConfig.getMaxWeightExchangevalue());
            mutationProcessor.setReceiverNeuronNumberMutationRate(nnConfig.getReceiverNeuronNumberMutationRate());
            mutationProcessor.setResponseNeuronNumberMutationRate(nnConfig.getResponseNeuronNumberMutationRate());
            mutationProcessor.setThresholdMutationRate(nnConfig.getThresholdMutationRate());
            mutationProcessor.setWeightsMutationRate(nnConfig.getWeightsMutationRate());
            mutationProcessor.setInputContributionrobability(nnConfig.getInputContributionrobability());            
        }
        if(performanceCalculator==null){
            if(performaceName==null){
                performanceCalculator = new PtpNeuralNetworkTrueTableGlobalCalculator(
                        viAdv, 
                        repAdv, 
                        environmentInputSet, 
                        environmentOutputSet);
            }else{
                if(environmentInputSet.length!=environmentOutputSet.length){
                    throw new RuntimeException("The input array must be the same length as the output array");
                }
                performanceCalculator = new PtpNeuralNetworkTrueTableGlobalCalculator(
                        performaceName,
                        viAdv, 
                        repAdv, 
                        environmentInputSet, 
                        environmentOutputSet);
            }
        }
        ret = super.build();
        if(desiredPerformance!=null && averagePerformanceForStopping!=null && maxTimes!=null){
            ret.init(averagePerformanceForStopping, desiredPerformance, maxTimes);
        }
        return (T) ret;
    }

    @Override
    protected PtpNeuralNetworkTrueTableEvolutionaryEnvironment instanceEnvironment(PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor cycleProcessor) {
        return new PtpNeuralNetworkTrueTableEvolutionaryEnvironment(cycleProcessor);
    }

    /**
     * @param populationSize the populationSize to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    /**
     * @param nnConfig the nnConfig to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setNnConfig(PtpNeuralNetworkConfiguration nnConfig) {
        this.nnConfig = nnConfig;
        return this;
    }

    /**
     * @param environmentInputSet the environmentInputSet to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setEnvironmentInputSet(Float[][] environmentInputSet) {
        this.environmentInputSet = environmentInputSet;
        return this;
    }

    /**
     * @param environmentOutputSet the environmentOutputSet to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setEnvironmentOutputSet(Float[][] environmentOutputSet) {
        this.environmentOutputSet = environmentOutputSet;
        return this;
    }

    /**
     * @param viAdv the viAdv to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setViAdv(List<String> viAdv) {
        this.viAdv = viAdv;
        return this;
    }

    /**
     * @param repAdv the repAdv to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setRepAdv(List<String> repAdv) {
        this.repAdv = repAdv;
        return this;
    }

    /**
     * @param performace the performaceName to set
     * @return 
     */
    public PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder setPerformaceName(String performace) {
        this.performaceName = performace;
        return this;
    }
    
}
