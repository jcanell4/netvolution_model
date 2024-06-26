package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="performance", description = "performance")
public class PtpNeuralNetworkDifTablePerformaceCalculator implements PtpNeuralNetworkDifTableSinglePropertyCalculator{
    private final AbsoluteDiferenceTableResponseVerifier verifier;

    public PtpNeuralNetworkDifTablePerformaceCalculator() {
        this.verifier = new AbsoluteDiferenceTableResponseVerifier();
    }
    
    public PtpNeuralNetworkDifTablePerformaceCalculator(AbsoluteDiferenceTableResponseVerifier verifier) {
        this.verifier = verifier;
    }
    
    
    public PtpNeuralNetworkDifTablePerformaceCalculator(Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        this();
        for(int i=0; i<environmentInputSet.length; i++){
            verifier.addValueCorrespondence(new ComparableArrayOf<>(environmentInputSet[i]), environmentOutputSet[i][0]);
        }
    }
    
    @Override
    public void addValueCorrespondence(ComparableArrayOf<Float> input, ComparableArrayOf<Float> output){
        verifier.addValueCorrespondence(input, output.getValues()[0]);
    }
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        BigDecimal success = BigDecimal.ZERO;
        int differentStimuli = verifier.getEntriesSize();
        for(int s=0; s<differentStimuli; s++){
            Float[] output = agent.update(verifier.getEntry(s).getValues());
            BigDecimal dif = BigDecimal.ONE.subtract(verifier.verify(verifier.getEntry(s), output[0]));
            success = success.add(dif);
        }
        return success.divide(BigDecimal.valueOf(differentStimuli), 10, RoundingMode.HALF_UP); 
    }
}
