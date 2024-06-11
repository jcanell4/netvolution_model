
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

/**
 *
 * @author josep
 * @param <I>
 * @param <O>
 */
public class CalcFunctionResponseVerifier<I extends Number & Comparable<I>, O extends Number & Comparable<O>> extends AbstractFunctionResponseVerifier<I,O, Float> {

    public CalcFunctionResponseVerifier(ResponseFunction<I, O> function) {
        super(function);
    }

    public CalcFunctionResponseVerifier() {
    }
    
    @Override
    public Float verify(I input, O output) {
        float d1 = output.floatValue();
        float d2 = getFunction().responseCalulate(input).floatValue();
        return d1-d2;
    }
    
}
