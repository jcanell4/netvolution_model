/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

/**
 *
 * @author josep
 */
public class RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder extends PtpVectorNeuralNetworkTrueTableEvolutionaryEnvironmentBuilder{
    @Override
    protected RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment instanceEnvironment(PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor cycleProcessor) {
        return new RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment(cycleProcessor);
    }
}
