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
        test.setUp();
        test.mutationTest3();
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
        
        muteAndPrint(net, 1000, true);
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
        
        muteAndPrint(net, 1000, true);
    }
    
    @Test
    public void mutationTest3(){
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
        configuration.setWeightsMutationRate(0.05f);
        configuration.setMaxWeightExchangevalue(0.5f);
        configuration.setDisconnectionMutationRate(0.001f); 
        configuration.setConnectionMutationRate(0.001f);
        
        muteAndPrint(net, 1000, true);
    }
    
    private void muteAndPrint(PtpVectorNeuralNetwork net, int cycles, boolean printChangesOnly){
        ChangesRegister totalChanges = new ChangesRegister();
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
        System.out.println("-------------------------------------------\n");
        totalChanges.inc(getAverageValues(net));
        System.out.println("Valors mitjos de la xarxa:");
        System.out.println(getAverageValuesReport(totalChanges, "- "));
        System.out.println("-------------------------------------------\n");
        if(!printChangesOnly){
            System.out.println("\nParametres agent inicial:");
            System.out.println(net.toString());
            System.out.println("-------------------------------------------\n");
        }
        netrOld = net;
        for(int i=0; i<cycles; i++){
            netr = mutationProcessor.muteFrom(netrOld);
            System.out.print("Cicle de mutació número: ");
            System.out.println(++nm);
            System.out.println("  Canvis constatats:");
            ChangesRegister cycleChanges = getChanges(netrOld, netr);
            totalChanges.inc(cycleChanges);
            System.out.println(getChangesReport(cycleChanges, "  - "));
            System.out.println("  Valors mitjos de la xarxa:");
            System.out.println(getAverageValuesReport(cycleChanges, "  - "));
            if(!printChangesOnly){
                System.out.println("-------------------------------------------\n");
                System.out.println("\nParametres agent mutat");
                System.out.println(netr.toString());
                System.out.println("-------------------------------------------\n");
            }
            System.out.println("-------------------------------------------\n");
            netrOld = netr;
        }
        System.out.println("-------------------------------------------\n");
        totalChanges.setAcumBetaValues(totalChanges.getAcumBetaValues()/(cycles+1));
        totalChanges.setAcumBiasValues(totalChanges.getAcumBiasValues()/(cycles+1));
        totalChanges.setAcumWeightValues(totalChanges.getAcumWeightValues()/(cycles+1));
        System.out.println("Resum dels valors promig totals en la mutació:");
        System.out.println(getAverageValuesReport(totalChanges, "- "));
        System.out.println("-------------------------------------------\n");
        System.out.println("Resum dels canvis totals en la mutació:");
        System.out.println(getChangesReport(totalChanges, "- "));
        System.out.println("-------------------------------------------\n");
    }
    
    private ChangesRegister getAverageValues(PtpVectorNeuralNetwork netr){
        ChangesRegister changesRegister = new ChangesRegister();
        for(int i=0; i<netr.getMaxNeuronsLength(); i++){
            changesRegister.incAcumBetaValues(((SigmoidActivationFunction)netr.getNeuron(i).getActivationFunction()).getBeta());
            changesRegister.incAcumBiasValues(netr.getNeuron(i).getBias());
        }
        changesRegister.setAcumBetaValues(changesRegister.getAcumBetaValues()/netr.getMaxNeuronsLength());
        changesRegister.setAcumBiasValues(changesRegister.getAcumBiasValues()/netr.getMaxNeuronsLength());
        int noZero=0;
        for(int i=0; i<netr.getConnections().length; i++){
            for(int j=0; j<netr.getConnections()[0].length; j++){
                if(netr.getConnections()[i][j]!=0){
                    noZero++;
                }
                changesRegister.incAcumWeightValues(netr.getConnections()[i][j]);
            }
        }
        changesRegister.setAcumWeightValues(changesRegister.getAcumWeightValues()/noZero);
        return changesRegister;         
    }
    
    private ChangesRegister getChanges(PtpVectorNeuralNetwork netrOld, PtpVectorNeuralNetwork netr){
        ChangesRegister changesRegister = new ChangesRegister();
        for(int i=0; i<netrOld.getMaxNeuronsLength(); i++){
            if(((SigmoidActivationFunction)netrOld.getNeuron(i).getActivationFunction()).getBeta()
                    !=((SigmoidActivationFunction)netr.getNeuron(i).getActivationFunction()).getBeta()){
                changesRegister.incBetaChanges(1);
            }
            if(!Objects.equals(netrOld.getNeuron(i).getBias(), netr.getNeuron(i).getBias())){
                changesRegister.incBiasChanges(1);
            }
            changesRegister.incAcumBetaValues(((SigmoidActivationFunction)netr.getNeuron(i).getActivationFunction()).getBeta());
            changesRegister.incAcumBiasValues(netr.getNeuron(i).getBias());
        }
        changesRegister.setAcumBetaValues(changesRegister.getAcumBetaValues()/netr.getMaxNeuronsLength());
        changesRegister.setAcumBiasValues(changesRegister.getAcumBiasValues()/netr.getMaxNeuronsLength());

        int noZero=0;
        for(int i=0; i<netrOld.getConnections().length; i++){
            for(int j=0; j<netrOld.getConnections()[0].length; j++){
                if(netrOld.getConnections()[i][j]==0 && netr.getConnections()[i][j]!=0){
                    changesRegister.incNewConnections(1);
                }
                if(netrOld.getConnections()[i][j]!=0 && netr.getConnections()[i][j]==0){
                    changesRegister.incDeletedConnections(1);
                }
                if(netrOld.getConnections()[i][j]!=netr.getConnections()[i][j]){
                    changesRegister.incWeightChanges(1);
                }
                if(netr.getConnections()[i][j]!=0){
                    noZero++;
                }
                changesRegister.incAcumWeightValues(netr.getConnections()[i][j]);
            }
        }
        changesRegister.setAcumWeightValues(changesRegister.getAcumWeightValues()/noZero);
        return changesRegister;        
    }
    
    private String getAverageValuesReport(ChangesRegister changesRegister, String prefix){
        StringBuilder strb = new StringBuilder();
        strb.append(prefix);
        strb.append("Valor promig en la funció d'activació (BETA): ");
        strb.append(changesRegister.getAcumBetaValues());
        strb.append("\n");
        strb.append(prefix);
        strb.append("Valor promig en el paràmetre bias: ");
        strb.append(changesRegister.getAcumBiasValues());
        strb.append("\n");
        strb.append(prefix);
        strb.append("Valor promig en el pes de connexions existents: ");
        strb.append(changesRegister.getAcumWeightValues());
        strb.append("\n");
        
        return strb.toString();
    }
    
    private String getChangesReport(ChangesRegister changesRegister, String prefix){
        StringBuilder strb = new StringBuilder();
        strb.append(prefix);
        strb.append("canvis en la funció d'activació (BETA): ");
        strb.append(changesRegister.countBetaChanges());
        strb.append("\n");
        strb.append(prefix);
        strb.append("canvis en el paràmetre bias: ");
        strb.append(changesRegister.countBiasChanges());
        strb.append("\n");
        strb.append(prefix);
        strb.append("canvis en el pes de connexions existents: ");
        strb.append(changesRegister.countWeightChanges());
        strb.append("\n");
        strb.append(prefix);
        strb.append("noves connexions establertes: ");
        strb.append(changesRegister.countNewConnections());
        strb.append("\n");
        strb.append(prefix);
        strb.append("connexions eliminades: ");
        strb.append(changesRegister.countDeletedConnections());
        strb.append("\n");

        return strb.toString();
    }
    
    private static class ChangesRegister{
        protected int betaChanges=0;
        protected int biasChanges=0;
        protected int weightChanges=0;
        protected int newConnections=0;
        protected int deletedConnections=0;
        protected double acumBetaValues=0;
        protected double acumBiasValues=0;
        protected double acumWeightValues=0;

        public ChangesRegister() {
        }
        
        public ChangesRegister( int betaChanges, int biasChanges, int weightChanges, int newConnections, int deletedConnections) {
            this.betaChanges = betaChanges;
            this.biasChanges = biasChanges;
            this.weightChanges = weightChanges;
            this.newConnections = newConnections;
            this.deletedConnections = deletedConnections;
        }
        
        

        /**
         * @return the acumBetaValues
         */
        public int countBetaChanges() {
            return (int) betaChanges;
        }

        /**
         * @param betaChanges the acumBetaValues to set
         */
        public void setBetaChanges(int betaChanges) {
            this.betaChanges = betaChanges;
        }

        /**
         * @param betaChanges the acumBetaValues to increment
         */
        public void incBetaChanges(int betaChanges) {
            this.betaChanges += betaChanges;
        }

        /**
         * @return the acumBiasValues
         */
        public int countBiasChanges() {
            return (int) biasChanges;
        }

        /**
         * @param biasChanges the acumBiasValues to set
         */
        public void setBiasChanges(int biasChanges) {
            this.biasChanges = biasChanges;
        }

        /**
         * @param biasChanges the acumBiasValues to increment
         */
        public void incBiasChanges(int biasChanges) {
            this.biasChanges += biasChanges;
        }

        /**
         * @return the acumWeightValues
         */
        public int countWeightChanges() {
            return (int) weightChanges;
        }

        /**
         * @param weightChanges the acumWeightValues to set
         */
        public void setWeightChanges(int weightChanges) {
            this.weightChanges = weightChanges;
        }

        /**
         * @param weightChanges the acumWeightValues to increment
         */
        public void incWeightChanges(int weightChanges) {
            this.weightChanges += weightChanges;
        }

        /**
         * @return the newConnections
         */
        public int countNewConnections() {
            return newConnections;
        }

        /**
         * @param newConnections the newConnections to set
         */
        public void setNewConnections(int newConnections) {
            this.newConnections = newConnections;
        }

        /**
         * @param newConnections the newConnections to increment
         */
        public void incNewConnections(int newConnections) {
            this.newConnections += newConnections;
        }

        /**
         * @return the deletedConnections
         */
        public int countDeletedConnections() {
            return deletedConnections;
        }

        /**
         * @param deletedConnections the deletedConnections to set
         */
        public void setDeletedConnections(int deletedConnections) {
            this.deletedConnections = deletedConnections;
        }
        
        /**
         * @return the newConnections
         */
        public int getNewConnections() {
            return newConnections;
        }

        /**
         * @return the deletedConnections
         */
        public int getDeletedConnections() {
            return deletedConnections;
        }

        /**
         * @param deletedConnections the deletedConnections to increment
         */
        public void incDeletedConnections(int deletedConnections) {
            this.deletedConnections += deletedConnections;
        }
        
        /**
         * @return the acumBetaValues
         */
        public double getAcumBetaValues() {
            return acumBetaValues;
        }

        /**
         * @param acumBetaValues the acumBetaValues to set
         */
        public void setAcumBetaValues(double acumBetaValues) {
            this.acumBetaValues = acumBetaValues;
        }

        /**
         * @return the acumBiasValues
         */
        public double getAcumBiasValues() {
            return acumBiasValues;
        }

        /**
         * @param acumBiasValues the acumBiasValues to set
         */
        public void setAcumBiasValues(double acumBiasValues) {
            this.acumBiasValues = acumBiasValues;
        }

        /**
         * @return the acumWeightValues
         */
        public double getAcumWeightValues() {
            return acumWeightValues;
        }

        /**
         * @param acumWeightValues the acumWeightValues to set
         */
        public void setAcumWeightValues(double acumWeightValues) {
            this.acumWeightValues = acumWeightValues;
        }

        /**
         * @param betaChanges the acumBetaValues to increment
         */
        public void incAcumBetaValues(double betaChanges) {
            this.acumBetaValues += betaChanges;
        }

        /**
         * @param biasChanges the acumBiasValues to increment
         */
        public void incAcumBiasValues(double biasChanges) {
            this.acumBiasValues += biasChanges;
        }

        /**
         * @param weightChanges the acumWeightValues to increment
         */
        public void incAcumWeightValues(double weightChanges) {
            this.acumWeightValues += weightChanges;
        }
        

        public void set(ChangesRegister changesRegister){
            this.betaChanges = changesRegister.betaChanges;
            this.biasChanges = changesRegister.biasChanges;
            this.weightChanges = changesRegister.weightChanges;
            this.newConnections = changesRegister.newConnections;
            this.deletedConnections = changesRegister.deletedConnections;            
            this.acumBetaValues = changesRegister.acumBetaValues;
            this.acumBiasValues = changesRegister.acumBiasValues;
            this.acumWeightValues = changesRegister.acumWeightValues;
        }

        public void inc(ChangesRegister changesRegister){
            this.betaChanges += changesRegister.betaChanges;
            this.biasChanges += changesRegister.biasChanges;
            this.weightChanges += changesRegister.weightChanges;
            this.newConnections += changesRegister.newConnections;
            this.deletedConnections += changesRegister.deletedConnections;            
            this.acumBetaValues += changesRegister.acumBetaValues;
            this.acumBiasValues += changesRegister.acumBiasValues;
            this.acumWeightValues += changesRegister.acumWeightValues;
        }
    }        
}
