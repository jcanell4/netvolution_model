package org.elsquatrecaps.rsjcb.netvolution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.elsquatrecaps.rsjcb.netvolution.events.FinishedEvolutionaryCycleEvent;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.SurviveOptimizationMethodValues;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization.OptimizeMethodItems;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.InputOutputContributionValues;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.MonotonicStabilityChecker;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.NeuronTypesForStabilityCheckingValues;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkConfiguration;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpSingleNeuron;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkMutationProcessor;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkRandomInitializer;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkWeightDistributionInitializer;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.SigmoidActivationFunction;
import org.elsquatrecaps.util.random.RandomFactory;
import org.junit.jupiter.api.Test;




/**
 *
 * @author josep
 */
public class TestNetNeuralModel {
    static final int OR=0;
    static final int AND=1;
    static final int XOR=2;
    static final int MPL=3;
    static final int MAJ=4;
    static float[] betaForActivationFunction = {0.01f, 0.5f, 1.0f, 2.0f, 5.0f};
    static final Float[][][] TRUETABLE_INPUT={
        {
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {
            {0f,0f,0f},
            {0f,0f,1f},
            {0f,1f,0f},
            {0f,1f,1f},
            {1f,0f,0f},
            {1f,0f,1f},
            {1f,1f,0f},
            {1f,1f,1f}
        },
        {
            {0f,0f,0f},
            {0f,0f,1f},
            {0f,1f,0f},
            {0f,1f,1f},
            {1f,0f,0f},
            {1f,0f,1f},
            {1f,1f,0f},
            {1f,1f,1f}
        }            
    };
    static final Float[][][] TRUETABLE_OUTPUT={
        {  /*OR*/
            {0f}, //0,0
            {1f}, //0,1
            {1f}, //1,0
            {1f}  //1,1          
        },
        { /*AND*/
            {0f}, //0,0
            {0f}, //0,1
            {0f}, //1,0
            {1f}  //1,1          
        },
        {  /*XOR*/
            {0f}, //0,0
            {1f}, //0,1
            {1f}, //1,0
            {0f}  //1,1          
        },
        {   /*MPL*/
            {0f}, //{0f,0f,0f},
            {0f}, //{0f,0f,1f},
            {0f}, //{0f,1f,0f},
            {1f}, //{0f,1f,1f},
            {1f}, //{1f,0f,0f},
            {0f}, //{1f,0f,1f},
            {1f}, //{1f,1f,0f},
            {1f}    //{1f,1f,1f}
        },
        {   /*MAJ*/
            {0f}, //{0f,0f,0f},
            {0f}, //{0f,0f,1f},
            {0f}, //{0f,1f,0f},
            {1f}, //{0f,1f,1f},
            {0f}, //{1f,0f,0f},
            {1f}, //{1f,0f,1f},
            {1f}, //{1f,1f,0f},
            {1f}  //{1f,1f,1f}
        }
    };

    
    @Test
    public void calculateNetValuesTest(){
        Float[][] results;
        PtpNeuralNetworkConfiguration configuration;
        configuration = new PtpNeuralNetworkConfiguration();

        configuration.setNeuronSize(10);
        configuration.setInputSize(2);
        configuration.setOutputSize(1);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setOutputResponseNeuronsSize(1);
        configuration.setHasIntermediateNeurons(false);
        //configuration.setInitialNeuronSize(0);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        //configuration.setMaxWeigt(0.1f);
        //configuration.setMinWeight(-0.1f);
        configuration.setActivationFunction(new SigmoidActivationFunction(0.01f));
        configuration.setMaxLoopsForResults(20);
        configuration.setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY);    
        Float[][] connections = new Float[configuration.getNeuronSize()][configuration.getNeuronSize()];
        for(int i=0; i<connections.length; i++){
            for(int j=0; j<connections[i].length; j++){
                connections[i][j]=0f;
            }
        }
        
        PtpVectorNeuralNetwork net = new PtpVectorNeuralNetwork();
        PtpVectorNeuralNetworkWeightDistributionInitializer.initialize(net, configuration, connections);
        
        net.getNeuron(0).changeBias(-0.031969f);
        net.getNeuron(1).changeBias(-0.0832195f);        
        net.getNeuron(9).changeBias(-0.0202935f);
      
        net.setWeight(0, 9, 0.0924843028f);
        net.setWeight(1, 9, -0.0284158364f);
        net.setWeight(9, 0, -0.0985049009f);
        
        results = new Float[TRUETABLE_INPUT[OR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[OR][i]);
        }
//        Assertions.assertEquals(TRUETABLE_OUTPUT[OR], results); 
    }
    
    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        prova2();
//        prova3();
//        prova4();
//    }
    
