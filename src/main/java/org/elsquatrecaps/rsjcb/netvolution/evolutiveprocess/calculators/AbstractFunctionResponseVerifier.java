/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

/**
 *
 * @author josep
 * @param <I>
 * @param <O>
 * @param <R>
 */
public abstract class AbstractFunctionResponseVerifier<I extends Comparable<I>, O extends Comparable<O>, R> implements ResponseVerifier<I, O, R> {
    private  ResponseFunction<I, O> function;

    public AbstractFunctionResponseVerifier() {
    }

    public AbstractFunctionResponseVerifier(ResponseFunction<I, O> function) {
        this.function = function;
    }

    public void setFunction(ResponseFunction<I, O> function) {
        this.function = function;
    }

    protected ResponseFunction<I, O> getFunction() {
        return function;
    }
    
    public static interface ResponseFunction<E, S>{
        S responseCalulate(E input);
    }
}
