package org.elsquatrecaps.rsjcb.netvolution;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.InputOutputContributionValues;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.NeuronTypesForStabilityCheckingValues;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkConfiguration;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkWeightDistributionInitializer;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.SigmoidActivationFunction;
import org.elsquatrecaps.util.random.Random;
import org.elsquatrecaps.util.random.RandomFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author josep
 */
public class LogicGatesTest {
//    private static PtpNeuralNetworkConfiguration configuration;    
    static final int OR=0;
    static final int AND=1;
    static final int NOR=2;
    static final int NAND=3;
    static final int XNOR=4;
    static final int XOR=5;
    static final int MPL=6;
    static final int MAJ=7;
    static final Float[][][] TRUETABLE_INPUT={
        {   /*OR*/
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {   /*AND*/
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {   /*NOR*/
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {   /*NAND*/
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {   /*XNOR*/
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {   /*XOR*/
            {0f,0f},
            {0f,1f},
            {1f,0f},
            {1f,1f}
        },
        {   /*MPL*/
            {0f,0f,0f},
            {0f,0f,1f},
            {0f,1f,0f},
            {0f,1f,1f},
            {1f,0f,0f},
            {1f,0f,1f},
            {1f,1f,0f},
            {1f,1f,1f}
        },
        {   /*MAJ*/
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
        {  /*NOR*/
            {1f}, //0,0
            {0f}, //0,1
            {0f}, //1,0
            {0f}  //1,1          
        },
        {  /*NAND*/
            {1f}, //0,0
            {1f}, //0,1
            {1f}, //1,0
            {0f}  //1,1          
        },
        {  /*XNOR*/
            {1f}, //0,0
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
    
    public static void main(String[] args) {
        LogicGatesTest test = new LogicGatesTest();
        LogicGatesTest.setUpClass();
        
        test.setUp();
        test.calculateAndNetTest();
        test.tearDown();
        test.setUp();
        test.calculateOrNetTest();
        test.tearDown();
        test.setUp();
        test.calculateNorNetTest();
        test.tearDown();
        test.setUp();
        test.calculateNandNetTest();
        test.tearDown();
        test.setUp();
        test.calculateXnorNetTest();
        test.tearDown();
        test.setUp();
        test.calculateXorNetTest();
        test.tearDown();
//        test.setUp();
//        test.mutationTest1();
//        test.tearDown();
//        test.setUp();
//        test.mutationTest2();
//        test.tearDown();
//        test.setUp();
//        test.mutationTest3();
//        test.tearDown();
        
        LogicGatesTest.tearDownClass();
    } 
    
    @Test
    public void calculateAndNetTest(){
        Float[][] results;
        PtpVectorNeuralNetwork net = getNet(2,1);
        
        net.getNeuron(0).changeBias(0f);
        net.getNeuron(1).changeBias(0f);        
        net.getNeuron(2).changeBias(-1.4f);
      
        net.setWeight(0, 2, 1f);
        net.setWeight(1, 2, 1f);
        
        results = new Float[TRUETABLE_INPUT[AND].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[AND][i]);
        }
        printResults("AND", TRUETABLE_INPUT[AND], results);
        Assertions.assertArrayEquals(TRUETABLE_OUTPUT[AND], results);         
    }
    
    @Test
    public void calculateOrNetTest(){
        Float[][] results;
        PtpVectorNeuralNetwork net = getNet(2,1);

        net.getNeuron(0).changeBias(0f);
        net.getNeuron(1).changeBias(0f);        
        net.getNeuron(2).changeBias(-2.46f);
      
        net.setWeight(0, 2, 2f);
        net.setWeight(1, 2, 2f);
        
        results = new Float[TRUETABLE_INPUT[OR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[OR][i]);
        }
        printResults("OR", TRUETABLE_INPUT[OR], results);
        Assertions.assertArrayEquals(TRUETABLE_OUTPUT[OR], results);         
    }
    
    @Test
    public void calculateNorNetTest(){
        Float[][] results;
        PtpVectorNeuralNetwork net = getNet(2,1);

        net.getNeuron(0).changeBias(0f);
        net.getNeuron(1).changeBias(0f);        
        net.getNeuron(2).changeBias(1.2f);
      
        net.setWeight(0, 2, -1f);
        net.setWeight(1, 2, -1f);
        
        results = new Float[TRUETABLE_INPUT[NOR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[NOR][i]);
        }
        printResults("NOR", TRUETABLE_INPUT[NOR], results);
        Assertions.assertArrayEquals(TRUETABLE_OUTPUT[NOR], results);         
    } 
    
    @Test
    public void calculateNandNetTest(){
        Float[][] results;
        PtpVectorNeuralNetwork net = getNet(2,1);

        net.getNeuron(0).changeBias(0f);
        net.getNeuron(1).changeBias(0f);        
        net.getNeuron(2).changeBias(1.45f);
      
        net.setWeight(0, 2, -1f);
        net.setWeight(1, 2, -1f);
        
        results = new Float[TRUETABLE_INPUT[NAND].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[NAND][i]);
        }
        printResults("NAND",TRUETABLE_INPUT[NAND], results);
        Assertions.assertArrayEquals(TRUETABLE_OUTPUT[NAND], results);         
    } 
    
    @Test
    public void calculateXnorNetTest(){
        Float[][] results;
        PtpVectorNeuralNetwork net = getNet(2,1,2);

        net.getNeuron(0).changeBias(0f);
        net.getNeuron(1).changeBias(0f);        
        net.getNeuron(2).changeBias(-1.4f);
        net.getNeuron(3).changeBias(1.2f);
        net.getNeuron(4).changeBias(-1.9005f);
      
        net.setWeight(0, 2, 1f);
        net.setWeight(1, 2, 1f);
        net.setWeight(0, 3, -1f);
        net.setWeight(1, 3, -1f);
        net.setWeight(2, 4, 2f);
        net.setWeight(3, 4, 2f);
        
       results = new Float[TRUETABLE_INPUT[XNOR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[XNOR][i]);
        }
        printResults("XNOR",TRUETABLE_INPUT[XNOR], results);
        Assertions.assertArrayEquals(TRUETABLE_OUTPUT[XNOR], results);         
    } 
    
    @Test
    public void calculateXorNetTest(){
        Random rand =RandomFactory.getRandomInstance();
        Float[][] results;
        PtpVectorNeuralNetwork net = getNet(2,1,2);

        net.getNeuron(0).changeBias(0f);
        net.getNeuron(1).changeBias(0f);        
        net.getNeuron(2).changeBias(-2.2555f);
//        net.getNeuron(2).changeBias(-2.4f);
        net.getNeuron(3).changeBias(1.9f);
//        net.getNeuron(3).changeBias(1.4f);
        net.getNeuron(4).changeBias(-1.0f); //0111
//        net.getNeuron(4).changeBias(-0.95f);  //1111
      
        net.setWeight(0, 2, 1.3f);
        net.setWeight(1, 2, 1.3f);
        net.setWeight(0, 3, -1.2f);
        net.setWeight(1, 3, -1.2f);
        net.setWeight(2, 4, 1f);
        net.setWeight(3, 4, 1f);
//        for(int i=0; i<net.getNeurons().length; i++){
//            for(int j=0; j<net.getNeurons().length; j++){
//                net.setWeight(i, j, rand.nextFloat(1)-0.5f);
//            }
//            if(i>=net.getInputNeuronsLength()){
//                net.getNeuron(i).changeBias(1f);
//            }
//        }
        
        net.train(TRUETABLE_INPUT[XNOR], TRUETABLE_OUTPUT[XNOR], 50000, 0.5f);
    
                // Prova
        for (int i = 0; i <TRUETABLE_INPUT[XOR].length ; i++) {
            Float[] pred = net.update(TRUETABLE_INPUT[XOR][i]);
            System.out.println(Arrays.toString(TRUETABLE_INPUT[XOR][i]) + " → " + pred[0]);
        }
        

        results = new Float[TRUETABLE_INPUT[XOR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.updateSM(TRUETABLE_INPUT[XOR][i]);
        }
        printResults("XOR",TRUETABLE_INPUT[XOR], results);
//        Assertions.assertArrayEquals(TRUETABLE_OUTPUT[XOR], results);         
    } 
    
    private PtpVectorNeuralNetwork getNet(int in, int out){
        return getNet(in, out, 0);
    }
    
    private PtpVectorNeuralNetwork getNet(int in, int out, int inter){
        PtpNeuralNetworkConfiguration configuration;
        configuration = new PtpNeuralNetworkConfiguration();

        configuration.setNeuronSize(in+out+inter);
        configuration.setInputSize(in);
        configuration.setOutputSize(out);
        configuration.setInputReceiverNeuronsSize(in);
        configuration.setOutputResponseNeuronsSize(out);
        configuration.setHasIntermediateNeurons(inter>0);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setActivationFunction(new SigmoidActivationFunction(1.0f));
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
        return net;
    }
    
    private void printResults(String gate, Float[][] in, Float[][] result){
        System.out.println(String.format("Results for %s:", gate));
        for(int j=0; j<in[0].length; j++){
            System.out.print(String.format("   X%1d   ", j));
        }
        System.out.println("   R   ");
        System.out.println("---------------------");
        
        
        for(int i=0; i<in.length; i++){
            for(int j=0; j<in[i].length; j++){
                System.out.print(String.format(" %2.3f ", in[i][j]));
            }
            System.out.println(String.format(" %2.3f ", result[i][0]));
        }
        
    }

    @BeforeAll
    public static void setUpClass() {
//        configuration = new PtpNeuralNetworkConfiguration();
//                
//        configuration.setNeuronSize(10);
//        configuration.setActivationFunction(new SigmoidActivationFunction((float) 0.01));
//        configuration.setHasIntermediateNeurons(false);
//        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
//        configuration.setInputReceiverNeuronsSize(2);
//        configuration.setOutputResponseNeuronsSize(1);
//        configuration.setInputSize(2);
//        configuration.setOutputSize(1);
//        configuration.setMaxWeigt(0.1f);
//        configuration.setMinWeight(-0.1f);
//        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
//        configuration.setMaxLoopsForResults(20);
//        configuration.setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY);
//        configuration.setThresholdMutationRate(0.01f);
//        configuration.setMaxThresholdExchangeFactorValue(0.1f);
//        configuration.setWeightsMutationRate(0.01f);
//        configuration.setMaxWeightExchangevalue(0.01f);
//        configuration.setConnectionMutationRate(0.01f);
//        configuration.setDisconnectionMutationRate(0.01f);
//        //configuration.setInitialNeuronSize(0);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }   
}
