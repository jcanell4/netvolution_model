package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import org.elsquatrecaps.util.random.RandomFactory;

/**
 *
 * @author josep
 */
public class PtpVectorNeuralNetworkRandomInitializer extends PtpVectorNeuralNetworkBaseUpdatingProcessor{
    
    public static void initialize(PtpVectorNeuralNetwork net, PtpNeuralNetworkConfiguration config, boolean isForDebugging){
        RandomFactory.setDebugState(isForDebugging);
        initialize(net, config);
    }
    
    public static void initialize(PtpVectorNeuralNetwork net, PtpNeuralNetworkConfiguration config){
        net.loopingTimesToStabilityCheck=config.getLoopingTimesToStabilityCheck();
        net.maxLoopsForResults=config.getMaxLoopsForResults();
        net.neurons = new PtpSingleNeuron[config.getNeuronSize()];
        net.connections = new float[config.getNeuronSize()][config.getNeuronSize()];

        createNeurons(config, net);

        setReceiverAndResponderNeurons(config, net);

        updateRandomConnections(config, net);
        
        net.smHandler = new PtpVectorNeuralNetworkSensoryMotorHandler(config.getInputReceiverNeuronsSize(), config.getInputSize(), config.getOutputSize());
        
        updteRandomInputOutputContributions(config, net);
        updateRandomConnectionsBetweenReceiversAndResponders(config, net);
        updateEffectiveConnectionsSize(net);
        net.updateInputNeuronsLength();
        net.updateOutputNeuronsLength();
    }
    
    protected static void setReceiverAndResponderNeurons(PtpNeuralNetworkConfiguration config, PtpVectorNeuralNetwork net){
        int oPos=net.getNeurons().length-config.getOutputResponseNeuronsSize();
        for(int i=0; i<config.getInputReceiverNeuronsSize(); i++){
            PtpNeuron n = net.getNeurons()[i];
//            n.setPathFromInput(true);
            n.setIhoType('i');
            if(i<net.getInputNeurons().size()){
                net.getInputNeurons().set(i, net.getNeurons()[i]);
            }else{
                net.getInputNeurons().add(net.getNeurons()[i]);
            }
        }
        for(int i=0; i<config.getOutputResponseNeuronsSize(); i++){
            PtpNeuron n = net.getNeurons()[oPos+i];
            n.setIhoType('o');
            if(i<net.getOutputNeurons().size()){
                net.getOutputNeurons().set(i, n);
            }else{
                net.getOutputNeurons().add(n);
            }
            if(config.getNeuronsForStabilityChecking()==NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY){
                net.neuronsToCheckForStability.add(oPos+i);
            }
        }             
    }
    
    protected static void updateRandomConnectionsBetweenReceiversAndResponders(PtpNeuralNetworkConfiguration config, PtpVectorNeuralNetwork net){
        int iPos;
        int jPos;
        boolean[] iConnectedControl;
        boolean[] jConnectedControl;
        boolean[][] ijConnectedControl;
        int oPos;
        
        oPos = net.getNeurons().length-config.getOutputResponseNeuronsSize();
        iConnectedControl = new boolean[config.getInputReceiverNeuronsSize()];
        jConnectedControl = new boolean[config.getOutputResponseNeuronsSize()];
        ijConnectedControl = new boolean[config.getInputReceiverNeuronsSize()][config.getOutputResponseNeuronsSize()];
        for(int i=0; !isAllTrue(jConnectedControl) || !isAllTrue(iConnectedControl) 
                || i<config.getInputReceiverNeuronsSize(); i++){
            iPos = i%config.getInputReceiverNeuronsSize();
            for(int j=0; !isAllTrue(jConnectedControl) || j<config.getOutputResponseNeuronsSize(); j++){
                jPos = j%config.getOutputResponseNeuronsSize();
                if(!ijConnectedControl[iPos][jPos] && RandomFactory.getRandomInstance().nextInt(100)<config.getConnectionProbabilityForwardInterNeuron()){
                    net.setWeight(iPos, oPos+jPos, RandomFactory.getRandomInstance().nextFloat(config.getMinWeight(), config.getMaxWeigt()));
                    updateNewConection(iPos, oPos+jPos, net);
                    iConnectedControl[iPos]=true;
                    jConnectedControl[jPos]=true;
                    ijConnectedControl[iPos][jPos]=true;
                }
            }
        }
        for(int i=0; i<config.getOutputResponseNeuronsSize(); i++){
            for(int j=0; j<config.getInputReceiverNeuronsSize(); j++){
                if(RandomFactory.getRandomInstance().nextInt(100)<config.getConnectionProbabilityBackwardInterNeuron()){
                    net.setWeight(oPos+i, j, RandomFactory.getRandomInstance().nextFloat(config.getMinWeight(), config.getMaxWeigt()));
                    updateNewConection(oPos+i, j, net);
                }
            }
        }        
    }
    
