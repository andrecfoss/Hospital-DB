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
public class NeedMoreAuxiliaryNursesException extends Exception {

    /**
     * Creates a new instance of <code>NotEnoughAuxiliaryNursesException</code>
     * without detail message.
     */
    public NeedMoreAuxiliaryNursesException() {
    }

    /**
     * Constructs an instance of <code>NotEnoughAuxiliaryNursesException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NeedMoreAuxiliaryNursesException(String msg) {
        super(msg);
    }
}
