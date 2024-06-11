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
public interface ResponseVerifier<I, O, R> {
    
    public abstract R verify(I input, O output);
    
}