    protected static void updteRandomInputOutputContributions(PtpNeuralNetworkConfiguration config, PtpVectorNeuralNetwork net){
        int iPos;
        int jPos;
        boolean[] iConnectedControl;
        boolean[] jConnectedControl;
        boolean[][] ijConnectedControl;

        net.smHandler.inputContribution=config.getInputContribution();
        net.smHandler.outputContribution=config.getOutputContribution();
        if(config.getInputContribution()==InputOutputContributionValues.MIXED_CONTRIBUTION){
            /*
            Everyone receiver neurons receive one or more inputs and everyone inputs transmit its value to one or more receicer neurons
            */
            jConnectedControl = new boolean[config.getInputSize()];
            iConnectedControl = new boolean[config.getInputReceiverNeuronsSize()];
            ijConnectedControl = new boolean[config.getInputReceiverNeuronsSize()][config.getInputSize()];
            for(int i=0; !isAllTrue(iConnectedControl) || !isAllTrue(jConnectedControl) || i<config.getInputReceiverNeuronsSize(); i++){
                iPos = i%config.getInputReceiverNeuronsSize();
                for(int j=0; net.smHandler.posInputMatrix.get(iPos).isEmpty() || j<config.getInputSize(); j++){
                    jPos = j%config.getInputSize();
                    if(!ijConnectedControl[iPos][jPos] && RandomFactory.getRandomInstance().nextInt(100)<config.getInputContributionrobability()){
                        net.smHandler.posInputMatrix.get(iPos).add(jPos);
                        iConnectedControl[iPos]=true;
                        jConnectedControl[jPos]=true;
                        ijConnectedControl[iPos][jPos]=true;
                    }
                }
            }
        }else if(config.getInputContribution()==InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION
                && config.getInputReceiverNeuronsSize()>=config.getInputSize()){
            /*
            There aren't inputs without transmit its values and every receiver neuron receives only one input value
            */
            iConnectedControl = new boolean[config.getInputReceiverNeuronsSize()];
            ijConnectedControl = new boolean[config.getInputReceiverNeuronsSize()][config.getInputSize()];
            for(int i=0; i<config.getInputSize(); i++){
                net.smHandler.posInputMatrix.get(i).add(i);
                iConnectedControl[i]=true;
                ijConnectedControl[i][i]=true;
            }
            for(int i=0; !isAllTrue(iConnectedControl) || i<config.getInputReceiverNeuronsSize(); i++){
                iPos = i%config.getInputReceiverNeuronsSize();
                if(!iConnectedControl[iPos]){
                    for(int j=0; j<config.getInputSize(); j++){
                        jPos = j%config.getInputSize();
                        if(!ijConnectedControl[iPos][jPos] && RandomFactory.getRandomInstance().nextInt(100)<config.getInputContributionrobability()){
                            net.smHandler.posInputMatrix.get(iPos).add(jPos);
                            iConnectedControl[iPos]=true;
                            ijConnectedControl[iPos][jPos]=true;
                        }
                    }
                }
            }
        }else if(config.getInputSize()==config.getInputReceiverNeuronsSize()){
            /*
            Biunivocal correspondence
            */
            for(int i=0; i<config.getInputSize(); i++){
                net.smHandler.posInputMatrix.get(i).add(i);
            }
        }else{
            throw new RuntimeException("You need the same number of inputs than of receiver neurons for a 'separated input contribution'");
        }
       
        if(config.getOutputContribution()==InputOutputContributionValues.MIXED_CONTRIBUTION){
            /*
            Everyone outputs receive one or more response neurons and everyone response neurons transmit its response to one or more outputs
            */
            iConnectedControl = new boolean[config.getOutputSize()];
            jConnectedControl = new boolean[config.getOutputResponseNeuronsSize()];
            ijConnectedControl = new boolean[config.getOutputSize()][config.getOutputResponseNeuronsSize()];
            for(int i=0; !isAllTrue(iConnectedControl) || !isAllTrue(jConnectedControl) || i<config.getOutputSize(); i++){
                iPos = i%config.getOutputSize();
                for(int j=0; net.smHandler.posOutputMatrix.get(iPos).isEmpty() || j<config.getOutputResponseNeuronsSize(); j++){
                    jPos = j%config.getOutputResponseNeuronsSize();
                    if(!ijConnectedControl[iPos][jPos] && RandomFactory.getRandomInstance().nextInt(100)<config.getOutputContributionProbability()){
                        net.smHandler.posOutputMatrix.get(iPos).add(jPos);
                        iConnectedControl[iPos]=true;
                        jConnectedControl[jPos]=true;
                        ijConnectedControl[iPos][jPos]=true;
                    }
                }
            }            
        }else if(config.getOutputContribution()==InputOutputContributionValues.ONE_TO_MANY_CONTRIBUTION
                && config.getOutputSize()>=config.getOutputResponseNeuronsSize()){
            /*
            Every output receives only one response neuron, and there aren't response neurons without transmit its response
            */

            iConnectedControl = new boolean[config.getOutputSize()];
            ijConnectedControl = new boolean[config.getOutputSize()][config.getOutputResponseNeuronsSize()];
            for(int i=0; i<config.getOutputSize(); i++){
                net.smHandler.posOutputMatrix.get(i).add(i);
                iConnectedControl[i]=true;
                ijConnectedControl[i][i]=true;
            }
            for(int i=0; !isAllTrue(iConnectedControl) || i<config.getOutputSize(); i++){
                iPos = i%config.getOutputSize();
                if(!iConnectedControl[iPos]){
                    for(int j=0; j<config.getOutputResponseNeuronsSize(); j++){
                        jPos = j%config.getOutputResponseNeuronsSize();
                        if(!ijConnectedControl[iPos][jPos] && RandomFactory.getRandomInstance().nextInt(100)<config.getInputContributionrobability()){
                            net.smHandler.posOutputMatrix.get(iPos).add(jPos);
                            iConnectedControl[iPos]=true;
                            ijConnectedControl[iPos][jPos]=true;
                        }
                    }
                }
            }

        }else if(config.getOutputSize()==config.getOutputResponseNeuronsSize()){
            /*
            Biunivocal correspondence
            */
            for(int i=0; i<config.getOutputSize(); i++){
                net.smHandler.posOutputMatrix.get(i).add(i);
            }
        }else{
            throw new RuntimeException("You need the same number of outputs than of response neurons for a 'separated output contribution'");            
        }  
    }
    
    protected static void updateRandomConnections(PtpNeuralNetworkConfiguration config, PtpVectorNeuralNetwork net){
        for(int i=0; i<config.getNeuronSize(); i++){
            if(config.getHasIntermediateNeurons()){
                for(int j=0; j<config.getNeuronSize(); j++){
                    if(i<j && RandomFactory.getRandomInstance().nextInt(100)<config.getConnectionProbabilityForwardInterNeuron()){
                        Float w = RandomFactory.getRandomInstance().nextFloat(config.getMinWeight(), config.getMaxWeigt());
                        net.setWeight(i, j, w);
                        updateNewConection(i, j, net);
                        
                    }else if(i>j && RandomFactory.getRandomInstance().nextInt(100)<config.getConnectionProbabilityBackwardInterNeuron()){
                        Float w = RandomFactory.getRandomInstance().nextFloat(config.getMinWeight(), config.getMaxWeigt());
                        net.setWeight(i, j, w);                        
                        updateNewConection(i, j, net);
                    }
                }
            }
        }
    }
    
    protected static boolean isAllTrue(boolean [] col){
        boolean ret = true;
        for(int i=0; ret && i<col.length; i++){
            ret = ret && col[i];
        }
        return ret;
    }
    
}
