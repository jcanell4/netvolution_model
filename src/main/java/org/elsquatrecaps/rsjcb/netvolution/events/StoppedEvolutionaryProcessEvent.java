/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
public class StoppedEvolutionaryProcessEvent extends CompletedEvolutionaryProcessEvent {
    private static final long serialVersionUID = 2478541542732890005L;        
    public static final String eventType ="StopedProcess";

    public StoppedEvolutionaryProcessEvent(PtpNeuralNetwork[] finalPopulation) {
        super(eventType, finalPopulation);
    }

    protected StoppedEvolutionaryProcessEvent(String type, PtpNeuralNetwork[] finalPopulation) {
        super(type, finalPopulation);
    }

    protected StoppedEvolutionaryProcessEvent(int id, String type, PtpNeuralNetwork[] finalPopulation) {
        super(id, type, finalPopulation);
    }
    
    public StoppedEvolutionaryProcessEvent(int id, PtpNeuralNetwork[] finalPopulation) {
        super(id, eventType, finalPopulation);
    }

//    public StoppedEvolutionaryProcessEvent(String type, PtpNeuralNetwork[] finalPopulation) {
//        super(type, finalPopulation);
//    }
    
//    public StoppedEvolutionaryProcessEvent(PtpNeuralNetwork[] finalPopulation) {
//        super(finalPopulation);
//    }
    
}
