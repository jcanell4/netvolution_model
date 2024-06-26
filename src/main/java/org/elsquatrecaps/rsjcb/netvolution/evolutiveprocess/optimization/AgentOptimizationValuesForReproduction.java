package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;

/**
 *
 * @author josep
 */
public class AgentOptimizationValuesForReproduction implements Comparable<AgentOptimizationValuesForReproduction> {
    
    private int id;
    private BigDecimal performance;
    private BigDecimal vitalAdvantage;
    private BigDecimal reprodutciveAdvantage;
    protected BigDecimal reproductionRate = BigDecimal.ZERO;

    public AgentOptimizationValuesForReproduction(int id) {
        this.id = id;
        this.vitalAdvantage = BigDecimal.ZERO;
        this.reprodutciveAdvantage = BigDecimal.ZERO;
    }

    public AgentOptimizationValuesForReproduction(int id, BigDecimal[] performanceAndReprodutciveAdvantage) {
        this.id = id;
        if(performanceAndReprodutciveAdvantage.length==3){
            this.performance = performanceAndReprodutciveAdvantage[0];
            this.vitalAdvantage = performanceAndReprodutciveAdvantage[1];
            this.reprodutciveAdvantage = performanceAndReprodutciveAdvantage[2];
        }else{
            this.performance = this.vitalAdvantage = performanceAndReprodutciveAdvantage[0];
            this.reprodutciveAdvantage = performanceAndReprodutciveAdvantage[1];
        }
    }

    public AgentOptimizationValuesForReproduction(int id, BigDecimal vitalAdvantage, BigDecimal reprodutciveAdvantage) {
        this.id = id;
        this.performance = this.vitalAdvantage = vitalAdvantage;
        this.reprodutciveAdvantage = reprodutciveAdvantage;
    }

    public AgentOptimizationValuesForReproduction(int id, BigDecimal performance, BigDecimal vitalAdvantage, BigDecimal reprodutciveAdvantage) {
        this.id = id;
        this.performance = performance;
        this.vitalAdvantage = vitalAdvantage;
        this.reprodutciveAdvantage = reprodutciveAdvantage;
    }

    public AgentOptimizationValuesForReproduction(int id, PtpNeuralNetworkTrueTableGlobalCalculator.PerformaceAndReproductiveAdvantage pra) {
        this.id = id;
        this.performance = pra.getPerformance();
        this.vitalAdvantage = pra.getVitalAdvantage();
        this.reprodutciveAdvantage = pra.getReproductiveAdvantage(true);
    }

    public BigDecimal getReporductiveAdvantageValue(boolean reproductiveAdvantageOnly) {
        BigDecimal ret;
        if (reproductiveAdvantageOnly) {
            ret = reprodutciveAdvantage;
        } else {
            ret = getReporductiveAdvantageValue();
        }
        return ret;
    }

    public BigDecimal getReporductiveAdvantageValue() {
        return vitalAdvantage.add(reprodutciveAdvantage);
    }

    @Override
    public int compareTo(AgentOptimizationValuesForReproduction t) {
        return getReporductiveAdvantageValue().compareTo(t.getReporductiveAdvantageValue());
    }

    public BigDecimal getVitalAdvantage() {
        return vitalAdvantage;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the reprodutciveAdvantage
     */
    public BigDecimal getReprodutciveAdvantage() {
        return reprodutciveAdvantage;
    }

    /**
     * @return the reproductionRate
     */
    public BigDecimal getReproductionRate() {
        return reproductionRate;
    }

    /**
     * @return the performance
     */
    public BigDecimal getPerformance() {
        return performance;
    }
}
