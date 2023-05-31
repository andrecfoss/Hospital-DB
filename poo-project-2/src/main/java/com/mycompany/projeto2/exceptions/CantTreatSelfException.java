/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2.exceptions;

/**
 *
 * @author
 */
public class CantTreatSelfException extends Exception {

    /**
     * Creates a new instance of <code>CantTreatSelfException</code> without
     * detail message.
     */
    public CantTreatSelfException() {
    }

    /**
     * Constructs an instance of <code>CantTreatSelfException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CantTreatSelfException(String msg) {
        super(msg);
    }
}
