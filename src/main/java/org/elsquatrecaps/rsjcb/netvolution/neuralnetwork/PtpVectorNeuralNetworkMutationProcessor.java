package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import org.elsquatrecaps.util.random.RandomFactory;
import java.util.ArrayList;
import static org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkRandomInitializer.isAllTrue;

/**
 * beta or threshold for each neuron
 * change of wights in connections for each neuron
 * disconnections
 * change in receiver neuron number
 * change in response neuron number
 * 
 * @author josep
 */
public class PtpVectorNeuralNetworkMutationProcessor extends AbstractPtpNeuralNetworkMutationProcessor {
    
    public PtpVectorNeuralNetworkMutationProcessor() {
    }
    
    public PtpVectorNeuralNetworkMutationProcessor(float thresholdMutationRate, float maxThresholdExchangeFactorValue
            , float weightsMutationRate, float maxWeightExchangevalue, float disconnectionMutationRate, float connectionMutationRate
            , float receiverNeuronNumberMutationRate, float responseNeuronNumberMutationRate, int inputContributionrobability) {
        super(thresholdMutationRate, maxThresholdExchangeFactorValue, weightsMutationRate, maxWeightExchangevalue, disconnectionMutationRate, connectionMutationRate, receiverNeuronNumberMutationRate, responseNeuronNumberMutationRate, inputContributionrobability);
    }
    
    public PtpVectorNeuralNetworkMutationProcessor(float thresholdMutationRate, float maxThresholdExchangeFactorValue
            , float weightsMutationRate, float maxWeightExchangevalue, float disconnectionMutationRate, float connectionMutationRate) {
        super(thresholdMutationRate, maxThresholdExchangeFactorValue, weightsMutationRate, maxWeightExchangevalue, disconnectionMutationRate, connectionMutationRate);
    }
    
    private float getIncrement(float maxvalue, int method){
        float l;
        switch (method) {
            case GAUSSIAN_METHOD_TO_CALCULATE_MUTATION_INCREMENTS:
                l = (float) RandomFactory.getRandomInstance().nextGaussian(0, maxvalue);
                break;
            case RAMDOM_METHOD_TO_CALCULATE_MUTATION_INCREMENTS:
                l = RandomFactory.getRandomInstance().nextFloat(-maxvalue, maxvalue);
                break; 
            default:
                l = maxvalue;
                break;
        }
        return l;
    }
    
