package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import java.util.List;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.OptimizationMethod;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkMutationProcessor;

/**
 *
 * @author josep
 * @param <T>
 */
public class PtpNeuralNetworkTrueTableDataBuilder <T extends PtpNeuralNetworkTrueTableDataBuilder> {
    
    protected PtpNeuralNetwork[] population;
    protected PtpNeuralNetworkMutationProcessor mutationProcessor;
    protected PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator;
    protected List<String> nnPropertiesToFollow;
    protected List<String> infoPropertiesToFollow;
    protected OptimizationMethod optimizationMethod;
    protected double survivalRateForOptimizationMethod = 0.5;
    protected SelectionBestCandidateMethod selectionBestCandidateMethod = SelectionBestCandidateMethod.ALL_THE_BEST_EQUALLY;

    public PtpNeuralNetworkTrueTableDataBuilder() {
    }

    /**
     * @param population the population to set
     * @return
     */
    public T setPopulation(PtpNeuralNetwork[] population) {
        this.population = population;
        return (T) this;
    }

    /**
     * @param performanceCalculator the performanceCalculator to set
     * @return
     */
    public T setPerformanceCalculator(PtpNeuralNetworkTrueTableGlobalCalculator performanceCalculator) {
        this.performanceCalculator = performanceCalculator;
        return (T) this;
    }

    /**
     * @param mutationProcessor the mutationProcessor to set
     * @return
     */
    public T setMutationProcessor(PtpNeuralNetworkMutationProcessor mutationProcessor) {
        this.mutationProcessor = mutationProcessor;
        return (T) this;
    }

    /**
     * @param propertiesToFollow the properties to follow
     * @return
     */
    public T setNnPropertiesToFollow(List<String> propertiesToFollow) {
        this.nnPropertiesToFollow = propertiesToFollow;
        return (T) this;
    }

    /**
     * @param optimizationMethod the optimizationMethod to set
     * @return
     */
    public T setOptimizationMethod(OptimizationMethod optimizationMethod) {
        this.optimizationMethod = optimizationMethod;
        return (T) this;
    }

    /**
     * @param survivalRateForOptimizationMethod the survivalRateForOptimizationMethod to set
     * @return
     */
    public T setSurvivalRateForOptimizationMethod(double survivalRateForOptimizationMethod) {
        this.survivalRateForOptimizationMethod = survivalRateForOptimizationMethod;
        return (T) this;
    }

    /**
     * @param selectionBestCandidateMethod the selectionBestCandidateMethod to set
     * @return
     */
    public T setSelectionBestCandidateMethod(SelectionBestCandidateMethod selectionBestCandidateMethod) {
        this.selectionBestCandidateMethod = selectionBestCandidateMethod;
        return (T) this;
    }
    
    /**
     * @param propertiesToFollow the properties to follow
     * @return
     */
    public T setInfoPropertiesToFollow(List<String> propertiesToFollow) {
        this.infoPropertiesToFollow = propertiesToFollow;
        return (T) this;
    }



}
