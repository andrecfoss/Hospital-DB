/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;
import java.util.Random;
/**
 *
 * @author
 */
public class Pessoa { // age como possivel paciente
    private int anoNascimento, id;
    private int contadorCurativos; // conta o numero de vezes que um curativo foi usado nesta pessoa. após um certo valor, morre.
    private static int contadorID; // usado para gerar novos IDs

    private double temperatura; // > 37.5 = infetado com COVID
    private int numGlobulosBrancos; // < 4500 = infetado com HIV
    private boolean temProblemasGastrointestinais; // true = infetado com Ébola
    
    // Construtor para pessoa saudável. Usado para criar staff do hospital.
    public Pessoa(int anoNascimento){
        this.anoNascimento = anoNascimento;
        contadorID++;
        this.id = contadorID;
        
        Random rng = new Random();
        temperatura = 37.5;
        numGlobulosBrancos = rng.nextInt(2000)+4500; 
        temProblemasGastrointestinais = false; 
    }
    
    // Construtor para pessoa que poderá estar doente. Se queremos que seja 100% doente, forcarInfecao deverá ser true.
    public Pessoa(int anoNascimento, boolean forcarInfecao){
        this.anoNascimento = anoNascimento;
        contadorID++;
        this.id = contadorID;
        
        Random rng = new Random();
        int random = rng.nextInt(100);
        temperatura = 37.5;
        numGlobulosBrancos = rng.nextInt(2000)+4500; 
        temProblemasGastrointestinais = false; 
        if (random < 70 || forcarInfecao) { // 70% chance de estar infetado e 30% de ser saudável (quando forcarInfecao for false)
            infetar();
        }
        
    }

    /**
     * Infeta esta pessoa.
     * Aleatóriamente muda os valores dos um dos seus atributos internos para valores que serão detetados por um diagnóstico Médico. (temperatura, numGlobulosBrancos e temProblemasGastrointestinais)
     */
    public void infetar(){
        Random rng = new Random();
        int infetado = rng.nextInt(3); // 0, 1 or 2
        switch (infetado) { // escolhe um dos atributos para infetar, ou seja, uma doença
            case 0: // COVID-19
                temperatura = rng.nextDouble()*2+37.5;
                break;
            case 1: // HIV
                numGlobulosBrancos = rng.nextInt(2000)+1000;
                break;
            case 2: // Ébola
                temProblemasGastrointestinais = true;
                break;
        }
    }
    
    // Getters e setters
    public int getAnoNascimento() {
        return anoNascimento;
    }
    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }
    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }
    public double getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
    public int getNumGlobulosBrancos() {
        return numGlobulosBrancos;
    }
    public void setNumGlobulosBrancos(int numGlobulosBrancos) {
        this.numGlobulosBrancos = numGlobulosBrancos;
    }
    public boolean temProblemasGastrointestinais() {
        return temProblemasGastrointestinais;
    }
    public void setProblemasGastrointestinais(boolean temProblemasGastrointestinais) {
        this.temProblemasGastrointestinais = temProblemasGastrointestinais;
    }

    public int getContadorCurativos() {
        return contadorCurativos;
    }
    public void setContadorCurativos(int contadorCurativos) {
        this.contadorCurativos = contadorCurativos;
    }

    
    
    public static int getContadorID(){
        return contadorID;
    }
    
    
    @Override
    public String toString() {
        return "ID: " + id + ", Ano de Nascimento: " + anoNascimento /*+ ", Temperatura: "+temperatura+
                ", Glóbulos brancos: "+numGlobulosBrancos+", Tem problemas gastrointestinais? "+temProblemasGastrointestinais*/;
    }

    /**
     * O método equals é redefinido para dizer que duas pessoas são iguais quando os seus IDs são iguais.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pessoa other = (Pessoa) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
