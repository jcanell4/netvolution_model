/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.EvolutionaryCycleInfo;

/**
 *
 * @author josep
 */
public class PausedEvolutionaryProcessEvent extends EvolutionaryEvent{
    private static final long serialVersionUID = 2478541542732890006L;        
    public static final String eventType ="PausedProcess";
    
    protected PausedEvolutionaryProcessEvent(int id, String type) {
        super(id, type);
    }

    protected PausedEvolutionaryProcessEvent(String type) {
        super(type);
    }

    public PausedEvolutionaryProcessEvent() {
        this(eventType);
    }
    
    public PausedEvolutionaryProcessEvent(int id) {
        this(id, eventType);
    }
    
}
