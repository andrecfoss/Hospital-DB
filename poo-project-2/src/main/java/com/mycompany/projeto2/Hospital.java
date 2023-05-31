/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;
import com.mycompany.projeto2.exceptions.CantTreatSelfException;
import com.mycompany.projeto2.exceptions.ExceededPatientCapacityException;
import com.mycompany.projeto2.exceptions.NurseCareerTooShortException;
import com.mycompany.projeto2.exceptions.PersonNotInListException;
import com.mycompany.projeto2.exceptions.PersonAlreadyInListException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/**
 *
 * @author 
 */
public class Hospital {
    private int anosParaPromoverEnf; // anos para promover um especialista para chefe
    private HashMap<Integer, Pessoa> pessoas; // hash map de todas as pessoas
    
    private ArrayList<Integer> medicos; 
    private ArrayList<Integer> listaEspera;
    private ArrayList<Integer> enfermeiros; // todos os enfermeiros
    private ArrayList<Integer> enfermeirosAux; 
    private ArrayList<Integer> enfermeirosEsp; 
    private ArrayList<Integer> enfermeirosChefe; 
    
    // atributos relativos ao historico
    private ArrayList<String> historicoRelatorios; // guarda todos os relatórios que são feitos pelo hospital
    private int contaCOVID, contaHIV, contaEbola, contaMortes;
    private int contaPacientes, contaTestes;
    
    
    // Construtor do hospital
    public Hospital(int anosParaPromoverEnf){
        this.anosParaPromoverEnf = anosParaPromoverEnf;
        pessoas = new HashMap<Integer, Pessoa>();
        medicos = new ArrayList<Integer>();
        listaEspera = new ArrayList<Integer>();
        
        enfermeiros = new ArrayList<Integer>();
        enfermeirosAux = new ArrayList<Integer>();
        enfermeirosEsp = new ArrayList<Integer>();
        enfermeirosChefe = new ArrayList<Integer>();
        
        historicoRelatorios = new ArrayList<String>();
    }
    
    /**
     * Adiciona a pessoa passada como argumento ao hospital.
     * @param pessoa
     * @throws PersonAlreadyInListException 
     */
    public void addPessoa(Pessoa pessoa) throws PersonAlreadyInListException {
        if (!pessoas.containsKey(pessoa.getID())){
            pessoas.put(pessoa.getID(), pessoa); // adiciona a pessoa ao mapa geral de todas pessoas
            
            if (pessoa.getClass() == Medico.class) { // se a pessoa é médico
                medicos.add(pessoa.getID());
            }
            else if (pessoa instanceof Enfermeiro){ // se a pessoa é enfermeiro
                enfermeiros.add(pessoa.getID());
                
                if (pessoa.getClass() == EnfermeiroAuxiliar.class){
                    enfermeirosAux.add(pessoa.getID());
                }
                
                if (pessoa instanceof EnfermeiroEspecialista){ 
                    enfermeirosEsp.add(pessoa.getID());
                    if (((EnfermeiroEspecialista)pessoa).isChefe()) { // chefes são também especialistas
                        enfermeirosChefe.add(pessoa.getID());
                    }
                }
            }
            else { // se não é médico nem enfermeiro a entrar no hospital, então será paciente, e será posto na lista de espera.
                listaEspera.add(pessoa.getID());
                contaPacientes++;
            }
        }
        else {
            throw new PersonAlreadyInListException("Pessoa já estava no hospital.");
        }
    }
    
    /**
     * Remove a pessoa passada como argumento do hospital.
     * @param pessoa
     * @throws PersonNotInListException 
     */
    public void removerPessoa(Pessoa pessoa) throws PersonNotInListException {
        if (pessoas.containsKey(pessoa.getID())){
            pessoas.remove(pessoa.getID());
            
            if (pessoa.getClass() == Medico.class) {
                medicos.remove(medicos.indexOf(pessoa.getID()));
            }
            else if (pessoa instanceof Enfermeiro){
                enfermeiros.remove(enfermeiros.indexOf(pessoa.getID()));
                
                if (pessoa.getClass() == EnfermeiroAuxiliar.class){
                    enfermeirosAux.remove(enfermeirosAux.indexOf(pessoa.getID()));
                }
                
                if (pessoa instanceof EnfermeiroEspecialista){
                    enfermeirosEsp.remove(enfermeirosEsp.indexOf(pessoa.getID()));
                    if (((EnfermeiroEspecialista)pessoa).isChefe()){
                        enfermeirosChefe.remove(enfermeirosChefe.indexOf(pessoa.getID()));
                    }
                }
            
                
                // se este enfermeiro estiver associado a um médico, queremos removê-lo desse médico também.
                for (int idMedico : medicos){ // para todos os médicos no hospital
                    Medico med = (Medico)getPessoa(idMedico);
                    if (med.temEnfermeiro(pessoa.getID())){ // verificamos se este médico tem o enfermeiro
                        med.removeEnfermeiro(pessoa.getID());
                        break; // e saímos do loop do for, logo o valor de "med" será o médico deste enfermeiro
                    }
                }
                // NOTA: fzer o mesmo para pacientes^ 
            }
            else if (listaEspera.contains(pessoa.getID())) { // se estiver em lista de espera, remove-a de lá.
                // listaEspera.remove(listaEspera.indexOf(pessoa.getID())); // não queremos enviar um exception aqui, pois não faz mal que não esteja na lista de espera (por exemplo, pode estar associada a um médico)
                removerDaListaDeEspera(pessoa);
            }
        }
        else {
            throw new PersonNotInListException("Esta pessoa não existe no hospital.");                
        }
    }
    
