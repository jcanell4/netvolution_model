/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

/**
 *
 * @author josep
 * @param <T>
 */
public interface EvolutionaryEventStore<T extends EvolutionaryEvent> extends EvolutionaryProcessObserver{
    T getNextEvent();
    T peeklastEvent();
}
