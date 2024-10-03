package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="ttperformance", description = "performance")
public class PtpNeuralNetworkTrueTablePerformaceCalculator implements PtpNeuralNetworkTrueTableSinglePropertyCalculator{
    private final TrueTableResponseVerifier<ComparableArrayOf<Float>, ComparableArrayOf<Float>> verifier;

    public PtpNeuralNetworkTrueTablePerformaceCalculator() {
        this.verifier = new TrueTableResponseVerifier<>();
    }
    
    public PtpNeuralNetworkTrueTablePerformaceCalculator(TrueTableResponseVerifier<ComparableArrayOf<Float>, ComparableArrayOf<Float>> verifier) {
        this.verifier = verifier;
    }
    
    
    public PtpNeuralNetworkTrueTablePerformaceCalculator(Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        this();
        for(int i=0; i<environmentInputSet.length; i++){
            verifier.addValueCorrespondence(new ComparableArrayOf<>(environmentInputSet[i]), new ComparableArrayOf<>(environmentOutputSet[i]));
        }
    }
    
    @Override
    public void addValueCorrespondence(ComparableArrayOf<Float> input, ComparableArrayOf<Float> output){
        verifier.addValueCorrespondence(input, output);
    }
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        int success=0;
//        Float resolveEficency=0f;
        int differentStimuli = verifier.getEntriesSize();
        for(int s=0; s<differentStimuli; s++){
            Float[] output = agent.update(verifier.getEntry(s).getValues());
            Boolean v = verifier.verify(verifier.getEntry(s), new ComparableArrayOf<>(output));
            if(v){
                success++;
            }
        }
        return BigDecimal.valueOf(success).divide(BigDecimal.valueOf(differentStimuli), 18, RoundingMode.HALF_UP);
    }
}