    @Override
    public PtpVectorNeuralNetwork muteFrom(PtpNeuralNetwork nnToMute){
        boolean needUpdateEffectiveConnectionsSize=false;
        boolean exit;
        PtpVectorNeuralNetwork nn = null;
        
        try {
            nn = (PtpVectorNeuralNetwork) nnToMute.clone();
        } catch (CloneNotSupportedException ex) {}

        //threshold mutation + bias mutation
        for(int i=0; i<nn.getNeurons().length; i++) {
            //threshold
            if(RandomFactory.getRandomInstance().nextFloat()<getThresholdMutationRate()){
                float l = getIncrement(getMaxThresholdExchangeFactorValue(), getMethodToCalculateMutationIncrements());
                nn.changeNeuronActivationFunctionLinearity(i, l);
            }
            //bias
            if(RandomFactory.getRandomInstance().nextFloat()<getWeightsMutationRate()){
                float l = getIncrement(getMaxWeightExchangevalue(), getMethodToCalculateMutationIncrements());
                nn.getNeuron(i).changeBias(l);
            }            
        }
        
        //kill connections mutation, Weights mutation and new connections mutation
        for(int i=0; i<nn.getNeurons().length; i++) {
            for(int j=0; j<nn.getNeurons().length; j++){
                // kill connections mutation
                if(nn.getWeight(i, j)!=0 && i!=j
                        && RandomFactory.getRandomInstance().nextFloat()<getDisconnectionMutationRate()){
                    nn.setWeight(i, j, 0f);
                    boolean needUpdate = PtpVectorNeuralNetworkBaseUpdatingProcessor.updateLostConection(i, j, nn);
                    needUpdateEffectiveConnectionsSize =  needUpdateEffectiveConnectionsSize || needUpdate;             
                }else
                    //new connections mutation
                    if(nn.getWeight(i, j)==0 && i!=j
                            && RandomFactory.getRandomInstance().nextFloat()<getConnectionMutationRate()){
                        float dw = getIncrement(getMaxWeightExchangevalue(), getMethodToCalculateMutationIncrements());
                        nn.setWeight(i, j, dw);
                        boolean needUpdate = PtpVectorNeuralNetworkBaseUpdatingProcessor.updateNewConection(i, j, nn);
                        needUpdateEffectiveConnectionsSize =   needUpdateEffectiveConnectionsSize || needUpdate;
                }
                    // Weights mutation
                if(nn.getWeight(i, j)!=0 && i!=j
                        && RandomFactory.getRandomInstance().nextFloat()<getWeightsMutationRate()){
                    float dw = nn.getWeight(i, j) + getIncrement(getMaxWeightExchangevalue(), getMethodToCalculateMutationIncrements());
                    if(dw!=0){
                        nn.setWeight(i, j, dw);
                    }
                }/*else*/
            }
        }            
        
        //change in receiver neuron number
        try{
            if(nn.smHandler.inputContribution==InputOutputContributionValues.MIXED_CONTRIBUTION
                    || nn.smHandler.inputContribution==InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION 
                    && nn.inputNeurons.size()>nn.smHandler.getInputSize()){
                if(RandomFactory.getRandomInstance().nextFloat()<getReceiverNeuronNumberMutationRate()){
                    if(RandomFactory.getRandomInstance().nextBoolean()){
                        //afegir
                        int newn = nn.inputNeurons.size();
                        nn.inputNeurons.add(nn.neurons[newn]);
                        nn.smHandler.posInputMatrix.add(new ArrayList<>());
                        boolean isConnected=false;
                        while(!isConnected){
                            exit = false;
                            for(int j=0; !exit && (nn.smHandler.posInputMatrix.get(newn).isEmpty() || j<nn.smHandler.getInputSize()); j++){
                                int jPos = j%nn.smHandler.getInputSize();
                                if(RandomFactory.getRandomInstance().nextInt(100)<this.getInputContributionrobability()){
                                    nn.smHandler.posInputMatrix.get(newn).add(jPos);
                                    isConnected=true;
                                    if(nn.smHandler.inputContribution!=InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION){
                                        exit=true;
                                    }
                                }
                            }
                        }                        
                    }else{
                        //eliminar
                        int toDelete = nn.inputNeurons.size()-1;
                        nn.inputNeurons.remove(toDelete);
                        boolean[] jConnectedControl = new boolean[nn.smHandler.getInputSize()];
                        boolean[][] ijConnectedControl = new boolean[toDelete][nn.smHandler.getInputSize()];
                        for(int i=0; i<toDelete; i++){
                            for(int j=0; j<nn.smHandler.posInputMatrix.get(i).size(); j++){
                                jConnectedControl[nn.smHandler.posInputMatrix.get(i).get(j)]=true;
                                ijConnectedControl[i][nn.smHandler.posInputMatrix.get(i).get(j)]=true;
                            }
                        }
                        nn.smHandler.posInputMatrix.remove(toDelete);
                        for(int i=0; !isAllTrue(jConnectedControl) || i<toDelete; i++){
                            int iPos = i%toDelete;
                            if(nn.smHandler.inputContribution!=InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION){
                                exit = !nn.smHandler.posInputMatrix.get(iPos).isEmpty();
                            }else{
                                exit=false;
                            }
                            for(int j=0; !exit && j<nn.smHandler.getInputSize(); j++){
                                if(!ijConnectedControl[i][j] && RandomFactory.getRandomInstance().nextInt(100)<this.getInputContributionrobability()){
                                    nn.smHandler.posInputMatrix.get(iPos).add(j);
                                    jConnectedControl[j]=true;
                                    ijConnectedControl[i][j]=true;
                                    if(nn.smHandler.inputContribution!=InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION){
                                        exit = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(IllegalArgumentException e){}
        
        // change in response neuron number
        try{
            if(nn.smHandler.outputContribution!=InputOutputContributionValues.SEPARATED_CONTRIBUTION){
                if(RandomFactory.getRandomInstance().nextFloat()<getResponseNeuronNumberMutationRate()){
                    if(true || RandomFactory.getRandomInstance().nextBoolean()){
                        //afegir
                        int newn = nn.neurons.length-nn.outputNeurons.size()-1;
                        nn.outputNeurons.add(nn.neurons[newn]);
                        boolean isConnected=false;
                        while(!isConnected){
                            exit=false;
                            for(int i=0; !exit && i<nn.smHandler.outputValues.length; i++){
                                int iPos = i%nn.smHandler.outputValues.length;
                                if(RandomFactory.getRandomInstance().nextInt(100)<this.getInputContributionrobability()){
                                    nn.smHandler.posOutputMatrix.get(iPos).add(nn.outputNeurons.size()-1);
                                    isConnected=true;
                                    if(nn.smHandler.outputContribution==InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION){
                                        exit=true;
                                    }
                                }
                            }
                        }                        
                    }else{
                        //eliminar
                        nn.outputNeurons.remove(0);
                        boolean[] iConnectedControl = new boolean[nn.smHandler.outputValues.length];
                        for(int i=0; i<nn.smHandler.outputValues.length; i++){
                            int toDelete = -1;
                            for(int j=0; j<nn.smHandler.posOutputMatrix.get(i).size(); j++){
                                if(nn.smHandler.posOutputMatrix.get(i).get(j)==0){
                                    toDelete=j;
                                }else{
                                    nn.smHandler.posOutputMatrix.get(i).set(j, nn.smHandler.posOutputMatrix.get(i).get(j)-1);
                                }
                            }
                            if(toDelete>-1){
                                nn.smHandler.posOutputMatrix.get(i).remove(toDelete);
                            }
                            iConnectedControl[i]=!nn.smHandler.posOutputMatrix.get(i).isEmpty();
                        }
                        for(int i=0; !isAllTrue(iConnectedControl) && i<nn.smHandler.outputValues.length; i++){
                            int iPos = i%nn.smHandler.outputValues.length;
                            if(!iConnectedControl[i]){
                                exit=false;
                                for(int j=0; !exit && j<nn.outputNeurons.size(); j++){
                                    if(RandomFactory.getRandomInstance().nextInt(100)<this.getInputContributionrobability()){
                                        nn.smHandler.posOutputMatrix.get(iPos).add(j);
                                        iConnectedControl[iPos]=true;
                                        if(nn.smHandler.outputContribution==InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION){
                                            exit=true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(IllegalArgumentException e){}
        nn.updateInputNeuronsLength();
        nn.updateOutputNeuronsLength();
        if(needUpdateEffectiveConnectionsSize){
            PtpVectorNeuralNetworkBaseUpdatingProcessor.updateEffectiveConnectionsSize(nn);
        }
        
        return nn;        
    }

}
