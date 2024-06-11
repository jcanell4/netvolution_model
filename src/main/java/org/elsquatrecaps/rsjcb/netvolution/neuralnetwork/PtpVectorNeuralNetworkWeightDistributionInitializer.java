package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ActivationFunction;
import org.elsquatrecaps.util.random.RandomFactory;

/**
 *
 * @author josepcanellas
 */
public class PtpVectorNeuralNetworkWeightDistributionInitializer extends PtpVectorNeuralNetworkRandomInitializer {
    
    public static void initialize(PtpVectorNeuralNetwork net, PtpNeuralNetworkConfiguration config, Float[][] connections, boolean isForDebugging){
        RandomFactory.setDebugState(isForDebugging);
        initialize(net, config, connections);
    }
    
    public static void initialize(PtpVectorNeuralNetwork net, PtpNeuralNetworkConfiguration config, Float[][] connections
            , int[][] inputMatrix, int[][] optputMatrix){
        
        net.loopingTimesToStabilityCheck=config.getLoopingTimesToStabilityCheck();
        net.maxLoopsForResults=config.getMaxLoopsForResults();
        net.neurons = new PtpSingleNeuron[connections.length];
        net.connections = new float[connections.length][connections.length];
        
        createNeurons(config, net);
        
        setReceiverAndResponderNeurons(config, net);
        
        updateConnections(connections, net);
        
   
        net.smHandler = new PtpVectorNeuralNetworkSensoryMotorHandler(inputMatrix.length, config.getInputSize(), config.getOutputSize());
        net.smHandler.inputContribution=config.getInputContribution();
        net.smHandler.outputContribution=config.getOutputContribution();
        
        updateInputOutputContributions(inputMatrix, optputMatrix, net);
        
        updateEffectiveConnectionsSize(net);
        net.updateInputNeuronsLength();
        net.updateOutputNeuronsLength();
    }
    
    public static void initialize(PtpVectorNeuralNetwork net, PtpNeuralNetworkConfiguration config, Float[][] connections){
        net.loopingTimesToStabilityCheck=config.getLoopingTimesToStabilityCheck();
        net.maxLoopsForResults=config.getMaxLoopsForResults();
        net.neurons = new PtpSingleNeuron[connections.length];
        net.connections = new float[connections.length][connections.length];
        
        createNeurons(config, net);
        
        setReceiverAndResponderNeurons(config, net);
        
        updateConnections(connections, net);
        
        net.smHandler = new PtpVectorNeuralNetworkSensoryMotorHandler(config.getInputReceiverNeuronsSize(), config.getInputSize(), config.getOutputSize());
        
        updteRandomInputOutputContributions(config, net);
        updateEffectiveConnectionsSize(net);
        net.updateInputNeuronsLength();
        net.updateOutputNeuronsLength();
    }
    
    protected static void createNeurons(PtpNeuralNetworkConfiguration config, PtpVectorNeuralNetwork net){
        for(int i=0; i<config.getNeuronSize(); i++){
            try {
                net.neurons[i] = new PtpSingleNeuron(i, (ActivationFunction) config.getActivationFunction().clone(), new MonotonicStabilityChecker(config.getEqualityIntervalToStabilityCheck()));
                net.neurons[i].bias = 0f;
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException("Error on clone the activation function from configuration properties");            
            }
        }
    }
    
    private static void updateConnections(Float[][] connections, PtpVectorNeuralNetwork net){
        for(int i=0; i<connections.length; i++){
            for(int j=0; j<connections.length; j++){
                net.setWeight(j, i, connections[i][j]);
                if(connections[i][j]!=0){
                    updateNewConection(j, i, net);
                }
            }
        }
    }
    
    public static void setWeight(int from, int to, float w, PtpVectorNeuralNetwork net){
        float old = net.getWeight(from, to);
        net.setWeight(from, to, w);
        if(old==0 && w!=0){
            updateNewConection(from, to, net);
        }else if(old!=0 && w==0){
            updateLostConection(from, to, net);
        }
    }
    
    private static void updateInputOutputContributions(int[][] in, int [][] out, PtpVectorNeuralNetwork net){
        for(int i=0; i<in.length; i++){
            for(int j=0; j<in[i].length; j++){
                net.smHandler.posInputMatrix.get(i).add(in[i][j]);
            }
        }
        for(int i=0; i<out.length; i++){
            for(int j=0; j<out[i].length; j++){
                net.smHandler.posOutputMatrix.get(i).add(in[i][j]);
            }
        }
    }
    
    
}
