package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ActivationFunction;

/**
 *
 * @author josep
 */
public class PtpNeuralNetworkConfiguration {
    private int neuronSize;
    private int inputSize; 
    private int outputSize;
    private int inputReceiverNeuronsSize;
    private int outputResponseNeuronsSize;
    private int initialNeuronSize;
    private InputOutputContributionValues inputContribution;
    private InputOutputContributionValues outputContribution;
    private float minWeight;
    private float maxWeigt;
    private ActivationFunction activationFunction;
    private boolean hasIntermediateNeurons;
    private int loopingTimesToStabilityCheck=4;
    private float equalityIntervalToStabilityCheck=0;
    private int maxLoopsForResults=1000;
    private NeuronTypesForStabilityCheckingValues neuronsForStabilityChecking=NeuronTypesForStabilityCheckingValues.ALL;
    private int inputContributionrobability = 50;
    private int outputContributionProbability = 50;
    private int connectionProbabilityForwardInterNeuron = 80;
    private int connectionProbabilityBackwardInterNeuron = 35;
    private float thresholdMutationRate = 0.01f;
    private float maxThresholdExchangeFactorValue = 0.1f;
    private float weightsMutationRate = 0.01f;
    private float maxWeightExchangevalue = 0.1f;
    private float disconnectionMutationRate = 0.01f;
    private float connectionMutationRate = 0.01f;
    private float receiverNeuronNumberMutationRate = 0.005f;
    private float responseNeuronNumberMutationRate = 0.005f;

    /**
     * @return the neuronSize
     */
    public int getNeuronSize() {
        return neuronSize;
    }

    /**
     * @param neuronSize the neuronSize to set
     */
    public void setNeuronSize(int neuronSize) {
        this.neuronSize = neuronSize;
    }

    /**
     * @return the inputSize
     */
    public int getInputSize() {
        return inputSize;
    }

    /**
     * @param inputSize the inputSize to set
     */
    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    /**
     * @return the outputSize
     */
    public int getOutputSize() {
        return outputSize;
    }

    /**
     * @param outputSize the outputSize to set
     */
    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    /**
     * @return the inputReceivingNeuronsSize
     */
    public int getInputReceiverNeuronsSize() {
        return inputReceiverNeuronsSize;
    }

    /**
     * @param inputReceivingNeuronsSize the inputReceivingNeuronsSize to set
     */
    public void setInputReceiverNeuronsSize(int inputReceivingNeuronsSize) {
        this.inputReceiverNeuronsSize = inputReceivingNeuronsSize;
    }

    /**
     * @return the outputNeuronsSize
     */
    public int getOutputResponseNeuronsSize() {
        return outputResponseNeuronsSize;
    }

    /**
     * @param outputNeuronsSize the outputNeuronsSize to set
     */
    public void setOutputResponseNeuronsSize(int outputNeuronsSize) {
        this.outputResponseNeuronsSize = outputNeuronsSize;
    }

    /**
     * @return the inputContribution
     */
    public InputOutputContributionValues getInputContribution() {
        return inputContribution;
    }

    /**
     * @param inputContribution the inputContribution to set
     */
    public void setInputContribution(InputOutputContributionValues inputContribution) {
        this.inputContribution = inputContribution;
    }

    /**
     * @return the outputContribution
     */
    public InputOutputContributionValues getOutputContribution() {
        return outputContribution;
    }

    /**
     * @param outputContribution the outputContribution to set
     */
    public void setOutputContribution(InputOutputContributionValues outputContribution) {
        this.outputContribution = outputContribution;
    }

    /**
     * @return the minWeight
     */
    public float getMinWeight() {
        return minWeight;
    }

    /**
     * @param minWeight the minWeight to set
     */
    public void setMinWeight(float minWeight) {
        this.minWeight = minWeight;
    }

    /**
     * @return the maxWeigt
     */
    public float getMaxWeigt() {
        return maxWeigt;
    }

