package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.lineage;

import java.io.Serializable;

/**
 *
 * @author josep
 * @param <T>
 */
public class ParentLine<T extends Serializable> implements Serializable{
    private static int nextId=0;
    private final int agetId;
    private ParentLine<T> parent;
    private T agent;
    
    public ParentLine(ParentLine<T> parent, T agent) {
        this.agetId = nextId++;
        this.agent = agent;
        this.parent = parent;
    }

    /**
     * @return the generationId
     */
    public int getAgentId() {
        return agetId;
    }

    /**
     * @return the parent
     */
    public ParentLine<T> getParent() {
        return parent;
    }

    /**
     * @return the agent
     */
    public T getAgent() {
        return agent;
    }
}