    /**
     * Remove a pessoa, passada como argumento, da lista de espera.
     * @param paciente
     * @throws PersonNotInListException 
     */
    public void removerDaListaDeEspera(Pessoa paciente) throws PersonNotInListException {
        if (listaEspera.contains(paciente.getID())) {
            listaEspera.remove(listaEspera.indexOf(paciente.getID()));
        }
        else { // se não estiver em lista de espera
            throw new PersonNotInListException("Paciente não estava na lista de espera.");
        }
    }
    
    /**
     * Esta função deverá associar o paciente ao médico, removendo-o da lista de espera.
     * @param medico
     * @param paciente 
     */
    public void associarPacienteAMedico(Pessoa paciente, Medico medico)
            throws ExceededPatientCapacityException, PersonAlreadyInListException, PersonNotInListException, CantTreatSelfException {
        // NOTA: é possível o médico associar-se a si mesmo como paciente.
        if (medico.equals(paciente)){
            throw new CantTreatSelfException("O paciente e o médico não podem ser a mesma pessoa.");
        }
        medico.addPaciente(paciente);
        removerDaListaDeEspera(paciente);
    }
    /**
     * Esta função deverá associar o paciente ao enfermeiro especialista.
     * @param enfermeiro
     * @param paciente 
     */
    public void associarPacienteAEnfermeiro(Pessoa paciente, EnfermeiroEspecialista enfermeiro) 
            throws ExceededPatientCapacityException, PersonAlreadyInListException {
        try {
            enfermeiro.addPaciente(paciente);
        }
        catch (PersonAlreadyInListException e) {
            System.out.println("Erro: Pessoa já estava associada ao enfermeiro!");
        }
        catch (ExceededPatientCapacityException e){
            System.out.println("Erro: O enfermeiro já está a cuidar de 3 pacientes! Capacidade cheia.");
        }
    }
    
    public void associarEnfermeiroAMedico(Enfermeiro enf, Medico medico) throws PersonAlreadyInListException {
        medico.addEnfermeiro(enf);
    }
    
    /**
     * Esta função promove o dado EnfermeiroEspecialista para chefe.
     * @param especialista
     * @throws PersonAlreadyInListException
     * @throws NurseCareerTooShortException 
     */
    public void promoverEnfermeiroEsp(EnfermeiroEspecialista especialista) throws PersonAlreadyInListException, NurseCareerTooShortException{
        if (especialista.getAnosCarreira() >= this.anosParaPromoverEnf) {
            if (!especialista.isChefe()) {
                especialista.promover();
                enfermeirosChefe.add(especialista.getID());
            }
            else {
                throw new PersonAlreadyInListException("Este enfermeiro já é chefe!");
            }
        }
        else {
            throw new NurseCareerTooShortException("A carreira deste enfermeiro é demasiado curta! Mínimo: " + this.anosParaPromoverEnf + " anos.");
        }
    }
    
    /**
     * Aumenta os anos de carreira de todos os enfermeiros no hospital.
     */
    public void atualizarAnosCarreiraEnf(){
        for (int idEnf : enfermeiros) { // int porque 'enfermeiros' é arraylist de integers dos IDs 
            Enfermeiro enf = (Enfermeiro)pessoas.get(idEnf);
            enf.setAnosCarreira(enf.getAnosCarreira()+1);
        }
    }
    
