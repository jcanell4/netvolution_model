/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.events;

/**
 *
 * @author josep
 */
public interface ThreadableService {
    void start();
    void start(String name);
    void finish();
    boolean isFinshRequired();
    void join();
}