package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

/**
 *
 * @author josep
 */
public interface PtpNeuralNetworkMutationProcessor {

    /**
     * @return the connectionMutationRate
     */
    float getConnectionMutationRate();

    /**
     * @return the disconnectionMutationRate
     */
    float getDisconnectionMutationRate();

    /**
     * @return the maxThresholdExchangeFactorValue
     */
    float getMaxThresholdExchangeFactorValue();

    /**
     * @return the maxWeightExchangevalue
     */
    float getMaxWeightExchangevalue();

    /**
     * @return the receiverNeuronNumberMutationRate
     */
    float getReceiverNeuronNumberMutationRate();

    /**
     * @return the responseNeuronNumberMutationRate
     */
    float getResponseNeuronNumberMutationRate();

    /**
     * @return the thresholdMutationRate
     */
    float getThresholdMutationRate();

    /**
     * @return the weightsMutationRate
     */
    float getWeightsMutationRate();

    PtpNeuralNetwork muteFrom(PtpNeuralNetwork nnToMute);

    /**
     * @param connectionMutationRate the connectionMutationRate to set
     */
    void setConnectionMutationRate(float connectionMutationRate);

    /**
     * @param disconnectionMutationRate the disconnectionMutationRate to set
     */
    void setDisconnectionMutationRate(float disconnectionMutationRate);

    /**
     * @param maxThresholdExchangeFactorValue the maxThresholdExchangeFactorValue to set
     */
    void setMaxThresholdExchangeFactorValue(float maxThresholdExchangeFactorValue);

    /**
     * @param maxWeightExchangevalue the maxWeightExchangevalue to set
     */
    void setMaxWeightExchangevalue(float maxWeightExchangevalue);

    /**
     * @param receiverNeuronNumberMutationRate the receiverNeuronNumberMutationRate to set
     */
    void setReceiverNeuronNumberMutationRate(float receiverNeuronNumberMutationRate);

    /**
     * @param responseNeuronNumberMutationRate the responseNeuronNumberMutationRate to set
     */
    void setResponseNeuronNumberMutationRate(float responseNeuronNumberMutationRate);

    /**
     * @param thresholdMutationRate the thresholdMutationRate to set
     */
    void setThresholdMutationRate(float thresholdMutationRate);

    /**
     * @param weightsMutationRate the weightsMutationRate to set
     */
    void setWeightsMutationRate(float weightsMutationRate);
    
    int getInputContributionrobability();

    void setInputContributionrobability(int inputContributionrobability);
}
