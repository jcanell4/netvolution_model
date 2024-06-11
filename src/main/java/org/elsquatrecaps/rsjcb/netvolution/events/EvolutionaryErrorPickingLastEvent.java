/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

/**
 *
 * @author josep
 */
public class EvolutionaryErrorPickingLastEvent extends ErrorOnProcessEvolution{
    private static final long serialVersionUID = 2478541542732890002L;    
    public static final String eventType ="ErrorPickingLastEvent";

    public EvolutionaryErrorPickingLastEvent(int id, InterruptedException ex) {
        super(id, eventType, ex);
    }

    public EvolutionaryErrorPickingLastEvent(InterruptedException ex) {
        super(eventType, ex);
    }

    @Override
    public InterruptedException getException() {
        return (InterruptedException) super.getException();
    }
}
