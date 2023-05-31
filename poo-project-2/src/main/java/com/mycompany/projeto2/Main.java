/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto2;
import com.mycompany.projeto2.exceptions.PersonNotInListException;
import com.mycompany.projeto2.exceptions.PersonAlreadyInListException;
import java.util.Scanner;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author
 */
public class Main {
    private static Hospital hospital = new Hospital(10);
    
    public static void main(String[] args) {
        menuInicial();
    }
    
    /**
     * Lê o ficheiro hospital.txt e carrega o hospital a partir deste mesmo, incluindo as pessoas (nas suas classes corretas), lista de pacientes à espera de curativo, e relatórios. 
     */
    private static void lerFicheiro(){
        try {
            hospital = new Hospital(10); // cria um novo hospital para onde irá carregar os dados
            
            String ficheiro = "hospital.txt";
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            
            String linha = "";
            
            br.readLine(); // skip à linha Pessoas no hospital
            linha = br.readLine(); // passar à próxima linha
            while (!linha.equals("|")) { // para todas as Pessoas no hospital
                // extrair info e inserir pessoas no hospital.
                
                String[] partes = linha.split(" "); // separa a linha
                
                // extrair informação
                String classe = partes[0];
                int id = Integer.parseInt(partes[1]);
                int anoNascimento = Integer.parseInt(partes[2]);
                int numGlobBrancos = Integer.parseInt(partes[3]);
                double temperatura = Double.parseDouble(partes[4]);
                boolean probGastrointestinais = Boolean.parseBoolean(partes[5]);
                    
                // determinar qual a classe da pessoa e adicioná-la ao hospital
                if (classe.equals("Medico")){
                    String nome = partes[6];
                    
                    Medico med = new Medico(nome, anoNascimento);
                    
                    med.setID(id);
                    med.setNumGlobulosBrancos(numGlobBrancos);
                    med.setTemperatura(temperatura);
                    med.setProblemasGastrointestinais(probGastrointestinais);
                    
                    hospital.addPessoa(med);
                }
                else if (classe.equals("EnfermeiroEspecialista")){
                    String nome = partes[6];
                    int anosCarreira = Integer.parseInt(partes[7]);
                    boolean isChefe = Boolean.parseBoolean(partes[8]);
                    
                    EnfermeiroEspecialista enfEsp = new EnfermeiroEspecialista(nome, anosCarreira, anoNascimento);
                    
                    if (isChefe) { enfEsp.promover(); }
                    
                    enfEsp.setID(id);
                    enfEsp.setNumGlobulosBrancos(numGlobBrancos);
                    enfEsp.setTemperatura(temperatura);
                    enfEsp.setProblemasGastrointestinais(probGastrointestinais);
                    
                    hospital.addPessoa(enfEsp);
                }
                else if (classe.equals("EnfermeiroAuxiliar")){
                    String nome = partes[6];
                    int anosCarreira = Integer.parseInt(partes[7]);
                    
                    EnfermeiroAuxiliar enfAux = new EnfermeiroAuxiliar(nome, anosCarreira, anoNascimento);
                    
                    enfAux.setID(id);
                    enfAux.setNumGlobulosBrancos(numGlobBrancos);
                    enfAux.setTemperatura(temperatura);
                    enfAux.setProblemasGastrointestinais(probGastrointestinais);
                    
                    hospital.addPessoa(enfAux);
                    
                } else { // é paciente
                    Pessoa paciente = new Pessoa(anoNascimento);
                    
                    paciente.setID(id);
                    paciente.setNumGlobulosBrancos(numGlobBrancos);
                    paciente.setTemperatura(temperatura);
                    paciente.setProblemasGastrointestinais(probGastrointestinais);
                    
                    hospital.addPessoa(paciente);
                }
                
                linha = br.readLine(); // passar à próxima linha
            }
            
            br.readLine(); // skip à linha Pacientes esperando curativo:
            linha = br.readLine();
            while (!linha.equals("|")) { // para todos os pacientes em lista de espera, adicioná-los lá
                String[] partes = linha.split(" "); // separa a linha
                
                int id = Integer.parseInt(partes[0]);
                String doenca = partes[1];
                
                Enfermeiro.addPacienteEsperandoCurativo(id, doenca);
                
                linha = br.readLine(); // passa à próxima linha
            }
            
            br.readLine(); // skip à linha Relatórios
            linha = br.readLine(); // passa à próxima linha (se existirem relatórios, será " ")
            
            while (!linha.equals("|")) { // para todos os relatórios
                String relatorio = "";
                for (int i = 0; i < 11; i++) { // cada relatório ocupa 11 linhas
                    relatorio += br.readLine()+"\n";
                }
                hospital.addRelatorioHospitalar(relatorio.trim()); // adiciona o relatório ao hospital
                
                linha = br.readLine(); // passa à próxima linha
                
            }
            
        }
        catch (IOException|PersonAlreadyInListException|NumberFormatException e){
            System.out.println("Erro: "+e);
        }
    }
    
