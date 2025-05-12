package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import java.util.List;

/**
 *
 * @author josep
 * @param <T>
 */
public abstract class AbstractPtpNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder<T extends AbstractPtpNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder> extends PtpNeuralNetworkTrueTableDataBuilder<T>{

    public <T extends PtpNeuralNetworkTrueTableEvolutionaryEnvironment> T build(){
        PtpNeuralNetworkTrueTableEvolutionaryCycleProcessorBuilder cBuilder = new PtpNeuralNetworkTrueTableEvolutionaryCycleProcessorBuilder();
        cBuilder.setPopulation(population)
                .setMutationProcessor(mutationProcessor)
                .setOptimizationMethod(optimizationMethod)
                .setPerformanceCalculator(performanceCalculator)
                .setNnPropertiesToFollow(nnPropertiesToFollow)
                .setSelectionBestCandidateMethod(selectionBestCandidateMethod)
                .setSurvivalRateForOptimizationMethod(survivalRateForOptimizationMethod);
        
        return instanceEnvironment(cBuilder.build());        
    }
    
    protected abstract <T extends PtpNeuralNetworkTrueTableEvolutionaryEnvironment> T instanceEnvironment(PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor cycleProcessor);
    
}
