package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ActivationFunction;
import org.elsquatrecaps.util.random.RandomFactory;

/**
 *
 * @author josep
 */
public class PtpVectorNeuralNetworkBaseUpdatingProcessor {
    protected static void createNeurons(PtpNeuralNetworkConfiguration config, PtpVectorNeuralNetwork net){
        for(int i=0; i<config.getNeuronSize(); i++){
            try {
                net.neurons[i] = new PtpSingleNeuron(i, (ActivationFunction) config.getActivationFunction().clone(), new MonotonicStabilityChecker(config.getEqualityIntervalToStabilityCheck()));
                if(RandomFactory.getRandomInstance().nextInt(100)<config.getConnectionProbabilityForwardInterNeuron()){ //????
                    net.neurons[i].bias = RandomFactory.getRandomInstance().nextFloat(config.getMinWeight(), config.getMaxWeigt());
                }
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException("Error on clone the activation function from configuration properties");            
            }
        }
    }
    
    protected static boolean updateConectionWheight(int from, int to, PtpVectorNeuralNetwork net){
        boolean ret;
        if(net.getWeight(from, to)!=0){
            ret = updateNewConection(from, to, net);
        }else{
            ret = updateLostConection(from, to, net);
        }
        return ret;
    }
    
    protected static boolean updateLostConection(int from, int to, PtpVectorNeuralNetwork net){
        boolean pf = net.getNeuron(from).isParticipatingInCalculation();
        boolean pt = net.getNeuron(to).isParticipatingInCalculation();
        if(net.getNeuron(from).isPathToOutput()&& !isNowParticipatingToOutput(from, to, net)){
            net.getNeuron(from).setPathToOutput(false);
            updateCascadePathToOutputForLostParticipation(from, net);
        }
        if(net.getNeuron(to).isPathFromInput() && !isNowParticipatingFromInput(to, from, net)){
            net.getNeuron(to).setPathFromInput(false);
            updateCascadePathFromInputForLostParticipation(to, net);
        }
        return pf!=net.getNeuron(from).isParticipatingInCalculation() || pt !=net.getNeuron(to).isParticipatingInCalculation();
    }
    
    protected static boolean updateNewConection(int from, int to, PtpVectorNeuralNetwork net){
        boolean pf = net.getNeuron(from).isParticipatingInCalculation();
        boolean pt = net.getNeuron(to).isParticipatingInCalculation();
        if(!net.getNeuron(from).isPathToOutput() && net.getNeuron(to).isPathToOutput()){
            net.getNeuron(from).setPathToOutput(true);
            updateCascadePathToOutputForNewParticipation(from, net);
        }
        if(net.getNeuron(from).isPathFromInput() && !net.getNeuron(to).isPathFromInput()){
            net.getNeuron(to).setPathFromInput(true);
            updateCascadePathFromInputForNewParticipation(to, net);
        }
        return pf!=net.getNeuron(from).isParticipatingInCalculation() || pt !=net.getNeuron(to).isParticipatingInCalculation();
    }
    
    protected static void updateEffectiveConnectionsSize(PtpVectorNeuralNetwork net){
        int fc=0;
        int bc=0;
        for(int i=0; i<net.getMaxNeuronsLength(); i++){
            for(int j=0; j<net.getMaxNeuronsLength(); j++){
                if(net.getWeight(i, j)!=0){
                    if(i<j){
                        fc++;
                    }else{
                        bc++;
                    }
                }
            }
        }
    }
    
    private static void updateCascadePathToOutputForLostParticipation(int to, PtpVectorNeuralNetwork net){
        for(int i=0; i<net.getMaxNeuronsLength(); i++){
            if(net.getWeight(i, to)!=0 && net.getNeuron(i).isPathToOutput()&& !isNowParticipatingToOutput(i, to, net)){
                net.getNeuron(i).setPathToOutput(false);
                updateCascadePathToOutputForLostParticipation(i, net);
            }
        }
    }
    
    private static void updateCascadePathFromInputForLostParticipation(int from, PtpVectorNeuralNetwork net){
        for(int i=0; i<net.getMaxNeuronsLength(); i++){
            if(net.getWeight(from, i)!=0 && net.getNeuron(i).isPathFromInput()&& !isNowParticipatingFromInput(i, from, net)){
                net.getNeuron(i).setPathFromInput(false);
                updateCascadePathFromInputForLostParticipation(i, net);
            }
        }
    }
    
    private static void updateCascadePathToOutputForNewParticipation(int to, PtpVectorNeuralNetwork net){
        for(int i=0; i<net.getMaxNeuronsLength(); i++){
            if(net.getWeight(i, to)!=0 && !net.getNeuron(i).isPathToOutput()){
                net.getNeuron(i).setPathToOutput(true);
//                if(!net.getNeuron(i).isPathToOutput()){
                    updateCascadePathToOutputForNewParticipation(i, net);
//                }
            }
        }
    }

    private static void updateCascadePathFromInputForNewParticipation(int from, PtpVectorNeuralNetwork net){
        for(int i=0; i<net.getMaxNeuronsLength(); i++){
            if(net.getWeight(from, i)!=0 && !net.getNeuron(i).isPathFromInput()){
                net.getNeuron(i).setPathFromInput(true);
                updateCascadePathFromInputForNewParticipation(i, net);
            }
        }
    }
        
    private static boolean isNowParticipatingToOutput(int n, int except, PtpVectorNeuralNetwork net){
        boolean ret = net.getNeuron(n).isOutputType();
        for(int i=0; !ret && i<net.getMaxNeuronsLength(); i++){
            if(i!=except){
                ret = net.getWeight(n, i)!=0 && net.getNeuron(i).isPathToOutput();
            }
        }
        return ret;
    }
    
    private static boolean isNowParticipatingFromInput(int n, int except, PtpVectorNeuralNetwork net){
        boolean ret = net.getNeuron(n).isInputType();
        for(int i=0; !ret && i<net.getMaxNeuronsLength(); i++){
            if(i!=except){
                ret = net.getWeight(i, n)!=0 && net.getNeuron(i).isPathFromInput();
            }
        }
        return ret;
    }
    
}
