/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;
import com.mycompany.projeto2.exceptions.ExceededPatientCapacityException;
import com.mycompany.projeto2.exceptions.PersonAlreadyInListException;
import com.mycompany.projeto2.exceptions.PersonNotInListException;
import java.util.HashMap;

/**
 *
 * @author
 */
public class EnfermeiroEspecialista extends Enfermeiro implements TemPacientes {
    private HashMap<Integer, Pessoa> pacientes;
    private boolean isChefe; // alguns métodos deverão requerir que o especialista seja chefe.

    public EnfermeiroEspecialista(String nome, int anosCarreira, int anoNascimento) {
        super(nome, anosCarreira, anoNascimento);
        pacientes = new HashMap<Integer, Pessoa>();
    }
    
    /**
     * Associa um novo paciente ao especialista. Tem no máximo 3 pacientes.
     * @param paciente
     * @throws ExceededPatientCapacityException 
     */
    public void addPaciente(Pessoa paciente) throws ExceededPatientCapacityException, PersonAlreadyInListException {
        if (pacientes.size()<3) {
            if (!temPaciente(paciente)) { 
                pacientes.put(paciente.getID(), paciente);   
            }
            else {
                throw new PersonAlreadyInListException();
            }
        }
        else {
            throw new ExceededPatientCapacityException();
        }
    }
    
    /**
     * Getter do HashMap<Integer, Pessoa> dos pacientes deste enfermeiro.
     * @return 
     */
     
    public HashMap<Integer, Pessoa> getPacientes() {
        return pacientes;
    }
    
    /**
     * Retorna se a pessoa passada como argumento é ou não paciente deste enfermeiro.
     * @param pessoa
     * @return 
     */
    public boolean temPaciente(Pessoa pessoa){
        return pacientes.containsValue(pessoa);
    }
    /**
     * Retorna se a pessoa com o ID passado como argumento é ou não paciente deste enfermeiro.
     * @param id
     * @return 
     */
    public boolean temPaciente(int id){
        return pacientes.containsKey(id);
    }
    
    /**
     * Remove um paciente deste médico.
     * @param id
     * @throws PersonNotInListException 
     */
    public void removePaciente(int id) throws PersonNotInListException {
        if (temPaciente(id)){
            pacientes.remove(id);
        }
        else {
            throw new PersonNotInListException();
        }
    }

    // Setter
    public void setPacientes(HashMap<Integer, Pessoa> pacientes) {
        this.pacientes = pacientes;
    }
    
    /**
     * Promove este enfermeiro-especialista para chefe.
     */
    public void promover(){
        isChefe = true;
    }
    /**
     * Retorna se este enfermeiro-especialista é chefe.
     * @return 
     */
    public boolean isChefe(){
        return isChefe;
    }
    
    
    @Override
    public String toString() {
        String s = super.toString() + ", Pacientes: " + pacientes.keySet();
        if (isChefe){
            s += ", Enfermeiro Chefe";
        }
        else {
            s += ", Enfermeiro Especialista";
        }
        return s;
    }
}
