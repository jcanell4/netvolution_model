package org.elsquatrecaps.rsjcb.netvolution;

import java.math.BigDecimal;
import java.util.Objects;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkNeuralCalculationCostCalculator;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkNeuronConnectionDensityCalculator;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.InputOutputContributionValues;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.NeuronTypesForStabilityCheckingValues;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkConfiguration;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkMutationProcessor;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpVectorNeuralNetworkWeightDistributionInitializer;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.SigmoidActivationFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author josep
 */
public class EvolutionaryJUnitTest {
    private static PtpNeuralNetworkConfiguration configuration;
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
    
    public static void main(String[] args) {
        EvolutionaryJUnitTest test = new EvolutionaryJUnitTest();
        EvolutionaryJUnitTest.setUpClass();
        
        test.setUp();
        test.calculate3NRecNetValuesTest();
        test.tearDown();
        test.setUp();
        test.calculate3RecursiveNetValuesTest();
        test.tearDown();
        test.setUp();
        test.calculate5NRecNetValuesTest();
        test.tearDown();
        test.setUp();
        test.mutationTest1();
        test.tearDown();
        test.setUp();
        test.mutationTest2();
        test.tearDown();
        
        EvolutionaryJUnitTest.tearDownClass();
    }    
    
    public EvolutionaryJUnitTest() {
    }

       
    @BeforeAll
    public static void setUpClass() {
        configuration = new PtpNeuralNetworkConfiguration();
                
        configuration.setNeuronSize(10);
        configuration.setActivationFunction(new SigmoidActivationFunction((float) 0.01));
        configuration.setHasIntermediateNeurons(false);
        configuration.setInputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setInputReceiverNeuronsSize(2);
        configuration.setOutputResponseNeuronsSize(1);
        configuration.setInputSize(2);
        configuration.setOutputSize(1);
        configuration.setMaxWeigt(0.1f);
        configuration.setMinWeight(-0.1f);
        configuration.setOutputContribution(InputOutputContributionValues.SEPARATED_CONTRIBUTION);
        configuration.setMaxLoopsForResults(20);
        configuration.setNeuronsForStabilityChecking(NeuronTypesForStabilityCheckingValues.OUTPUT_ONLY);
        configuration.setThresholdMutationRate(0.01f);
        configuration.setMaxThresholdExchangeFactorValue(0.1f);
        configuration.setWeightsMutationRate(0.01f);
        configuration.setMaxWeightExchangevalue(0.01f);
        configuration.setConnectionMutationRate(0.01f);
        configuration.setDisconnectionMutationRate(0.01f);
        //configuration.setInitialNeuronSize(0);
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
    
    private PtpVectorNeuralNetwork createBaseNet(){
        Float[][] connections;
        
        connections = new Float[configuration.getNeuronSize()][configuration.getNeuronSize()];
        for(int i=0; i<connections.length; i++){
            for(int j=0; j<connections[i].length; j++){
                connections[i][j]=0f;
            }
        }
        
        PtpVectorNeuralNetwork net = new PtpVectorNeuralNetwork();
        PtpVectorNeuralNetworkWeightDistributionInitializer.initialize(net, configuration, connections);
        return net;        
    }
    
    private void validateValues(Float[][] results, float[] expectedResults, double cost, double expectedCost, double dens, double expectedDens){
        System.out.print("Cost: ");
        System.out.println(cost);
        System.out.print("Density:");
        System.out.println(dens);
        for(Float[] r: results){
            System.out.print("R: ");
            System.out.println(r[0]);
        }
        
        for(int i=0; i<results.length; i++){
            Assertions.assertTrue(Math.abs(expectedResults[i]-results[i][0])<0.00001);
        }
        Assertions.assertEquals(expectedCost, cost);
        Assertions.assertTrue(Math.abs(expectedDens-dens)<0.0000001);        
    }
    
    
    @Test
    public void calculate3NRecNetValuesTest(){
        float[] expRes;
        BigDecimal costV;
        BigDecimal densV;
        Float[][] results;
        PtpVectorNeuralNetwork net = createBaseNet();
        
        net.getNeuron(0).changeBias(0.1f);
        net.getNeuron(0).getActivationFunction().changeLinearity(0.5f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(0, 9, -0.3f, net);
        
        net.getNeuron(1).changeBias(0.2f);        
        net.getNeuron(1).getActivationFunction().changeLinearity(2.0f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(1, 9, 1.0f, net);
        
        net.getNeuron(9).changeBias(-0.50f);
        
        results = new Float[TRUETABLE_INPUT[OR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.update(TRUETABLE_INPUT[OR][i]);
        }
        
        PtpNeuralNetworkNeuralCalculationCostCalculator cost = new PtpNeuralNetworkNeuralCalculationCostCalculator();
        PtpNeuralNetworkNeuronConnectionDensityCalculator dens = new PtpNeuralNetworkNeuronConnectionDensityCalculator();
        
        
        costV = cost.calculate(net);
        densV = dens.calculate(net);
        
        expRes = new float[results.length];
        expRes[0] = 0.49986f;
        expRes[1] = 0.50066f;
        expRes[2] = 0.49977f;
        expRes[3] = 0.50057f;
        
        validateValues(results, expRes, costV.doubleValue(), 0.0, densV.doubleValue(), 0.0/90.0);        
    }
    
    @Test
    public void calculate3RecursiveNetValuesTest(){
        float[] expRes;
        BigDecimal costV;
        BigDecimal densV;
        Float[][] results;
        PtpVectorNeuralNetwork net = createBaseNet();
        
        net.getNeuron(0).changeBias(0.1f);
        net.getNeuron(0).getActivationFunction().changeLinearity(0.5f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(0, 9, -0.3f, net);
        
        net.getNeuron(1).changeBias(0.2f);        
        net.getNeuron(1).getActivationFunction().changeLinearity(2.0f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(1, 9, 1.0f, net);
        
        net.getNeuron(9).changeBias(-0.50f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(9, 0, -0.001f, net);        
        
        results = new Float[TRUETABLE_INPUT[OR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.update(TRUETABLE_INPUT[OR][i]);
        }
        
        PtpNeuralNetworkNeuralCalculationCostCalculator cost = new PtpNeuralNetworkNeuralCalculationCostCalculator();
        PtpNeuralNetworkNeuronConnectionDensityCalculator dens = new PtpNeuralNetworkNeuronConnectionDensityCalculator();
        
        
        costV = cost.calculate(net);
        densV = dens.calculate(net);
        
        expRes = new float[results.length];
        expRes[0] = 0.49257f;
        expRes[1] = 0.49336f;
        expRes[2] = 0.49071f;
        expRes[3] = 0.49150f;
        
        validateValues(results, expRes, costV.doubleValue(), 1.0, densV.doubleValue(), 1.0/90.0);
    }

    @Test
    public void calculate5NRecNetValuesTest(){
        float[] expRes;
        BigDecimal costV;
        BigDecimal densV;
        Float[][] results;
        PtpVectorNeuralNetwork net = createBaseNet();
        
        net.getNeuron(0).changeBias(0.53f);
        net.getNeuron(0).getActivationFunction().changeLinearity(0.5f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(0, 9, 0.33600f, net);
        
        net.getNeuron(1).changeBias(-0.9100f);        
        net.getNeuron(1).getActivationFunction().changeLinearity(2.0f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(1, 9, -0.86800f, net);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(1, 3, -0.30800f, net);
        
        net.getNeuron(3).changeBias(-0.09000f);        
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(3, 7, 0.33600f, net);

        net.getNeuron(7).changeBias(0.03000f);        
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(7, 9, 0.06230f, net);

        net.getNeuron(9).changeBias(0.070f);
        net.getNeuron(9).getActivationFunction().changeLinearity(0.5f);
        
        results = new Float[TRUETABLE_INPUT[OR].length][1];
        for(int i=0; i<results.length; i++){
            results[i] = net.update(TRUETABLE_INPUT[OR][i]);
        }
        
        PtpNeuralNetworkNeuralCalculationCostCalculator cost = new PtpNeuralNetworkNeuralCalculationCostCalculator();
        PtpNeuralNetworkNeuronConnectionDensityCalculator dens = new PtpNeuralNetworkNeuronConnectionDensityCalculator();
        
        
        costV = cost.calculate(net);
        densV = dens.calculate(net);
        
        expRes = new float[results.length];
        expRes[0] = 0.52187f;
        expRes[1] = 0.47689f;
        expRes[2] = 0.52694f;
        expRes[3] = 0.48196f;
        
        validateValues(results, expRes, costV.doubleValue(), 0.0, densV.doubleValue(), 3.0/90.0);        
    }
    
    @Test
    public void mutationTest1(){
        PtpVectorNeuralNetwork netr;
        PtpVectorNeuralNetworkMutationProcessor mutationProcessor;
        PtpVectorNeuralNetwork net = createBaseNet();
        
        net.getNeuron(0).changeBias(0.1f);
        net.getNeuron(0).getActivationFunction().changeLinearity(0.5f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(0, 9, -0.3f, net);
        
        net.getNeuron(1).changeBias(0.2f);        
        net.getNeuron(1).getActivationFunction().changeLinearity(2.0f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(1, 9, 1.0f, net);
        
        net.getNeuron(9).changeBias(-0.50f);
        
        muteAndPrint(net);
    }
    
    @Test
    public void mutationTest2(){
        PtpVectorNeuralNetwork netr;
        PtpVectorNeuralNetworkMutationProcessor mutationProcessor;
        PtpVectorNeuralNetwork net = createBaseNet();
        
        net.getNeuron(0).changeBias(0.1f);
        net.getNeuron(0).getActivationFunction().changeLinearity(0.5f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(0, 9, -0.3f, net);
        
        net.getNeuron(1).changeBias(0.2f);        
        net.getNeuron(1).getActivationFunction().changeLinearity(2.0f);
        PtpVectorNeuralNetworkWeightDistributionInitializer.setWeight(1, 9, 1.0f, net);
        
        net.getNeuron(9).changeBias(-0.50f);
        configuration.setThresholdMutationRate(0.1f);
        configuration.setMaxThresholdExchangeFactorValue(0.5f);
        configuration.setWeightsMutationRate(0.1f);
        configuration.setMaxWeightExchangevalue(0.5f);
        configuration.setDisconnectionMutationRate(0.0025f); 
        configuration.setConnectionMutationRate(0.0025f);
        
        muteAndPrint(net);
    }
    
    private void muteAndPrint(PtpVectorNeuralNetwork net){
        PtpVectorNeuralNetwork netrOld;
        PtpVectorNeuralNetwork netr;
        PtpVectorNeuralNetworkMutationProcessor mutationProcessor;
        mutationProcessor = new PtpVectorNeuralNetworkMutationProcessor(
                configuration.getThresholdMutationRate(),
                configuration.getMaxThresholdExchangeFactorValue(), 
                configuration.getWeightsMutationRate(), 
                configuration.getMaxWeightExchangevalue(), 
                configuration.getDisconnectionMutationRate(), 
                configuration.getConnectionMutationRate());
        
        int nm =0;
        
        System.out.println("REPORT DE MUTACIÓ");
        System.out.println("=================");
        System.out.println();
        System.out.println("Paràmetres: ");
        System.out.print("\t- Ratio de canvi per la BETA de les funcins d'activació de cada neurona: ");
        System.out.println(configuration.getThresholdMutationRate());
        System.out.print("\t- Valor màxim de canvi per la BETA de les funcins d'activació de cada neurona: ");
        System.out.println(configuration.getMaxThresholdExchangeFactorValue());
        System.out.print("\t- Valor mínim de canvi per la BETA de les funcins d'activació de cada neurona: ");
        System.out.println(- configuration.getMaxThresholdExchangeFactorValue());
        System.out.print("\t- Ratio de canvi pels pesos de cada connexió: ");
        System.out.println(configuration.getWeightsMutationRate());
        System.out.print("\t- Valor màxim de canvi pels pesos de cada connexió: ");
        System.out.println(configuration.getMaxWeightExchangevalue());
        System.out.print("\t- Valor mínim de canvi per la BETA de les funcins d'activació de cada neurona: ");
        System.out.println(- configuration.getMaxWeightExchangevalue());
        System.out.print("\t- Ratio d'aparició de noves connexions: ");
        System.out.println(configuration.getConnectionMutationRate());
        System.out.print("\t- Ratio de desaparició de connexions existents: ");
        System.out.println(configuration.getDisconnectionMutationRate());
        System.out.println();
        System.out.println("\nParametres agent inicial:");
        System.out.println(net.toString());
        System.out.println("-------------------------------------------\n");
        netrOld = net;
        for(int i=0; i<30; i++){
            netr = mutationProcessor.muteFrom(netrOld);
            System.out.print("Cicle de mutació número: ");
            System.out.println(++nm);
            System.out.println("Canvis constatats:");
            System.out.println(getChangesReport(netrOld, netr));
            System.out.println("-------------------------------------------\n");
            System.out.println("\nParametres agent mutat");
            System.out.println(netr.toString());
            System.out.println("-------------------------------------------\n");
            netrOld = netr;
        }
    }
    
    private String getChangesReport(PtpVectorNeuralNetwork netrOld, PtpVectorNeuralNetwork netr){
        int c;
        StringBuilder strb = new StringBuilder();
        strb.append("  - canvis en la funció d'activació (BETA): ");
        c=0;
        for(int i=0; i<netrOld.getMaxNeuronsLength(); i++){
            if(((SigmoidActivationFunction)netrOld.getNeuron(i).getActivationFunction()).getBeta()
                    !=((SigmoidActivationFunction)netr.getNeuron(i).getActivationFunction()).getBeta()){
                c++;
            }
        }
        strb.append(c);
        strb.append("\n");
        strb.append("  - canvis en el paràmetre bias: ");
        c=0;
        for(int i=0; i<netrOld.getMaxNeuronsLength(); i++){
            if(!Objects.equals(netrOld.getNeuron(i).getBias(), netr.getNeuron(i).getBias())){
                c++;
            }
        }
        strb.append(c);
        strb.append("\n");
        strb.append("  - canvis en el pes de connexions existents: ");
        c=0;
        int n=0;
        int e=0;
        for(int i=0; i<netrOld.getConnections().length; i++){
            for(int j=0; j<netrOld.getConnections()[0].length; j++){
                if(netrOld.getConnections()[i][j]==0 && netr.getConnections()[i][j]!=0){
                    n++;
                }
                if(netrOld.getConnections()[i][j]!=0 && netr.getConnections()[i][j]==0){
                    e++;
                }
                if(netrOld.getConnections()[i][j]!=netr.getConnections()[i][j]){
                    c++;
                }
            }
        }
        strb.append(c);
        strb.append("\n");
        strb.append("  - noves connexions establertes: ");
        strb.append(n);
        strb.append("\n");
        strb.append("  - connexions eliminades: ");
        strb.append(e);
        strb.append("\n");

        return strb.toString();
    }
        
}
