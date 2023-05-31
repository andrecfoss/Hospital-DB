/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;
import com.mycompany.projeto2.exceptions.PersonNotInListException;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author
 */
public abstract class Enfermeiro extends Pessoa { // deverá aplicar os curativos, etc.
    private String nome;
    private int anosCarreira;
    private static HashMap<Integer, String> pacientesEsperandoCurativo = new HashMap<Integer, String>();
    
    public Enfermeiro(String nome, int anosCarreira, int anoNascimento) {
        super(anoNascimento);
        this.nome = nome;
        this.anosCarreira = anosCarreira;
    }

    
    public static void addPacienteEsperandoCurativo(int id, String doenca){
        pacientesEsperandoCurativo.put(id, doenca);
    }
    public static void removePacienteEsperandoCurativo(int id){
        pacientesEsperandoCurativo.remove(id);
    }
    
    public boolean aplicarCurativo(int id) throws PersonNotInListException {
        if (pacientesEsperandoCurativo.containsKey(id)){
            Random rng = new Random();
            // aleatoriamente, aplica o curativo
            if (rng.nextInt(100) < 50.0) { // cura
                return true;
            }
            else { // não cura
                return false; 
            }
        }
        else {
            throw new PersonNotInListException("Paciente não está presente na lista de espera pelos curativos.");
        }
    }
    
    // Getter
    public static HashMap<Integer, String> getPacientesEsperandoCurativo(){
        return pacientesEsperandoCurativo;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getAnosCarreira() {
        return anosCarreira;
    }
    public void setAnosCarreira(int anosCarreira) {
        this.anosCarreira = anosCarreira;
    }
    
    
    
    
    @Override
    public String toString() {
        return super.toString()+", Nome: "+nome+", Anos de Carreira: "+anosCarreira;
    }
}
