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
public class PersonNotInListException extends Exception {

    /**
     * Creates a new instance of <code>PatientNotInListException</code> without
     * detail message.
     */
    public PersonNotInListException() {
    }

    /**
     * Constructs an instance of <code>PatientNotInListException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PersonNotInListException(String msg) {
        super(msg);
    }
}