    /**
     * Função para criação do ficheiro que poderá ser carregado posteriormente. Não deverá ser acessível ao utilizador.
     */
    private static void criarFicheiro() { // WIP
        try {
            // deverá criar o ficheiro hospital.txt com a info toda do hospital para poder se depois carregado no menu inicial usando um outro método.
            String ficheiro = "hospital.txt";
            FileWriter fw = new FileWriter(ficheiro);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Pessoas no hospital");
            bw.newLine();
            for (Pessoa p : hospital.getPessoas().values()) {
                String classe = p.getClass().getSimpleName();
                bw.write(classe+" "+p.getID()+" "+p.getAnoNascimento()+" "+p.getNumGlobulosBrancos()+" "+p.getTemperatura()+" "+p.temProblemasGastrointestinais());
                
                if (classe.equals("Medico")) {
                    Medico m = (Medico)p;
                    bw.write(" "+m.getNome());
                }
                
                if (classe.equals("EnfermeiroEspecialista")) {
                    EnfermeiroEspecialista ee = (EnfermeiroEspecialista)p;
                    bw.write(" "+ee.getNome()+" "+ee.getAnosCarreira()+" "+ee.isChefe());
                }
                
                if (classe.equals("EnfermeiroAuxiliar")) {
                    EnfermeiroAuxiliar ea = (EnfermeiroAuxiliar)p;
                    bw.write(" "+ea.getNome()+" "+ea.getAnosCarreira());
                }
                
                bw.newLine();
            }
            
            bw.write("|");
            bw.newLine();
            
            bw.write("Pacientes esperando curativo:");
            bw.newLine();
            for (int idCurativo: Enfermeiro.getPacientesEsperandoCurativo().keySet()) {
                bw.write(idCurativo+" "+Enfermeiro.getPacientesEsperandoCurativo().get(idCurativo)+" ");
                bw.newLine();
            }
            
            bw.write("|");
            bw.newLine();
            
            bw.write("Relatórios");
            bw.newLine();
            for (int i = 1; i <= hospital.getNumRelatorios(); i++){
                bw.newLine();
                bw.write(hospital.getRelatorioHospitalar(i-1));
                bw.newLine();
            }
            
            bw.write("|");
            
            bw.close();
        }
        catch (Exception e){
            System.out.println("Erro: "+e);
        }
    }
    
    /**
     * Menu inicial. Trata da escolha dos sub-menus.
     */
    private static void menuInicial(){
        Scanner scan = new Scanner (System.in);
        int opcao;
        
        do {
            System.out.println("-------------- [ Menu Inicial ] -------------");
            System.out.println("---------------------------------------------");
            System.out.println(" 1. Menu Médico");
            System.out.println(" 2. Menu Enfermeiro");
            System.out.println(" 3. Menu Administrador");
            System.out.println(" 4. Carregar o Hospital de hospital.txt");
            System.out.println(" 5. Fechar programa");
            System.out.println("---------------------------------------------");
            System.out.println(" Selecione o menu que deseja aceder: ");
            opcao = scan.nextInt();
            
            switch (opcao){
                case 1:
                    menuMedico();
                    break;
                case 2:
                    menuEnfermeiro();
                    break;
                case 3:
                    menuAdmin();
                    break;
                case 4:
                    System.out.println("Carregando hospital de hospital.txt...");
                    lerFicheiro();
                    System.out.println("Hospital carregado!");
                    break;
                case 5:
                    System.out.println("Terminando execução do programa. Adeus!");
                    break;
                default:
                    System.out.println("Opcao inválida! Tente denovo.");
            }
        } while (opcao != 5);
    }
    
