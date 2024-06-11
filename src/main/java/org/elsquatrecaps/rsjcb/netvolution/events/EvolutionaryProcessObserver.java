/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

/**
 *
 * @author josep
 */
public interface EvolutionaryProcessObserver {
    void updateEvent(EvolutionaryEvent ev);
    EvolutionaryEvent lastEventUpdated();
    void notifyLastEvent();
//    boolean close(String fromThreadName);
}
