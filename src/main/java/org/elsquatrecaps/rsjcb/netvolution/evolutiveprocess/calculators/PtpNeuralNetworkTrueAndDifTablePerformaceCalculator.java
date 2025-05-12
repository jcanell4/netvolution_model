package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="performance", description = "performance")
public class PtpNeuralNetworkTrueAndDifTablePerformaceCalculator implements PtpNeuralNetworkTableSinglePropertyCalculator{
    private final AbsoluteDiferenceTableResponseVerifier difVerifier;
    private final TrueTableResponseVerifier<ComparableArrayOf<Float>, ComparableArrayOf<Float>> trueVerifier;



    public PtpNeuralNetworkTrueAndDifTablePerformaceCalculator() {
        this.difVerifier = new AbsoluteDiferenceTableResponseVerifier();
        this.trueVerifier = new TrueTableResponseVerifier<>();
    }
    
    public PtpNeuralNetworkTrueAndDifTablePerformaceCalculator(TrueTableResponseVerifier<ComparableArrayOf<Float>, ComparableArrayOf<Float>> trueVerifier, 
                                                                AbsoluteDiferenceTableResponseVerifier difVerifier) {
        this.difVerifier = difVerifier;
        this.trueVerifier = trueVerifier;
    }
    
    
    public PtpNeuralNetworkTrueAndDifTablePerformaceCalculator(Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        this();
        for(int i=0; i<environmentInputSet.length; i++){
            difVerifier.addValueCorrespondence(new ComparableArrayOf<>(environmentInputSet[i]), environmentOutputSet[i][0]);
            trueVerifier.addValueCorrespondence(new ComparableArrayOf<>(environmentInputSet[i]), new ComparableArrayOf<>(environmentOutputSet[i]));
        }
    }
    
    @Override
    public void addValueCorrespondence(ComparableArrayOf<Float> input, ComparableArrayOf<Float> output){
        difVerifier.addValueCorrespondence(input, output.getValues()[0]);
        trueVerifier.addValueCorrespondence(input, output);
    }
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        BigDecimal success = BigDecimal.ZERO;
        int differentStimuli = difVerifier.getEntriesSize();
        for(int s=0; s<differentStimuli; s++){
            Float[] output = agent.updateSM(difVerifier.getEntry(s).getValues());
            BigDecimal dif = BigDecimal.ONE.subtract(difVerifier.verify(difVerifier.getEntry(s), agent.getLastUpdate()[0]));
            success = success.add(dif);
            Boolean v = trueVerifier.verify(trueVerifier.getEntry(s), new ComparableArrayOf<>(output));
            if(v){
                success = success.add(BigDecimal.ONE);
            }
        }
        return success.divide(BigDecimal.valueOf(differentStimuli+differentStimuli), 18, RoundingMode.HALF_UP); 
    }
}