    /** -------------------------------------------------------------
     * Menu do Médico.
     * --------------------------------------------------------------
     */ 
    private static void menuMedico(){
        Scanner scan = new Scanner (System.in);
        int opcao;
        do{
            System.out.println("-----------------Menu Médico---------------");
            System.out.println("-------------------------------------------");
            System.out.println(" 1. Listar pacientes em espera no hospital  ");
            System.out.println(" 2. Listar pacientes a aguardar alta        ");
            System.out.println(" 3. Diagnóstico ao paciente                 ");
            System.out.println(" 4. Dar alta hospitalar                     ");
            System.out.println(" 5. Requerimento de auxiliares              ");
            System.out.println(" 6. Sair                                    ");
            System.out.println("--------------------------------------------");
            System.out.println(" Selecione uma opção: ");
            
            opcao = scan.nextInt();
            
            switch (opcao) {
                case 1:
                    imprimeListaEspera();
                    break;
                case 2:
                    try {
                        System.out.println("Por favor insira o ID do médico cujos pacientes deseja ver: ");
                        int idMed = scan.nextInt();
                        if (hospital.getMedicos().contains(idMed)) {
                            Medico m = (Medico)hospital.getPessoa(idMed);

                            System.out.println("- Pacientes do médico "+idMed+" -");
                            for (Pessoa paciente : m.getPacientes().values()){
                                System.out.println(paciente);
                            }
                        }
                        else {
                            System.out.println("O ID inserido não corresponde ao ID de um médico. Voltando ao menu anterior...");
                        }
                    }
                    catch (PersonNotInListException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 3:
                    fazDiagnostico();
                    break;
                case 4:
                    darAlta();
                    break;
                case 5:
                    System.out.println("WIP - Opção Requerimento de auxiliares Selecionado");
                    break;
                case 6:
                    System.out.println("Voltando ao menu anterior!");
                    break;
                default:
                    System.out.println("Opção Inválida !");
            }
        } while (opcao != 6);
    }
    
    /**
     * Imprime os pacientes em lista de espera no hospital.
     */
    private static void imprimeListaEspera(){
        try {
            System.out.println("- Lista de Espera -");
            for (int i : hospital.getListaEspera()){
                System.out.println(hospital.getPessoa(i));
            }
        }
        catch (PersonNotInListException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    /**
     * Trata da lógica do diagnostico.
     */
    private static void fazDiagnostico(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Por favor insira o ID do médico que irá efetuar o diagnóstico: ");
            int idMed = scan.nextInt();
            if (hospital.getMedicos().contains(idMed)) { // se o ID for de um médico
                Medico m = (Medico)hospital.getPessoa(idMed);

                System.out.println("Por favor insira o ID do paciente em lista de espera:");
                int idPaciente = scan.nextInt();
                
                Pessoa paciente = hospital.getPessoa(idPaciente);

                if (!m.temPaciente(paciente)) { // se não tem este paciente
                    hospital.associarPacienteAMedico(paciente, m); // adiciona o paciente ao médico
                }

               
                int numDoentesAntes = Enfermeiro.getPacientesEsperandoCurativo().size();
                
                if (m.diagnostica(paciente)){ // se o paciente estiver doente
                    System.out.println("Paciente "+paciente.getID()+" doente. Adicionado à lista de espera por curativo.");
                }
                else { // se não estiver doente
                    System.out.println("Paciente "+paciente.getID()+" não está doente. Só falta dar alta para ir para casa.");
                    // m.removePaciente(idPaciente); // remove o paciente do médico, será feito quando se dá alta
                    // hospital.removerPessoa(paciente); // manda o paciente para casa (retira-o do hospital), será feito quando se dá alta
                }
                
                // para propósitos de histórico do hospital
                if (numDoentesAntes < Enfermeiro.getPacientesEsperandoCurativo().size()){ // deteta se houve um novo doente
                    String doenca = Enfermeiro.getPacientesEsperandoCurativo().get(paciente.getID()); // busca a doenca deste novo doente
                    switch (doenca) {
                        case "COVID-19": // se tiver covid
                            hospital.addCountCOVID();
                            break;
                        case "HIV": // se tiver HIV
                            hospital.addCountHIV();
                            break;
                        case "Ébola": // se tiver ébola
                            hospital.addCountEbola();
                            break;
                    }
                }
                hospital.addCountTestes();
            }
            else {
                System.out.println("O ID inserido não corresponde ao ID de um médico. Voltando ao menu anterior...");
            }
        }
        catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }
    
    /**
     * Trata da lógica da alta hospitalar.
     */
    private static void darAlta(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Por favor insira o ID do médico que irá dar alta: ");
            int idMed = scan.nextInt();

            if (hospital.getMedicos().contains(idMed)) {
                Medico m = (Medico)hospital.getPessoa(idMed);
                
                System.out.println("Por favor insira o ID do paciente a aguardar alta: ");
                int idPac = scan.nextInt();
                
                // tentar dar alta, que irá chamar o diagnóstico outravez
                if (m.darAlta(hospital.getPessoa(idPac))){ // dá alta
                    System.out.println("O paciente teve alta.");
                    hospital.removerPessoa(hospital.getPessoa(idPac)); // remove a pessoa do hospital
                }
                else { // não deu alta; está doente ainda
                    System.out.println("O paciente não está em condições de ter alta.");
                }
            }
            else {
                System.out.println("O ID inserido não corresponde ao ID de um médico. Voltando ao menu anterior...");
            }
        }
        catch (Exception e) {
            System.out.println("Erro "+e);
        }
    }
    
    
    /** -------------------------------------------------------------
     * Menu do Enfermeiro.
     * --------------------------------------------------------------
     */
    private static void menuEnfermeiro(){
        Scanner scan = new Scanner (System.in);
        int opcao;
        do{
            System.out.println("-----------------Menu Enfermeiro--------------- ");
            System.out.println("----------------------------------------------- ");
            System.out.println(" 1. Listar enfermeiros de médico                  ");
            System.out.println(" 2. Listar pacientes a aguardar curativo          ");
            System.out.println(" 3. Atribuir enfermeiro-especialista a médico     ");
            System.out.println(" 4. Aplicar curativo a paciente                   ");
            System.out.println(" 5. Sair                                          ");
            System.out.println("-----------------------------------------------   ");
            System.out.println(" Selecione uma opção: ");
            
            opcao = scan.nextInt();
            switch (opcao) {
                case 1:
                    listarEnfermeirosdeMedico();
                    break;
                case 2:
                    listarPacEsperandoCurativo();
                    break;    
                case 3:
                    associarEnfAMed();
                    break;    
                case 4:
                    aplicarCurativo();
                    break;    
                case 5:
                    System.out.println("Voltando ao menu anterior!");
                    break;
                default:
                    System.out.println("Opção Inválida !");
            }
        } while (opcao != 5);
    }
    
    /**
     * Lista os pacientes esperando por curativo de uma forma clara e legível.
     */
    private static void listarPacEsperandoCurativo(){
        try {
            System.out.println("- Pacientes esperando curativo -");
            for (int id : Enfermeiro.getPacientesEsperandoCurativo().keySet()){
                System.out.println(hospital.getPessoa(id) + ", " + Enfermeiro.getPacientesEsperandoCurativo().get(id));
            }
        }
        catch (PersonNotInListException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    /**
     * Trata de listar enfermeiros de um médico um por um num formato fácilmente legível.
     */
    private static void listarEnfermeirosdeMedico(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Por favor insira o ID do médico cujos enfermeiros deseja ver: ");
            int id = scan.nextInt();
            if (hospital.getMedicos().contains(id)) {
                Medico m = (Medico)hospital.getPessoa(id);

                System.out.println("- Enfermeiros do médico "+id+" -");
                for (Enfermeiro enfermeiro : m.getEnfermeiros().values()){
                    System.out.println(enfermeiro);
                }
            }
            else {
                System.out.println("O ID inserido não corresponde ao ID de um médico. Voltando ao menu anterior...");
            }
        }
        catch (PersonNotInListException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    /**
     * Trata da lógica da aplicação de um curativo
     */
    private static void aplicarCurativo(){
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("Insira o ID do enfermeiro que irá aplicar o curativo: ");
            int idEnf = scan.nextInt();
            if (hospital.getEnfermeiros().contains(idEnf)){
                Enfermeiro enf = (Enfermeiro)hospital.getPessoa(idEnf); // guarda o enfermeiro na variavel enf

                System.out.println("Insira o ID do paciente a quem o curativo será aplicado: ");
                int idPac = scan.nextInt();
                if (Enfermeiro.getPacientesEsperandoCurativo().containsKey(idPac)) { // se este paciente estiver à espera de curativo
                    Pessoa paciente = hospital.getPessoa(idPac);
                    boolean curativoCurou = enf.aplicarCurativo(idPac);
                    if (curativoCurou){ // se o curativo, após aplicado, calhar na chance de curar, entra aqui
                        // cura-se o paciente, ao passar os seus atributos internos para valores saudáveis
                        paciente.setTemperatura(37.5);
                        paciente.setNumGlobulosBrancos(5000);
                        paciente.setProblemasGastrointestinais(false);
                    }
                    
                    paciente.setContadorCurativos(paciente.getContadorCurativos()+1); // aumenta o número de curativos aplicados por 1
                    
                    Enfermeiro.removePacienteEsperandoCurativo(idPac); // remove da lista de espera de curativos, só será readicionado após o médico diagnositcar outravez ou dar alta
                    System.out.println("Curativo aplicado.");
                    
                    if (paciente.getContadorCurativos() >= 2 && !curativoCurou){ // se o paciente não foi curado após a aplicação de 2 curativos, morre
                        System.out.println("Infelizmente, o paciente não resistiu à doença, e morreu.");
                        hospital.addCountMortes();
                        hospital.removerPessoa(paciente);
                    }
                }
                else {
                    throw new PersonNotInListException("Paciente não está presente na lista de espera pelos curativos.");
                }
            }
            else {
                System.out.println("O ID inserido não corresponde ao ID de um enfermeiro. Voltando ao menu anterior...");
            }
        }
        catch (PersonNotInListException e){
            System.out.println("Erro: "+e);
        }
    }
    
    /**
     * Associa um enfermeiro a um médico.
     */
    private static void associarEnfAMed(){
        Scanner scan = new Scanner(System.in);
        int idEnf, idMed;      
        try {
            System.out.println("Insira o ID do enfermeiro-especialista: ");
            idEnf = scan.nextInt();
            Pessoa enf = hospital.getPessoa(idEnf);
            if (enf.getClass() != EnfermeiroEspecialista.class) {
                System.out.println("O ID inserido não é de um enfermeiro-especialista. Voltando ao menu anterior...");
            }
            else {
                System.out.println("Insira o ID do médico a qual o associar: ");
                idMed = scan.nextInt();
                Pessoa med = hospital.getPessoa(idMed);
                if (med.getClass() != Medico.class) {
                    System.out.println("O ID inserido não é de um médico. Voltando ao menu anterior...");
                }
                else {
                    hospital.associarEnfermeiroAMedico((Enfermeiro)enf, (Medico)med);
                    System.out.println("O enfermeiro " +enf.getID()+ " foi associado ao médico " +med.getID()+ ".");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    /** --------------------------------------------------------------
     * Menu do Administrador.
     * --------------------------------------------------------------
     */
    private static void menuAdmin(){
        Scanner scan = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("------------------------Menu Administrador----------------------");
            System.out.println("----------------------------------------------------------------");
            System.out.println("1. Criar médico                                                   ");
            System.out.println("2. Criar enfermeiro-especialista                                  ");
            System.out.println("3. Criar enfermeiro-auxiliar                                      ");
            System.out.println("4. Criar novo paciente                                            ");
            System.out.println("5. Promover enfermeiro a chefe                                    ");
            System.out.println("6. Aumentar anos de carreira de todos os enfermeiros              ");
            System.out.println("7. Listar enfermeiros                                             ");
            System.out.println("8. Listar médicos                                                 ");
            System.out.println("9. Listar pedidos para enfermeiros-auxiliares                     ");
            System.out.println("10. Listar pacientes em espera no hospital                        ");
            System.out.println("11. Atirar pedidos para enfermeiros-auxiliares para a trituradora ");
            System.out.println("12. Atende ao pedido para enfermeiros-auxiliares                  ");
            System.out.println("13. Virus outbreak                                                ");
            System.out.println("14. Criar relatório hospitalar                                    ");
            System.out.println("15. Aceder a histórico de relatórios hospitalares                 ");
            System.out.println("16. Sair                                                          ");
            System.out.println("----------------------------------------------------------------");
            System.out.println(" Selecione uma opção: ");
            
            opcao = scan.nextInt();

            switch(opcao) {
                case 1:
                    criarMedico();
                    break;
                case 2:
                    criarEnfermeiroEsp();
                    break;
                case 3:
                    criarEnfermeiroAux();
                    break; 
                case 4:
                    criarPaciente();
                    break;
                case 5:
                    promoverEsp();
                    break;
                case 6:
                    System.out.println("Atualizando os anos de carreira dos enfermeiros...");
                    hospital.atualizarAnosCarreiraEnf();
                    break;
                case 7:
                    try {
                        System.out.println("- Enfermeiros -");
                        for (int i : hospital.getEnfermeiros()){
                            System.out.println(hospital.getPessoa(i));
                        }
                    }
                    catch (PersonNotInListException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        System.out.println("- Médicos -");
                        for (int i : hospital.getMedicos()){
                            System.out.println(hospital.getPessoa(i));
                        }
                    }
                    catch (PersonNotInListException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 9:
                    System.out.println("<Não implementado>");
                    break;
                case 10:
                    try {
                        System.out.println("- Lista de Espera -");
                        for (int i : hospital.getListaEspera()){
                            System.out.println(hospital.getPessoa(i));
                        }
                    }
                    catch (PersonNotInListException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 11:
                    System.out.println("Opção Atirar pedidos para enfermeiros-auxiliares para a trituradora Selecionado");
                    System.out.println(">>> WIP <<<");
                    break;
                case 12:
                    System.out.println("Opção Atende ao pedido para enfermeiros-auxiliares Selecionado");
                    System.out.println(">>> WIP <<<");
                    break;
                case 13:
                    try {
                        System.out.println("Provocando um \"Virus outbreak\"!! ...");
                        hospital.virusOutbreak();
                        System.out.println("Virus outbreak completo.");
                    }
                    catch (PersonNotInListException e){
                        System.out.println("Erro: "+e);
                    }
                    break;
                case 14:
                    System.out.println("Gerando um novo relatório hospitalar...");
                    try {
                        String novoRelatorio = hospital.getNovoRelatorioHospitalar(); // cria o novo relatório
                        System.out.println(novoRelatorio); // escreve-o na consola
                        hospital.addRelatorioHospitalar(novoRelatorio); // guarda-o no histórico
                    }
                    catch (ArithmeticException e){
                        System.out.println("Erro: Não existem nenhuns pacientes na nossa base de dados!");
                    }
                    break;
                case 15:
                    acederRelatorioHospitalar();
                    break;
                case 16:
                    System.out.println("Voltando ao menu anterior!");
                    break;
                default:
                    System.out.println("Opção Inválida !");
            }
        } while (opcao != 16);
    }
    
    /**
     * Cria um novo médico
     */
    private static void criarMedico(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Por favor insira um nome para o médico:");
        String nome = scan.next();
        System.out.println("Por favor insira o ano de nascimento do médico:");
        int ano = scan.nextInt();
        
        try {
            Medico med = new Medico(nome, ano);
            hospital.addPessoa(med);
            System.out.println("Medico adicionado: " + med);
        } catch (PersonAlreadyInListException ex) {
            System.out.println("Erro: O médico criado já está no hospital!");
        }
    }
    
    /**
     * Cria um novo enfermeiro-especialista
     */
    private static void criarEnfermeiroEsp(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Por favor insira um nome para o enfermeiro-especialista:");
        String nome = scan.next();
        System.out.println("Por favor insira quantos anos de carreira este enfermeiro tem: ");
        int anosCarreira = scan.nextInt();
        System.out.println("Por favor insira o ano de nascimento do enfermeiro:");
        int ano = scan.nextInt();
        
        try {
            EnfermeiroEspecialista enf = new EnfermeiroEspecialista(nome, anosCarreira, ano);
            hospital.addPessoa(enf);
            System.out.println("Enfermeiro-especialista adicionado: " + enf);
        } catch (PersonAlreadyInListException ex) {
            System.out.println("Erro: O enfermeiro especialista criado já está no hospital!");
        }
    }
    
    /**
     * Cria um novo enfermeiro-auxiliar
     */
    private static void criarEnfermeiroAux(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Por favor insira um nome para o enfermeiro-auxiliar:");
        String nome = scan.next();
        System.out.println("Por favor insira quantos anos de carreira este enfermeiro tem: ");
        int anosCarreira = scan.nextInt();
        System.out.println("Por favor insira o ano de nascimento do enfermeiro:");
        int ano = scan.nextInt();
        
        try {
            EnfermeiroAuxiliar enf = new EnfermeiroAuxiliar(nome, anosCarreira, ano);
            hospital.addPessoa(enf);
            System.out.println("Enfermeiro-auxiliar adicionado: " + enf);
        } 
        catch (PersonAlreadyInListException ex) {
            System.out.println("Erro: O enfermeiro auxiliar criado já está no hospital!");
        }
    }
    
    /**
     * Cria um novo paciente
     */
    private static void criarPaciente(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Por favor insira a data de nascimento da pessoa: ");
        int data = scan.nextInt();
        
        try {
            Pessoa p = new Pessoa(data, false); // false para usar o construtor que permite a pessoa entrar no hospital com chance de estar infetada
            hospital.addPessoa(p);
            System.out.println("Pessoa adicionada: " + p);
        }
        catch (PersonAlreadyInListException e) {
            System.out.println("Erro: "+e.getMessage());
        }
    }
    
    /**
     * Promove um especialista para chefe.
     */
    private static void promoverEsp(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Por favor insira o ID do enfermeiro especialista: ");
            int idEsp = scan.nextInt();
            if (hospital.getEnfermeirosEsp().contains(idEsp)){
                EnfermeiroEspecialista especialista = (EnfermeiroEspecialista)hospital.getPessoa(idEsp);
                hospital.promoverEnfermeiroEsp(especialista);
                System.out.println("Especialista "+especialista.getID()+" promovido a chefe.");
            }
            else {
                System.out.println("O ID inserido não é de um enfermeiro-especialista. Voltando ao menu anterior...");
            }
        }
        catch (Exception e){
            System.out.println("Erro: "+e);
        }
    }
    
    /**
     * Acede à base de dados dos relatórios hospitalares do hospital, para escolher um relatório a visualizar
     */
    private static void acederRelatorioHospitalar(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Acedendo ao histórico de relatórios hospitalares...");
        int numRelatorios = hospital.getNumRelatorios(); // número total de relatórios
        if (numRelatorios > 0) {
            for (int i = 1; i < numRelatorios+1; i++){ // imprime uma lista numerada de todos os relatórios no histórico hospital
                System.out.println("|| Relatório #"+i);
            }

            System.out.println("Qual o relatório a que deseja aceder?");
            int opcaoRelatorio = scan.nextInt();

            if (opcaoRelatorio <= numRelatorios && opcaoRelatorio > 0) { // para evitar index out of bounds exception
                System.out.println(hospital.getRelatorioHospitalar(opcaoRelatorio-1));
            }
            else {
                System.out.println("O relatório inserido não existe.");
            }
        }
        else  {
            System.out.println("Não existem nenhuns relatórios no histórico. Por favor crie um relatório primeiro.");
        }
    }
}