    public static void prova10(){
        PtpNeuralNetworkConfiguration configuration;
        configuration = new PtpNeuralNetworkConfiguration();

        configuration.setNeuronSize(10);
        configuration.setInputSize(2);
        configuration.setOutputSize(1);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setOutputResponseNeuronsSize(1);
        configuration.setHasIntermediateNeurons(false);
        configuration.setInitialNeuronSize(0);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setMaxWeigt(0.1f);
        configuration.setMinWeight(-0.1f);
        configuration.setActivationFunction(new SigmoidActivationFunction(0.01f));
        configuration.setMaxLoopsForResults(20);
        configuration.setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY);    
        
        PtpVectorNeuralNetwork net = new PtpVectorNeuralNetwork();
        
        PtpVectorNeuralNetworkRandomInitializer.initialize(net, configuration);
        
//        Float[][] environmentInputSet={
//            {0f,0f},
//            {0f,1f},
//            {1f,0f},
//            {1f,1f}
//        };
//        Float[][] environmentOutputSet={
//            {1f},
//            {0f},
//            {0f},
//            {1f}
//        };
//        List<String> per = new ArrayList<>();
//        List<String> ra = new ArrayList<>();
//        List<String> tf = new ArrayList<>();
//        PtpNeuralNetworkTrueTableEvolutionaryEnvironment env = new PtpNeuralNetworkTrueTableEvolutionaryEnvironment(
//                new PtpVectorNeuralNetwork[10], 
//                new PtpNeuralNetworkTrueTableGlobalCalculator(per, ra, environmentInputSet, environmentOutputSet), 
//                new PtpVectorNeuralNetworkMutationProcessor(), 
//                tf);
//        env.getMutationProcessor().setConnectionMutationRate(configuration.getConnectionMutationRate());
//        env.getMutationProcessor().setDisconnectionMutationRate(configuration.getDisconnectionMutationRate());
//        env.getMutationProcessor().setMaxThresholdExchangeFactorValue(configuration.getMaxThresholdExchangeFactorValue());
//        env.getMutationProcessor().setMaxWeightExchangevalue(configuration.getMaxWeightExchangevalue());
//        env.getMutationProcessor().setReceiverNeuronNumberMutationRate(configuration.getReceiverNeuronNumberMutationRate());
//        env.getMutationProcessor().setResponseNeuronNumberMutationRate(configuration.getResponseNeuronNumberMutationRate());
//        env.getMutationProcessor().setThresholdMutationRate(configuration.getThresholdMutationRate());
//        env.getMutationProcessor().setWeightsMutationRate(configuration.getWeightsMutationRate());
//        env.getMutationProcessor().setInputContributionrobability(configuration.getInputContributionrobability());
//        env.init(0, 0, 0);
    }
    
    public static void prova4(){
        int pos=-1;
        PtpVectorNeuralNetwork[] net = new PtpVectorNeuralNetwork[10];

        while(pos<net.length-1){
            net[++pos] = setNetA1(2);
            net[pos].updateInputNeuronsLength();
            net[pos].updateOutputNeuronsLength();
        }
        
        ObjectInputStream ois =null;
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(net);
            oos.flush();
            byte[] ao = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(ao);
            ois = new ObjectInputStream(bais);
            PtpVectorNeuralNetwork[] net2 = (PtpVectorNeuralNetwork[]) ois.readObject();
            for (PtpVectorNeuralNetwork net21 : net2) {
                net21.updateInputNeurons();
                net21.updateOutputNeurons();
            }
            boolean v = Arrays.deepEquals(net, net2);
            System.out.println("Prova4: " + v);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(oos!=null){
                    oos.close();
                }
                if(ois!=null){
                    ois.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static void prova3(){
        PtpNeuralNetworkConfiguration configuration;
        configuration = new PtpNeuralNetworkConfiguration();

        configuration.setNeuronSize(10);
        configuration.setInputSize(2);
        configuration.setOutputSize(1);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setOutputResponseNeuronsSize(1);
        configuration.setHasIntermediateNeurons(false);
        configuration.setInitialNeuronSize(0);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setMaxWeigt(0.1f);
        configuration.setMinWeight(-0.1f);
        configuration.setActivationFunction(new SigmoidActivationFunction(0.01f));
        configuration.setMaxLoopsForResults(20);
        configuration.setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY);    
        
        PtpVectorNeuralNetwork net = new PtpVectorNeuralNetwork();
        PtpVectorNeuralNetworkRandomInitializer.initialize(net, configuration);
        
        net.updateInputNeuronsLength();
        net.updateOutputNeuronsLength();
        
        ObjectInputStream ois =null;
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(net);
            oos.flush();
            byte[] ao = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(ao);
            ois = new ObjectInputStream(bais);
            PtpVectorNeuralNetwork net2 = (PtpVectorNeuralNetwork) ois.readObject();
            net2.updateInputNeurons();
            net2.updateOutputNeurons();
            boolean v = net.equals(net2);
            System.out.println("Prova3: " + v);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(oos!=null){
                    oos.close();
                }
                if(ois!=null){
                    ois.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static void prova2(){
        ObjectInputStream ois =null;
        ObjectOutputStream oos = null;
        try {
            MonotonicStabilityChecker ch = new MonotonicStabilityChecker(0.01f);
            PtpSingleNeuron neuro = new PtpSingleNeuron(1, new SigmoidActivationFunction(0.5f), ch);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(neuro);
            oos.flush();
            byte[] ao = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(ao);
            ois = new ObjectInputStream(bais);
            PtpSingleNeuron neuro2 = (PtpSingleNeuron) ois.readObject();
            boolean v = neuro.equals(neuro2);
            System.out.println("Equals prova2:" + v);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(oos!=null){
                    oos.close();
                }
                if(ois!=null){
                    ois.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    public static void prova1() {
        PtpVectorNeuralNetworkMutationProcessor mutationProcessor;
        Float[][] inputs;
        int pos=-1;
        PtpVectorNeuralNetwork[] net = new PtpVectorNeuralNetwork[10];
        
        inputs = new Float[][] {
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f},
        };
        
        net[++pos] = setNetA1(2);
        processAndPrintResults(net[pos], pos, inputs);
        
        net[++pos] = setNet1(0);
        processAndPrintResults(net[pos], pos);
////        net[++pos] = setNet2(0);
////        processAndPrintResults(net[pos], pos);
////        net[++pos] = setNet3(0);
////        processAndPrintResults(net[pos], pos);
////        try {
////            net[++pos] = setNet4(0);            
////        } catch (RuntimeException e) {
////            System.out.println("net3 error: ".concat(e.getMessage()));
////        }
////        net[++pos] = setNet5(0);
////        processAndPrintResults(net[pos], pos);
//       
////        mutationProcessor = new PtpVectorNeuralNetworkMutationProcessor();
////        net[++pos] = mutationProcessor.muteFrom(net[0]);
////        processAndPrintResults(net[pos], pos);
////        
////        net[++pos] = mutationProcessor.muteFrom(net[3]);
////        processAndPrintResults(net[pos], pos);
//       
        PtpNeuralNetworkConfiguration configuration;
        configuration = new PtpNeuralNetworkConfiguration();

        configuration.setNeuronSize(10);
        configuration.setInputSize(2);
        configuration.setOutputSize(1);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setOutputResponseNeuronsSize(1);
        configuration.setHasIntermediateNeurons(false);
        configuration.setInitialNeuronSize(0);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setMaxWeigt(0.1f);
        configuration.setMinWeight(-0.1f);
        configuration.setActivationFunction(new SigmoidActivationFunction(0.01f));
        configuration.setMaxLoopsForResults(20);
        configuration.setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY);
        
//        
////        net[++pos] = new PtpVectorNeuralNetwork(configuration);
////        processAndPrintResults(net[pos], pos);
//        
//        
////        net[++pos] = evolutionProcessTest(configuration);
//        System.out.println("Resultats del millor de l'últim cicle:");
//        processAndPrintResults(net[pos], pos, inputs);
        
        evolutionProcessTest(configuration);

//
    }
//    
    public static PtpVectorNeuralNetwork evolutionProcessTest(PtpNeuralNetworkConfiguration configuration){
        Float[][] environmentInputSet={
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        };
        Float[][] environmentOutputSet={
            {1f},
            {0f},
            {0f},
            {1f}
        };
        List<String> per = new ArrayList<>();
        List<String> ra = new ArrayList<>();
        List<String> tf = new ArrayList<>();
        RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder builder = new RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder();
        builder.setPopulationSize(100)
                .setNnConfig(configuration)
                .setEnvironmentInputSet(environmentInputSet)
                .setEnvironmentOutputSet(environmentOutputSet)
                .setViAdv(per)
                .setRepAdv(ra)
                .setNnPropertiesToFollow(tf)
                .setOptimizationMethod(OptimizeMethodItems.getItem(SurviveOptimizationMethodValues.AVERAGE.getValue()).getInstance())
                .setSurvivalRateForOptimizationMethod(50);
        RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment env = builder.build();
//        env.getMutationProcessor().setConnectionMutationRate(configuration.getConnectionMutationRate());
//        env.getMutationProcessor().setDisconnectionMutationRate(configuration.getDisconnectionMutationRate());
//        env.getMutationProcessor().setMaxThresholdExchangeFactorValue(configuration.getMaxThresholdExchangeFactorValue());
//        env.getMutationProcessor().setMaxWeightExchangevalue(configuration.getMaxWeightExchangevalue());
//        env.getMutationProcessor().setReceiverNeuronNumberMutationRate(configuration.getReceiverNeuronNumberMutationRate());
//        env.getMutationProcessor().setResponseNeuronNumberMutationRate(configuration.getResponseNeuronNumberMutationRate());
//        env.getMutationProcessor().setThresholdMutationRate(configuration.getThresholdMutationRate());
//        env.getMutationProcessor().setWeightsMutationRate(configuration.getWeightsMutationRate());
//        env.getMutationProcessor().setInputContributionrobability(configuration.getInputContributionrobability());
        
        env.addEventHandler(FinishedEvolutionaryCycleEvent.eventType, ev -> screenWriteDataOnFinishEvolutionCycle((FinishedEvolutionaryCycleEvent) ev));
//        env.addEventHandler(ErrorOnProcessEvolution.eventType, ev -> this.onErrorProcessEvolution((ErrorOnProcessEvolution) ev));
//        env.addEventHandler(InitialEvolutionaryProcessEvent.eventType, ev -> this.onInitProcessEvolution((InitialEvolutionaryProcessEvent) ev));
//        env.addEventHandler(CompletedEvolutionaryProcessEvent.eventType, ev -> this.onFinishProcessEvolution((CompletedEvolutionaryProcessEvent) ev));

        env.init(0.90f, 1.0f, 1000000);
        
        
        
        //env.process();
        env.start();
        
//        return env.getLastEvent().getBestAgent();
        return null;
        
    }
    
    private static void screenWriteDataOnFinishEvolutionCycle(FinishedEvolutionaryCycleEvent ev) {
        System.out.println(String.format("Evolutionary cycle number: %d", ev.getId()));
        System.out.println(String.format("Agents replaced: %d", ev.getReplacedAgents()));
        System.out.println(String.format("Average performance: %f", ev.getAvgPerformance()));
        System.out.println(String.format("Maximum performance: %f", ev.getMaxPerformance()));
        System.out.println(String.format("Minimum performance: %f", ev.getMinPerformance()));
        System.out.println("------------------------------------------");        
    }

    private static void processAndPrintResults(PtpVectorNeuralNetwork net, int idNet){
        processAndPrintResults(net, idNet, false);
    }
    
    private static void processAndPrintResults(PtpVectorNeuralNetwork net, int idNet, boolean onlyResults){
        Float[] input = new Float[net.getInputLength()];
        for(int i=0; i<input.length; i++){
            input[i] = Float.valueOf(RandomFactory.getRandomInstance(1).nextInt(0, 2));
        }
        processAndPrintResults(net, idNet, input, onlyResults);
    }

    private static void processAndPrintResults(PtpVectorNeuralNetwork net, int idNet, Float[] input){
        processAndPrintResults(net, idNet, false);
    }
    
    private static void processAndPrintResults(PtpVectorNeuralNetwork net, int idNet, Float[] input, boolean onlyResults){
        Float[] result;
        result = net.updateSM(input);

        System.out.print("XARXA ");
        System.out.println(idNet);
        System.out.println("-----------------------");
        System.out.print("Entrades:");
        System.out.println(Arrays.stream(input).map(String::valueOf).collect(Collectors.joining(",")));
        System.out.print("Resultats: ");
        System.out.println(Arrays.stream(result).map(String::valueOf).collect(Collectors.joining(",")));
        
        if(!onlyResults){
            System.out.println("Parametres interns de la xarxa: ");
            System.out.println(net.toString());
        }
    }
    
    private static void processAndPrintResults(PtpVectorNeuralNetwork net, int idNet, Float[][] inputs){
        processAndPrintResults(net, idNet, inputs, false);
    }
    
    private static void processAndPrintResults(PtpVectorNeuralNetwork net, int idNet, Float[][] inputs, boolean onlyResults){
        for(Float[] input: inputs){
            processAndPrintResults(net, idNet, input, onlyResults);
            
            System.out.println("---");
            
        }
        System.out.println("====");
    }
    
    private static PtpVectorNeuralNetwork setNetA1(int posBeta){
        PtpVectorNeuralNetwork net;
        PtpNeuralNetworkConfiguration configuration = new PtpNeuralNetworkConfiguration();
        configuration.setActivationFunction(new SigmoidActivationFunction(betaForActivationFunction[posBeta]));
        configuration.setHasIntermediateNeurons(false);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setInputSize(2);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setOutputResponseNeuronsSize(1);
        configuration.setOutputSize(1);
        configuration.setMaxLoopsForResults(40
        );
        Float[][] connections = {
            { 0f,  0f,  0f,  0f,  0f},
            { 0f,  0f,  0f,  0f, -2f},
            { 0f,  0f,  0f,  0f,  0f},
            { 0f,  0f,  0f,  0f,  0f},
            {-3f,  5f,  0f,  0f,  0f}
        };
        
        net = new PtpVectorNeuralNetwork();
        PtpVectorNeuralNetworkWeightDistributionInitializer.initialize(net, configuration, connections);

        return net;
    }
    
//    private static void verifyNetA1(PtpVectorNeuralNetwork net, int posBeta){
//        boolean sumsOk = true;
//        System.out.print("Verification of neuron sums: ");
//        net
//        
//        for(int i=0; i<net.getNeurons().length; i++){
//            System.out.println("");
//        }
//    }
    
    private static PtpVectorNeuralNetwork setNet1(int posBeta){
        PtpVectorNeuralNetwork net;
        PtpNeuralNetworkConfiguration configuration = new PtpNeuralNetworkConfiguration();
        configuration.setActivationFunction(new SigmoidActivationFunction(betaForActivationFunction[posBeta]));
        configuration.setHasIntermediateNeurons(false);
        configuration.setInputContribution(InputOutputContributionValues.MIXED_CONTRIBUTION);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setInputSize(2);
        configuration.setMaxWeigt(10f);
        configuration.setMinWeight(-10);
        configuration.setNeuronSize(10);
        configuration.setOutputContribution(InputOutputContributionValues.MIXED_CONTRIBUTION);
        configuration.setOutputResponseNeuronsSize(2);
        configuration.setOutputSize(1);
        
        net = new PtpVectorNeuralNetwork(configuration);
//        net = new PtpVectorNeuralNetwork(configuration);
        return net;
    }

//    private static PtpVectorNeuralNetwork setNet2(int posBeta){
//        PtpVectorNeuralNetwork net;
//        PtpNeuralNetworkConfiguration configuration = new PtpNeuralNetworkConfiguration();
//        configuration.setActivationFunction(new SigmoidActivationFunction(betaForActivationFunction[posBeta]));
//        configuration.setHasIntermediateNeurons(false);
//        configuration.setInputContribution(InputOutputContributionValues.MIXED_CONTRIBUTION);
//        configuration.setInputReceiverNeuronsSize(4);
//        configuration.setInputSize(3);
//        configuration.setMaxWeigt(10f);
//        configuration.setMinWeight(-10);
//        configuration.setNeuronSize(10);
//        configuration.setOutputContribution(InputOutputContributionValues.MIXED_CONTRIBUTION);
//        configuration.setOutputResponseNeuronsSize(4);
//        configuration.setOutputSize(2);
//        
////        net = new PtpVectorNeuralNetwork(configuration, true);
//        net = new PtpVectorNeuralNetwork(configuration);
//        return net;
//    }
//    
//    private static PtpVectorNeuralNetwork setNet3(int posBeta) throws RuntimeException{
//        PtpVectorNeuralNetwork net;
//        PtpNeuralNetworkConfiguration configuration = new PtpNeuralNetworkConfiguration();
//        configuration.setActivationFunction(new SigmoidActivationFunction(betaForActivationFunction[posBeta]));
//        configuration.setHasIntermediateNeurons(false);
//        configuration.setInputContribution(InputOutputContributionValues.MIXED_CONTRIBUTION);
//        configuration.setInputReceiverNeuronsSize(2);
//        configuration.setInputSize(4);
//        configuration.setMaxWeigt(10f);
//        configuration.setMinWeight(-10);
//        configuration.setNeuronSize(10);
//        configuration.setOutputContribution(InputOutputContributionValues.MIXED_CONTRIBUTION);
//        configuration.setOutputResponseNeuronsSize(4);
//        configuration.setOutputSize(4);
//        
////        net = new PtpVectorNeuralNetwork(configuration, true);
//        net = new PtpVectorNeuralNetwork(configuration);
//        return net;
//    }
//
//    private static PtpVectorNeuralNetwork setNet4(int posBeta) throws RuntimeException{
//        PtpVectorNeuralNetwork net;
//        PtpNeuralNetworkConfiguration configuration = new PtpNeuralNetworkConfiguration();
//        configuration.setActivationFunction(new SigmoidActivationFunction(betaForActivationFunction[posBeta]));
//        configuration.setHasIntermediateNeurons(false);
//        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
//        configuration.setInputReceiverNeuronsSize(2);
//        configuration.setInputSize(4);
//        configuration.setMaxWeigt(10f);
//        configuration.setMinWeight(-10);
//        configuration.setNeuronSize(10);
//        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
//        configuration.setOutputResponseNeuronsSize(4);
//        configuration.setOutputSize(4);
//        
////        net = new PtpVectorNeuralNetwork(configuration, true);
//        net = new PtpVectorNeuralNetwork(configuration);
//        return net;
//    }
//    
//    private static PtpVectorNeuralNetwork setNet5(int posBeta) throws RuntimeException{
//        PtpVectorNeuralNetwork net;
//        PtpNeuralNetworkConfiguration configuration = new PtpNeuralNetworkConfiguration();
//        configuration.setActivationFunction(new SigmoidActivationFunction(betaForActivationFunction[posBeta]));
//        configuration.setHasIntermediateNeurons(false);
//        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
//        configuration.setInputReceiverNeuronsSize(2);
//        configuration.setInputSize(2);
//        configuration.setMaxWeigt(10f);
//        configuration.setMinWeight(-10);
//        configuration.setNeuronSize(10);
//        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
//        configuration.setOutputResponseNeuronsSize(1);
//        configuration.setOutputSize(1);
//        
////        net = new PtpVectorNeuralNetwork(configuration, true);
//        net = new PtpVectorNeuralNetwork(configuration);
//        return net;
//    }
}
