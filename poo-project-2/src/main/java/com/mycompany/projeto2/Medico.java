/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;
import com.mycompany.projeto2.exceptions.ExceededPatientCapacityException;
import com.mycompany.projeto2.exceptions.PersonNotInListException;
import com.mycompany.projeto2.exceptions.PersonAlreadyInListException;
import java.util.HashMap;
/**
 *
 * @author
 */
public class Medico extends Pessoa implements TemPacientes { // deverá dar altas e fazer os diagnosticos dos pacientes
    private String nome;
    private static final double MAXTEMP = 37.5;
    private static final int MINGLOBBRANCOS = 4500;
    private HashMap<Integer, Pessoa> pacientes;
    private HashMap<Integer, Enfermeiro> enfermeiros;

    public Medico(String nome, int anoNascimento) {
        super(anoNascimento);
        this.nome = nome;
        pacientes = new HashMap<Integer, Pessoa>();
        enfermeiros = new HashMap<Integer, Enfermeiro>();
    }
    
    
    /**
     * Associa um novo paciente ao médico. O médico tem no máximo 3 pacientes.
     * @param paciente
     * @throws ExceededPatientCapacityException 
     */
    public void addPaciente(Pessoa paciente) throws ExceededPatientCapacityException, PersonAlreadyInListException {
        if (pacientes.size()<3) {
            if (!this.temPaciente(paciente)) { 
                pacientes.put(paciente.getID(), paciente);   
            }
            else {
                throw new PersonAlreadyInListException("O paciente já estava associado a este médico.");
            }
        }
        else {
            throw new ExceededPatientCapacityException("O médico já tem 3 pacientes! Capacidade cheia.");
        }
    }

    /**
     * Associa um novo Enfermeiro ao médico. O médico tem quantos enfermeiros quanto quiser..
     * @param paciente
     * @throws ExceededPatientCapacityException 
     */
    public void addEnfermeiro(Enfermeiro enfermeiro) throws PersonAlreadyInListException {
        if (!this.temEnfermeiro(enfermeiro)) { 
            enfermeiros.put(enfermeiro.getID(), enfermeiro);   
        }
        else {
            throw new PersonAlreadyInListException("O enfermeiro já estava associado ao médico!");
        }
    }
    
    
    /**
     * Getter do HashMap<Integer, Pessoa> dos pacientes deste médico.
     * @return 
     */
    public HashMap<Integer, Pessoa> getPacientes(){
        return pacientes;
    }
    /**
     * Getter do HashMap<Integer, Pessoa> dos enfermeiros deste médico.
     * @return 
     */
    public HashMap<Integer, Enfermeiro> getEnfermeiros(){
        return enfermeiros;
    }
        
    
    /**
     * Retorna se a pessoa passada como argumento é ou não paciente deste médico.
     * @param pessoa
     * @return 
     */
    public boolean temPaciente(Pessoa pessoa){
        return pacientes.containsValue(pessoa);
    }
    public boolean temPaciente(int id){
        return pacientes.containsKey(id);
    }
    
    /**
     * Retorna se a pessoa passada como argumento é ou não enfermeiro deste médico.
     * @param pessoa
     * @return 
     */
    public boolean temEnfermeiro(Enfermeiro enfermeiro){
        return enfermeiros.containsValue(enfermeiro);
    }
    public boolean temEnfermeiro(int id){
        return enfermeiros.containsKey(id);
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
            throw new PersonNotInListException("Esta pessoa não é paciente deste médico.");
        }
    }
    /**
     * Remove um enfermeiro deste médico.
     * @param id
     * @throws PersonNotInListException 
     */
    public void removeEnfermeiro(int id) throws PersonNotInListException {
        if (temEnfermeiro(id)){
            enfermeiros.remove(id);
        }
        else {
            throw new PersonNotInListException();
        }
    }
    
    
    /**
     * Diagnostica a pessoa.Retorna true se diagnóstico for positivo a algo, false se for negativo a todos os testes. Se estiver doente, deverá enviar este paciente para uma lista de pessoas dos enfermeiros que guardam o curativo que esta pessoa precisa.
     * @param paciente
     * @throws PersonNotInListException
     * @return 
     */
    public boolean diagnostica(Pessoa paciente) throws PersonNotInListException {
        if (temPaciente(paciente)){
            if (paciente.getTemperatura() > MAXTEMP){
                Enfermeiro.addPacienteEsperandoCurativo(paciente.getID(),"COVID-19");
                return true;
            }
            if (paciente.getNumGlobulosBrancos() < MINGLOBBRANCOS) {
                Enfermeiro.addPacienteEsperandoCurativo(paciente.getID(),"HIV");
                return true;
            }
            if (paciente.temProblemasGastrointestinais()) {
                Enfermeiro.addPacienteEsperandoCurativo(paciente.getID(),"Ébola");
                return true;
            }
            return false;
        }
        else{
            throw new PersonNotInListException("Pessoa não é paciente deste médico.");
        }
    }
    /**
     * Remove o paciente indicado do grupo associado ao médico, e seguidamente retorna se deverá também ser removido do hospital. 
     * @param paciente 
     * @return  
     */
    public boolean darAlta(Pessoa paciente) throws PersonNotInListException {
        // se o diagnóstico deu negativo a este paciente, então podemos prosseguir a removê-lo e retornar true para indicar que se deu alta
        // se o diagnostico deu positivo, retorna-se falso, e não se remove o paciente
        
        
        if (!diagnostica(paciente)) {
            removePaciente(paciente.getID());
            return true;
        }
        else {
            return false; // e a pessoa continua a ser paciente deste médico
        }
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    
    
    public String toString(){
        return super.toString()+", Nome: "+nome+", Pacientes: "+pacientes.keySet()+", Enfermeiros: "+enfermeiros.keySet()+", Medico";
    }
}
