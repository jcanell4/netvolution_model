/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

/**
 *
 * @author josep
 */
public abstract class AbstractPtpNeuralNetworkMutationProcessor implements PtpNeuralNetworkMutationProcessor {
    
    protected float thresholdMutationRate = 0.01f;
    protected float maxThresholdExchangeFactorValue = 0.1f;
    protected float weightsMutationRate = 0.01f;
    protected float maxWeightExchangevalue = 0.1f;
    protected float disconnectionMutationRate = 0.01f;
    protected float connectionMutationRate = 0.01f;
    protected float receiverNeuronNumberMutationRate = 0.005f;
    protected float responseNeuronNumberMutationRate = 0.005f;
    private int inputContributionrobability = 50;

    public AbstractPtpNeuralNetworkMutationProcessor() {
    }
    
    public AbstractPtpNeuralNetworkMutationProcessor(float thresholdMutationRate, float maxThresholdExchangeFactorValue
            , float weightsMutationRate, float maxWeightExchangevalue, float disconnectionMutationRate, float connectionMutationRate
            , float receiverNeuronNumberMutationRate, float responseNeuronNumberMutationRate, int inputContributionrobability) {
        this.connectionMutationRate=connectionMutationRate;
        this.disconnectionMutationRate=disconnectionMutationRate;
        this.maxThresholdExchangeFactorValue=maxThresholdExchangeFactorValue;
        this.receiverNeuronNumberMutationRate= receiverNeuronNumberMutationRate;
        this.responseNeuronNumberMutationRate=responseNeuronNumberMutationRate;
        this.thresholdMutationRate=thresholdMutationRate;
        this.weightsMutationRate=weightsMutationRate;
        this.maxWeightExchangevalue=maxWeightExchangevalue;
        this.inputContributionrobability = inputContributionrobability;
    }
    
    public AbstractPtpNeuralNetworkMutationProcessor(float thresholdMutationRate, float maxThresholdExchangeFactorValue
            , float weightsMutationRate, float maxWeightExchangevalue, float disconnectionMutationRate, float connectionMutationRate) {
        this.connectionMutationRate=connectionMutationRate;
        this.disconnectionMutationRate=disconnectionMutationRate;
        this.maxThresholdExchangeFactorValue=maxThresholdExchangeFactorValue;
        this.thresholdMutationRate=thresholdMutationRate;
        this.weightsMutationRate=weightsMutationRate;
        this.maxWeightExchangevalue=maxWeightExchangevalue;
    }
    

    /**
     * @return the thresholdMutationRate
     */
    @Override
    public float getThresholdMutationRate() {
        return thresholdMutationRate;
    }

    /**
     * @param thresholdMutationRate the thresholdMutationRate to set
     */
    @Override
    public void setThresholdMutationRate(float thresholdMutationRate) {
        this.thresholdMutationRate = thresholdMutationRate;
    }

    /**
     * @return the maxThresholdExchangeFactorValue
     */
    @Override
    public float getMaxThresholdExchangeFactorValue() {
        return maxThresholdExchangeFactorValue;
    }

    /**
     * @param maxThresholdExchangeFactorValue the maxThresholdExchangeFactorValue to set
     */
    @Override
    public void setMaxThresholdExchangeFactorValue(float maxThresholdExchangeFactorValue) {
        this.maxThresholdExchangeFactorValue = maxThresholdExchangeFactorValue;
    }

    /**
     * @return the weightsMutationRate
     */
    @Override
    public float getWeightsMutationRate() {
        return weightsMutationRate;
    }

    /**
     * @param weightsMutationRate the weightsMutationRate to set
     */
    @Override
    public void setWeightsMutationRate(float weightsMutationRate) {
        this.weightsMutationRate = weightsMutationRate;
    }

    /**
     * @return the maxWeightExchangevalue
     */
    @Override
    public float getMaxWeightExchangevalue() {
        return maxWeightExchangevalue;
    }

    /**
     * @param maxWeightExchangevalue the maxWeightExchangevalue to set
     */
    @Override
    public void setMaxWeightExchangevalue(float maxWeightExchangevalue) {
        this.maxWeightExchangevalue = maxWeightExchangevalue;
    }

    /**
     * @return the disconnectionMutationRate
     */
    @Override
    public float getDisconnectionMutationRate() {
        return disconnectionMutationRate;
    }

    /**
     * @param disconnectionMutationRate the disconnectionMutationRate to set
     */
    @Override
    public void setDisconnectionMutationRate(float disconnectionMutationRate) {
        this.disconnectionMutationRate = disconnectionMutationRate;
    }

    /**
     * @return the connectionMutationRate
     */
    @Override
    public float getConnectionMutationRate() {
        return connectionMutationRate;
    }

    /**
     * @param connectionMutationRate the connectionMutationRate to set
     */
    @Override
    public void setConnectionMutationRate(float connectionMutationRate) {
        this.connectionMutationRate = connectionMutationRate;
    }

    /**
     * @return the receiverNeuronNumberMutationRate
     */
    @Override
    public float getReceiverNeuronNumberMutationRate() {
        return receiverNeuronNumberMutationRate;
    }

    /**
     * @param receiverNeuronNumberMutationRate the receiverNeuronNumberMutationRate to set
     */
    @Override
    public void setReceiverNeuronNumberMutationRate(float receiverNeuronNumberMutationRate) {
        this.receiverNeuronNumberMutationRate = receiverNeuronNumberMutationRate;
    }

    /**
     * @return the responseNeuronNumberMutationRate
     */
    @Override
    public float getResponseNeuronNumberMutationRate() {
        return responseNeuronNumberMutationRate;
    }

    /**
     * @param responseNeuronNumberMutationRate the responseNeuronNumberMutationRate to set
     */
    @Override
    public void setResponseNeuronNumberMutationRate(float responseNeuronNumberMutationRate) {
        this.responseNeuronNumberMutationRate = responseNeuronNumberMutationRate;
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
    
}
