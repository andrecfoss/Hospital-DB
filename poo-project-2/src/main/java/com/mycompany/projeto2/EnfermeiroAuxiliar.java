/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;

/**
 *
 * @author
 */
public class EnfermeiroAuxiliar extends Enfermeiro {

    public EnfermeiroAuxiliar(String nome, int anosCarreira, int anoNascimento) {
        super(nome, anosCarreira, anoNascimento);
    }

    @Override
    public String toString() {
        return super.toString()+", Enfermeiro Auxiliar";
    }
    
    
    
}