    /**
     * @param maxWeigt the maxWeigt to set
     */
    public void setMaxWeigt(float maxWeigt) {
        this.maxWeigt = maxWeigt;
    }

    /**
     * @return the activationFunction
     */
    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * @param activationFunction the activationFunction to set
     */
    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    /**
     * @return the hasIntermediateNeurons
     */
    public boolean getHasIntermediateNeurons() {
        return hasIntermediateNeurons;
    }

    /**
     * @param hasIntermediateNeurons the hasIntermediateNeurons to set
     */
    public void setHasIntermediateNeurons(boolean hasIntermediateNeurons) {
        this.hasIntermediateNeurons = hasIntermediateNeurons;
    }

    /**
     * @return the loopingTimesToStabilityCheck
     */
    public int getLoopingTimesToStabilityCheck() {
        return loopingTimesToStabilityCheck;
    }

    /**
     * @param loopingTimesToStabilityCheck the loopingTimesToStabilityCheck to set
     */
    public void setLoopingTimesToStabilityCheck(int loopingTimesToStabilityCheck) {
        this.loopingTimesToStabilityCheck = loopingTimesToStabilityCheck;
    }

    /**
     * @return the equalityIntervalToStabilityCheck
     */
    public float getEqualityIntervalToStabilityCheck() {
        return equalityIntervalToStabilityCheck;
    }

    /**
     * @param equalityIntervalToStabilityCheck the equalityIntervalToStabilityCheck to set
     */
    public void setEqualityIntervalToStabilityCheck(float equalityIntervalToStabilityCheck) {
        this.equalityIntervalToStabilityCheck = equalityIntervalToStabilityCheck;
    }

    /**
     * @return the maxLoopsForResults
     */
    public int getMaxLoopsForResults() {
        return maxLoopsForResults;
    }

    /**
     * @param maxLoopsForResults the maxLoopsForResults to set
     */
    public void setMaxLoopsForResults(int maxLoopsForResults) {
        this.maxLoopsForResults = maxLoopsForResults;
    }

    /**
     * @return the neuronsForStabilityChecking
     */
    public NeuronTypesForStabilityCheckingValues getNeuronsForStabilityChecking() {
        return neuronsForStabilityChecking;
    }

