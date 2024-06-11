package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

/**
 *
 * @author josep
 */
public enum SurviveOptimizationMethodValues {
    AVERAGE(0, "AVERAGE", "Average of vital advantages");
    private static SurviveOptimizationMethodValues[] ovalues;
    static {
        ovalues = new SurviveOptimizationMethodValues[SurviveOptimizationMethodValues.values().length];
        for(SurviveOptimizationMethodValues it: SurviveOptimizationMethodValues.values()){
            ovalues[it.getId()] = it;
        }
    }
    
    private final int id;
    private final String value;
    private final String name;

    private SurviveOptimizationMethodValues(int id, String value, String name) {
        this.name = name;
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public String getName(){
        return name;
    }
    
    public int getId(){
        return id;
    }
    
    public String getValue(){
        return value;
    }
    
    public static SurviveOptimizationMethodValues getValueById(int id){
        return ovalues[id];
    }
}
