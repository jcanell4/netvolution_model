package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

/**
 *
 * @author josep
 */
public class PtpNeuralNetworkTrueTableEvolutionaryCycleProcessorBuilder extends PtpNeuralNetworkTrueTableDataBuilder<PtpNeuralNetworkTrueTableEvolutionaryCycleProcessorBuilder> {
    

    protected PtpNeuralNetworkTrueTableEvolutionaryCycleProcessorBuilder() {
    }

    
    public PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor build(){
        PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor ret = new PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor(population, mutationProcessor);
        if (optimizationMethod!=null){
            ret.setOptimizationMethod(optimizationMethod);
        }
        if (performanceCalculator!=null){
            ret.setPerformanceCalculator(performanceCalculator);
        }
        if(nnPropertiesToFollow!=null){
            ret.setPropertiesToFollow(nnPropertiesToFollow).equals(ret);
        }
        if(infoPropertiesToFollow!=null){
            ret.setInfoPropertiesToFollow(infoPropertiesToFollow).equals(ret);
        }
        if(selectionBestCandidateMethod!=null){
            ret.setSelectionBestCandidateMethod(selectionBestCandidateMethod);
        }
        ret.setSurvivalRateForOptimizationMethod(survivalRateForOptimizationMethod);
        return ret;
    }
    
}