    /**
     * @param neuronsForStabilityChecking the neuronsForStabilityChecking to set
     */
    public void setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues neuronsForStabilityChecking) {
        this.neuronsForStabilityChecking = neuronsForStabilityChecking;
    }

    /**
     * @return the initialNeuronSize
     */
    public int getInitialNeuronSize() {
        return initialNeuronSize;
    }

    /**
     * @param initialNeuronSize the initialNeuronSize to set
     */
    public void setInitialNeuronSize(int initialNeuronSize) {
        this.initialNeuronSize = initialNeuronSize;
    }

    /**
     * @return the inputContributionrobability
     */
    public int getInputContributionrobability() {
        return inputContributionrobability;
    }

    /**
     * @param inputContributionrobability the inputContributionrobability to set
     */
    public void setInputContributionrobability(int inputContributionrobability) {
        this.inputContributionrobability = inputContributionrobability;
    }

    /**
     * @return the outputContributionProbability
     */
    public int getOutputContributionProbability() {
        return outputContributionProbability;
    }

    /**
     * @param outputContributionProbability the outputContributionProbability to set
     */
    public void setOutputContributionProbability(int outputContributionProbability) {
        this.outputContributionProbability = outputContributionProbability;
    }

    /**
     * @return the connectionProbabilityForwardInterNeuron
     */
    public int getConnectionProbabilityForwardInterNeuron() {
        return connectionProbabilityForwardInterNeuron;
    }

    /**
     * @param connectionProbabilityForwardInterNeuron the connectionProbabilityForwardInterNeuron to set
     */
    public void setConnectionProbabilityForwardInterNeuron(int connectionProbabilityForwardInterNeuron) {
        this.connectionProbabilityForwardInterNeuron = connectionProbabilityForwardInterNeuron;
    }

    /**
     * @return the connectionProbabilityBackwardInterNeuron
     */
    public int getConnectionProbabilityBackwardInterNeuron() {
        return connectionProbabilityBackwardInterNeuron;
    }

    /**
     * @param connectionProbabilityBackwardInterNeuron the connectionProbabilityBackwardInterNeuron to set
     */
    public void setConnectionProbabilityBackwardInterNeuron(int connectionProbabilityBackwardInterNeuron) {
        this.connectionProbabilityBackwardInterNeuron = connectionProbabilityBackwardInterNeuron;
    }    

    /**
     * @return the thresholdMutationRate
     */
    public float getThresholdMutationRate() {
        return thresholdMutationRate;
    }

    /**
     * @param thresholdMutationRate the thresholdMutationRate to set
     */
    public void setThresholdMutationRate(float thresholdMutationRate) {
        this.thresholdMutationRate = thresholdMutationRate;
    }

    /**
     * @return the maxThresholdExchangeFactorValue
     */
    public float getMaxThresholdExchangeFactorValue() {
        return maxThresholdExchangeFactorValue;
    }

    /**
     * @param maxThresholdExchangeFactorValue the maxThresholdExchangeFactorValue to set
     */
    public void setMaxThresholdExchangeFactorValue(float maxThresholdExchangeFactorValue) {
        this.maxThresholdExchangeFactorValue = maxThresholdExchangeFactorValue;
    }

    /**
     * @return the weightsMutationRate
     */
    public float getWeightsMutationRate() {
        return weightsMutationRate;
    }

    /**
     * @param weightsMutationRate the weightsMutationRate to set
     */
    public void setWeightsMutationRate(float weightsMutationRate) {
        this.weightsMutationRate = weightsMutationRate;
    }

    /**
     * @return the maxWeightExchangevalue
     */
    public float getMaxWeightExchangevalue() {
        return maxWeightExchangevalue;
    }

    /**
     * @param maxWeightExchangevalue the maxWeightExchangevalue to set
     */
    public void setMaxWeightExchangevalue(float maxWeightExchangevalue) {
        this.maxWeightExchangevalue = maxWeightExchangevalue;
    }

    /**
     * @return the disconnectionMutationRate
     */
    public float getDisconnectionMutationRate() {
        return disconnectionMutationRate;
    }

    /**
     * @param disconnectionMutationRate the disconnectionMutationRate to set
     */
    public void setDisconnectionMutationRate(float disconnectionMutationRate) {
        this.disconnectionMutationRate = disconnectionMutationRate;
    }

    /**
     * @return the connectionMutationRate
     */
    public float getConnectionMutationRate() {
        return connectionMutationRate;
    }

    /**
     * @param connectionMutationRate the connectionMutationRate to set
     */
    public void setConnectionMutationRate(float connectionMutationRate) {
        this.connectionMutationRate = connectionMutationRate;
    }

    /**
     * @return the receiverNeuronNumberMutationRate
     */
    public float getReceiverNeuronNumberMutationRate() {
        return receiverNeuronNumberMutationRate;
    }

    /**
     * @param receiverNeuronNumberMutationRate the receiverNeuronNumberMutationRate to set
     */
    public void setReceiverNeuronNumberMutationRate(float receiverNeuronNumberMutationRate) {
        this.receiverNeuronNumberMutationRate = receiverNeuronNumberMutationRate;
    }

    /**
     * @return the responseNeuronNumberMutationRate
     */
    public float getResponseNeuronNumberMutationRate() {
        return responseNeuronNumberMutationRate;
    }

    /**
     * @param responseNeuronNumberMutationRate the responseNeuronNumberMutationRate to set
     */
    public void setResponseNeuronNumberMutationRate(float responseNeuronNumberMutationRate) {
        this.responseNeuronNumberMutationRate = responseNeuronNumberMutationRate;
    }
}
