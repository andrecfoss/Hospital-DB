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
public class PersonAlreadyInListException extends Exception {

    /**
     * Creates a new instance of <code>PersonAlreadyInListException</code>
     * without detail message.
     */
    public PersonAlreadyInListException() {
    }

    /**
     * Constructs an instance of <code>PersonAlreadyInListException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PersonAlreadyInListException(String msg) {
        super(msg);
    }
}
