
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
    List<PtpNeuralNetworkSinglePropertyCalculator> performanceCalculators= new ArrayList<>();
    List<PtpNeuralNetworkSinglePropertyCalculator> reproductuvaAdvantageCalculators= new ArrayList<>();

//    public PtpNeuralNetworkTrueTableGlobalCalculator(String[] performanceCalculators, String[] reproductiveAdvantageCalculators) {
//        this(Arrays.asList(performanceCalculators), Arrays.asList(reproductiveAdvantageCalculators));        
//    }
    
    public PtpNeuralNetworkTrueTableGlobalCalculator(String[] performanceCalculators, String[] reproductiveAdvantageCalculators, Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        this(Arrays.asList(performanceCalculators), Arrays.asList(reproductiveAdvantageCalculators), environmentInputSet, environmentOutputSet);        
    }
    
//    public PtpNeuralNetworkTrueTableGlobalCalculator(List<String> performanceCalculators, List<String> reproductiveAdvantageCalculators) {
//        for(String k: performanceCalculators){
//            if("performance".equals(k) && SinglePropertyCalculatorItems.getItem(k).getInstance() instanceof PtpNeuralNetworkTrueTableSinglePropertyCalculator
//                    || !"performance".equals(k)){
//                this.performanceCalculators.add(SinglePropertyCalculatorItems.getItem(k).getInstance());
//            }
//        }
//        for(String k: reproductiveAdvantageCalculators){
//            this.reproductuvaAdvantageCalculators.add(SinglePropertyCalculatorItems.getItem(k).getInstance());
//        }
//        
//    }
    
    public PtpNeuralNetworkTrueTableGlobalCalculator(List<String> performanceCalculators, List<String> reproductiveAdvantageCalculators, Float[][] environmentInputSet, Float[][] environmentOutputSet) {
        PtpNeuralNetworkDifTableSinglePropertyCalculator calc = (PtpNeuralNetworkDifTableSinglePropertyCalculator) SinglePropertyCalculatorItems.getItem("performance").getInstance();
        this.performanceCalculators.add(calc);
        for(int i=0; i<environmentInputSet.length; i++){
            calc.addValueCorrespondence(new ComparableArrayOf<>(environmentInputSet[i]), new ComparableArrayOf<>(environmentOutputSet[i]));
        }
        for(String k: performanceCalculators){
            this.performanceCalculators.add(SinglePropertyCalculatorItems.getItem(k).getInstance());
        }
        for(String k: reproductiveAdvantageCalculators){
            this.reproductuvaAdvantageCalculators.add(SinglePropertyCalculatorItems.getItem(k).getInstance());
        }
    }
    
    public PerformaceAndReproductiveAdvantage calculate(PtpNeuralNetwork agent){
        BigDecimal p=BigDecimal.ZERO;
        BigDecimal ra=BigDecimal.ZERO;
        for(PtpNeuralNetworkSinglePropertyCalculator calc: performanceCalculators){
            p = p.add(calc.calculate(agent));
        }
        for(PtpNeuralNetworkSinglePropertyCalculator calc: reproductuvaAdvantageCalculators){
            ra = ra.add(calc.calculate(agent));
        }
        return new PerformaceAndReproductiveAdvantage(p, ra); 
    }
    
    public static class PerformaceAndReproductiveAdvantage{
        private BigDecimal reproductuvaAdvantage;
        private BigDecimal performace;

        public PerformaceAndReproductiveAdvantage(BigDecimal performace) {
            this.reproductuvaAdvantage = BigDecimal.ZERO;
            this.performace = performace;
        }
        
        public PerformaceAndReproductiveAdvantage(BigDecimal performace, BigDecimal reproductuvaAdvantage) {
            this.reproductuvaAdvantage = reproductuvaAdvantage;
            this.performace = performace;
        }

        public PerformaceAndReproductiveAdvantage() {
            this.reproductuvaAdvantage = BigDecimal.ZERO;
            this.performace = BigDecimal.ZERO;
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
            return reproductuvaAdvantage.add(performace);
        }

        public BigDecimal getPerformace() {
            return performace;
        }

    }
}
