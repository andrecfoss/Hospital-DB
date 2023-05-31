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
 * Esta interface contém as estruturas dos métodos referentes a pacientes, que são .
 * Implementado pelas classes Medico e Enfermeiro.
 */
public interface TemPacientes {
    public void addPaciente(Pessoa paciente) throws ExceededPatientCapacityException, PersonAlreadyInListException;
    public HashMap<Integer, Pessoa> getPacientes();
    public boolean temPaciente(Pessoa pessoa);
    public boolean temPaciente(int id);
    public void removePaciente(int id) throws PersonNotInListException;
}
