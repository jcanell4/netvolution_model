
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
public class PtpNeuralNetworkTrueTableGlobalCalculator{
    List<PtpNeuralNetworkSinglePropertyCalculator> vitalAdvantageCalculators= new ArrayList<>();
    List<PtpNeuralNetworkSinglePropertyCalculator> reproductuvaAdvantageCalculators= new ArrayList<>();

//    public PtpNeuralNetworkTrueTableGlobalCalculator(String[] vitalAdvantageCalculators, String[] reproductiveAdvantageCalculators) {
//        this(Arrays.asList(vitalAdvantageCalculators), Arrays.asList(reproductiveAdvantageCalculators));        
//    }
    
    public PtpNeuralNetworkTrueTableGlobalCalculator(String[] vitalAdvantageCalculators, String[] reproductiveAdvantageCalculators, Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        this(Arrays.asList(vitalAdvantageCalculators), Arrays.asList(reproductiveAdvantageCalculators), environmentInputSet, environmentOutputSet);        
    }
    
    public PtpNeuralNetworkTrueTableGlobalCalculator(List<String> vitalAdvantageCalculators, List<String> reproductiveAdvantageCalculators, Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        PtpNeuralNetworkDifTableSinglePropertyCalculator calc = (PtpNeuralNetworkDifTableSinglePropertyCalculator) SinglePropertyCalculatorItems.getItem("performance").getInstance();
        this.vitalAdvantageCalculators.add(calc);
        for(int i=0; i<environmentInputSet.length; i++){
            calc.addValueCorrespondence(new ComparableArrayOf<>(environmentInputSet[i]), new ComparableArrayOf<>(environmentOutputSet[i]));
        }
        for(String k: vitalAdvantageCalculators){
            this.vitalAdvantageCalculators.add(SinglePropertyCalculatorItems.getItem(k).getInstance());
        }
        for(String k: reproductiveAdvantageCalculators){
            this.reproductuvaAdvantageCalculators.add(SinglePropertyCalculatorItems.getItem(k).getInstance());
        }
    }
    
    public PerformaceAndReproductiveAdvantage calculate(PtpNeuralNetwork agent){
        BigDecimal p=BigDecimal.ZERO;
        BigDecimal va=BigDecimal.ZERO;
        BigDecimal ra=BigDecimal.ZERO;
        
        p = vitalAdvantageCalculators.get(0).calculate(agent);
        va = BigDecimal.valueOf(p.doubleValue()*(
                vitalAdvantageCalculators.size()
                +vitalAdvantageCalculators.size()
                +reproductuvaAdvantageCalculators.size()
                +reproductuvaAdvantageCalculators.size())
        );
        for(int i=1; i<vitalAdvantageCalculators.size(); i++){
            PtpNeuralNetworkSinglePropertyCalculator calc = vitalAdvantageCalculators.get(i);
            va = va.add(calc.calculate(agent));
        }
        for(PtpNeuralNetworkSinglePropertyCalculator calc: reproductuvaAdvantageCalculators){
            ra = ra.add(calc.calculate(agent));
        }
        return new PerformaceAndReproductiveAdvantage(p, va, ra); 
    }
    
    public static class PerformaceAndReproductiveAdvantage{
        final private BigDecimal performance;
        final private BigDecimal reproductuvaAdvantage;
        final private BigDecimal vitalAdvantage;

        public PerformaceAndReproductiveAdvantage(BigDecimal performace) {
            this.reproductuvaAdvantage = BigDecimal.ZERO;
            this.performance = this.vitalAdvantage = performace;
        }
        
        public PerformaceAndReproductiveAdvantage(BigDecimal performace, BigDecimal vitalAdvantage) {
            this.reproductuvaAdvantage = BigDecimal.ZERO;
            this.vitalAdvantage = vitalAdvantage;
            this.performance = performace;
        }
        
        public PerformaceAndReproductiveAdvantage(BigDecimal performace, BigDecimal vitalAdvantage, BigDecimal reproductuvaAdvantage) {
            this.reproductuvaAdvantage = reproductuvaAdvantage;
            this.vitalAdvantage = vitalAdvantage;
            this.performance = performace;
        }

        public PerformaceAndReproductiveAdvantage() {
            this.reproductuvaAdvantage = BigDecimal.ZERO;
            this.vitalAdvantage = BigDecimal.ZERO;
            this.performance = BigDecimal.ZERO;
        }

        public BigDecimal getReproductiveAdvantage(boolean reproductiveAdvantageOnly) {
            BigDecimal ret;
            if(reproductiveAdvantageOnly){
                ret = reproductuvaAdvantage;
            }else{
                ret = getReproductiveAdvantage();
            }
            return ret;
        }
        
        public BigDecimal getReproductiveAdvantage() {
            return reproductuvaAdvantage.add(vitalAdvantage);
        }

        public BigDecimal getVitalAdvantage() {
            return vitalAdvantage;
        }

        /**
         * @return the performance
         */
        public BigDecimal getPerformance() {
            return performance;
        }
    }
}
