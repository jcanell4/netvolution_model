package org.elsquatrecaps.rsjcb.netvolution.events;

import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.lineage.ParentLine;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
public class ProgenyLinesEvent extends EvolutionaryEvent{
    private static final long serialVersionUID = 2478541542732890008L;        
    public static final String eventType ="ProgenyLines";
    private ParentLine<PtpNeuralNetwork>[] progenyLines;   

    
    protected ProgenyLinesEvent(String type, ParentLine<PtpNeuralNetwork>[] progenyLines) {
        super(type);
        this.progenyLines = progenyLines;
    }

    protected ProgenyLinesEvent(int id, String type, ParentLine<PtpNeuralNetwork>[] progenyLines) {
        super(id, type);
        this.progenyLines = progenyLines;
    }

    public ProgenyLinesEvent(ParentLine<PtpNeuralNetwork>[] progenyLines) {
        this(eventType, progenyLines);
    }

    public ProgenyLinesEvent(int id, ParentLine<PtpNeuralNetwork>[] progenyLines) {
        this(id, eventType, progenyLines);
    }
    
    public ParentLine<PtpNeuralNetwork>[]  getProgenyLines() {
        return progenyLines;
    }
}