    /**
     * Infeta pessoas aleatóriamente no hospital.
     * Chance de 50% de cada pessoa no hospital (incluindo médicos ou enfermeiros) ser infetada com uma doença.
     */
    public void virusOutbreak() throws PersonNotInListException{
        Random rng = new Random();
        int random;
        
        for (Pessoa pessoa : pessoas.values()){ // passa por cada pessoa no hospital
            if (!Enfermeiro.getPacientesEsperandoCurativo().containsKey(pessoa.getID())){ // só infeta se não estava já detetado como doente e à espera de curativo
                random = rng.nextInt(101);
                if (random < 50){ // 50% chance de entrar aqui e infetar

                    if (pessoa instanceof Medico){ // se for instancia de um Medico
                        Medico med = (Medico)pessoa;
                        for (int idPaciente : med.getPacientes().keySet()){ // queremos que os pacientes voltem para a lista de espera (pois já não estão a ser tratados por esse médico/enfermeiro)
                            med.removePaciente(idPaciente);
                            listaEspera.add(idPaciente);
                        }
                    }
                    
                    pessoa.infetar();
                    
                    if (!listaEspera.contains(pessoa.getID())) { // se não estiver na lista de espera
                        listaEspera.add(pessoa.getID()); // adiciona à lista de espera
                        contaPacientes++;
                    }
                    
                    //  NOTA:  queremos também remover o doutor/enfermeiro das suas listas respetivas, pois já não poderá efetuar as suas funcionalidades
                    
                }
            }
        }   
    }
    
    
    
    /**
     * Cria um relatório com várias informações sobre o estado atual do hospital no formato de uma String e retorna-o.
     */
    public String getNovoRelatorioHospitalar(){
        String r = "-> Relatório #"+ (historicoRelatorios.size()+1);
        
        r += "\nNº de pessoas atualmente no hospital: " + pessoas.size() + ". Total: " + Pessoa.getContadorID();
        r += "\n- Nº Doutores: " + medicos.size();
        r += "\n- Nº Enfermeiros: " + enfermeiros.size();
        r += "\n- Nº Pacientes (total): " + contaPacientes;
        r += "\n- Nº de Óbitos: " + contaMortes;
        
        r += "\nTotal de testes: " + contaTestes;
        r += "\n- Nº de testes positivos de COVID: " + contaCOVID;
        r += "\n- Nº de testes positivos de HIV: " + contaHIV;
        r += "\n- Nº de testes positivos de Ébola: " + contaEbola;
        double racioDoentes = ((double)(contaCOVID+contaHIV+contaEbola))/contaTestes;
        r += "\n- Rácio de testes positivos: " + racioDoentes;
        return r;
    }
    
    // Adiciona o String recebido à base de dados de relatórios hospitalares
    public void addRelatorioHospitalar(String novoRelatorio){
        historicoRelatorios.add(novoRelatorio);
    }
    
    // Retorna o relatório hospitalar desejado
    public String getRelatorioHospitalar(int num){
        return historicoRelatorios.get(num);
    }
    
    // Métodos que servem para adicionar aos contadores do histórico do hospital.
    public void addCountCOVID() {
        contaCOVID++;
    }   
    public void addCountHIV() {
        contaHIV++;
    }  
    public void addCountEbola() {
        contaEbola++;
    } 
    public void addCountTestes() {
        contaTestes++;
    }  
    public void addCountMortes() {
        contaMortes++;
    }  
    
    // Getters & setters
    public Pessoa getPessoa(int id) throws PersonNotInListException {
        if (pessoas.containsKey(id)){
            return pessoas.get(id);
        }
        else {
            throw new PersonNotInListException("Pessoa não existe no hospital.");
        }
    }

    public HashMap<Integer, Pessoa> getPessoas(){
        return pessoas;
    }
    public ArrayList<Integer> getIDPessoas(){ // retorna um ArrayList só dos IDs das pessoas no hospital
        return new ArrayList<Integer>(pessoas.keySet());
    }
    
    public ArrayList<Integer> getMedicos() {
        return medicos;
    }

    public ArrayList<Integer> getListaEspera() {
        return listaEspera;
    }

    public ArrayList<Integer> getEnfermeiros() {
        return enfermeiros;
    }
    public ArrayList<Integer> getEnfermeirosAux() {
        return enfermeirosAux;
    }
    public ArrayList<Integer> getEnfermeirosEsp() {
        return enfermeirosEsp;
    }
    public ArrayList<Integer> getEnfermeirosChefe() {
        return enfermeirosChefe;
    }

    /**
     * Getter do tamanho do histórico de relatórios
     * @return 
     */
    public int getNumRelatorios(){
        return historicoRelatorios.size();
    }
    
    @Override
    public String toString() {
        return "-- Hospital --\nAnos para promover: " + anosParaPromoverEnf + "\nPessoas no hospital: "+pessoas;
    }
}
