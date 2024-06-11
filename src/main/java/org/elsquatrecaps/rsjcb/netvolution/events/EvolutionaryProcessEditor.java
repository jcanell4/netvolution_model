package org.elsquatrecaps.rsjcb.netvolution.events;

import java.util.function.Consumer;

/**
 *
 * @author josep
 */
public interface EvolutionaryProcessEditor {

    void publish(String type, EvolutionaryEvent event);
    void subscribe(String type, EvolutionaryProcesSubscriptor s);
    void subscribe(String type, Consumer<EvolutionaryEvent> s);
}
