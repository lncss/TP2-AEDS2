import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


//atributos da class Filme
class Filme{
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //utilizado para converter string to date
    private String name;
    private String title;
    private Date realeseDate;
    private Integer duration;
    private String genre;
    private String originalIdiom;
    private String situation;
    private float budget; // (orçamento)
    private ArrayList<String> keyWord;


    //construtor primario (valor inicial)
    public Filme(){
        name = "";
        title = "";
        realeseDate = null;
        duration = 0;
        genre = "";
        originalIdiom = "";
        situation = "";
        budget = 0;
        keyWord = null;
    }

    //construtor secundario
    public Filme(String name, String title, Date realeseDate, Integer duration, String genre, String originalIdiom, String situation, float budget,ArrayList<String> keyWord){
        this.name = name;
        this.title = title;
        this.realeseDate = realeseDate;
        this.duration = duration;
        this.genre = genre;
        this.originalIdiom = originalIdiom;
        this.situation = situation;
        this.budget = budget;
        this.keyWord = keyWord; 
    }

    //metodos para setar(set) e retornar(get) cada atributo de filme
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setRealeseDate(Date realeseDate){
        this.realeseDate = realeseDate;
    }

    public Date getRealeseDate(){ 
        return realeseDate;
    }

    public void setDuration(Integer duration){
        this.duration = duration;
    }

    public Integer getDuration(){
        return duration;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getGenre(){
        return genre;
    }

    public void setOriginalIdiom(String originalIdiom){
        this.originalIdiom = originalIdiom;
    }

    public String getOriginalIdiom(){
        return originalIdiom;
    }

    public void setSituation(String situation){
        this.situation = situation;
    }

    public String getSituation(){
        return situation;
    }

    public void setBudget(float budget){
        this.budget = budget;
    }

    public float getBudget(){
        return budget;
    }

    public void setKeyWord(ArrayList<String> keyWord){
        this.keyWord = keyWord;
    }

    public ArrayList<String> getKeyWord(){
        return keyWord;
    }

    //clonar a classe
    public Filme clone(){
        Filme film = new Filme();
        film.name = this.name;
        film.title = this.title;
        film.duration = this.duration;
        film.realeseDate = this.realeseDate;
        film.genre = this.genre;
        film.originalIdiom = this.originalIdiom;
        film.situation = this.situation;
        film.budget = this.budget;
        film.keyWord = this.keyWord;
        return film;
    }

    //imprimir a classe
    public void imprimirClass(){
        System.out.println(getName() + " " + getTitle() + " " + sdf.format(getRealeseDate()) + " " + getDuration() + " " + getGenre() + " " + getOriginalIdiom() + " " +
        getSituation() + " " + getBudget() + " " + getKeyWord());
        //System.out.println(getTitle());
    }


    //remover as tags do html
    public static String removeTag(String line){
        String resp = "";
        for(int i = 0; i < line.length(); i++){ //o valor de i deve ser menor que o tamanho da linha
            if(line.charAt(i) == '<'){ // se na posição i houver o sinal de abrir tag ele passa para a proxima posição
                while(line.charAt(i) != '>'){
                    i++;
                }
            }else if(line.charAt(i) == '&'){
                while(line.charAt(i) != ';') i++;
            }else{
                resp += line.charAt(i); // atribui a resp a frase/palavra sem as tags
            }
        }     
        //System.out.println(resp);  
        return resp;
    }

    // identificar o fim do pub.in
    public static boolean isFim(String s){
        return (s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M'); 
    }

    //passar numero para inteiro 
    public int justInt(String line){
        String resp = "";
        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) >= '0' && line.charAt(i) <= '9'){ 
                resp += line.charAt(i);
            } else { 
                i = line.length();
            }
        }
        return Integer.parseInt(resp); //transforma a string em  inteiro
    }

    //converter string to float
    public Float isFloat(String line){
        String resp = "";
        float r  =  0;
        for(int i = 0; i < line.length(); i++){
            if((line.charAt(i) >= '0' && line.charAt(i) <= '9') || line.charAt(i) == '.'){ //procura por numeros na string
                resp += line.charAt(i); //armazena os numero em uma string resposta
            }else if(line.charAt(i) == '-'){
                resp = "0";
            }
        }
        r = Float.parseFloat(resp); //passa string para float e retorna
        return r;
    }

    //tratar name
    public String tratarName(String line){
        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == '_'){ //remove os underlines presentes na string
                line = line.replaceAll("_", "");
            }else{
                i = line.length();
            }
        }
        
        return line;
    }

    //tratar Situation
    public String tratatSituation(String line){
        line = line.replaceAll("Situação","");
        return line.trim();
    }

    //tratar idioma
    public String tratarIdioma(String line){
        line = line.replaceAll("Idioma original", "");
        return line.trim();
    }

    //ler name
    public void lerName(String film)throws ParseException{
        String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));

            line = br.readLine();

            //setar o nome da serie
            while(!line.contains("h2 class")){ //enquanto não encontrar h2 class o arquivo continua sendo lido
               line = br.readLine();
            }
            line = br.readLine().trim(); //tira os espaços em branco da linha
            line = removeTag(line); //remove as tags e deixa só o nome da serie
            setName(tratarName(line));

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }

    }

    //ler release date
    public void lerDate(String film)throws ParseException{
        String line;

        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));

            //setar releaseDate
            line = br.readLine();
            while(!line.contains("class=\"release\"")){//enquanto não encontrar a class release o arquivo continua sendo lido
                line = br.readLine();
            }   
            line = br.readLine().trim(); //remover espaços em branco
            setRealeseDate(sdf.parse(line));
            //System.out.println(line.split(" ")[0]);

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    //ler genero
    public void lerGenre(String film)throws ParseException{
        String line;

        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));

            //setar genre
            line = br.readLine();
            while(!line.contains("class=\"genres\"")){//enquanto não encontrar a classe genre o arquivo continua sendo lido
                line = br.readLine();
            }
            line = br.readLine();
            line = br.readLine().trim();//remover espaços em branco
            setGenre(removeTag(line));
            //System.out.println(removeTag(line));

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    public void lerDuration(String film)throws ParseException{
        String line;

        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));

            //setar duration
            line = br.readLine();
            while(!line.contains("class=\"runtime\"")){//enquanto não encontrar a classe genre o arquivo continua sendo lido
                line = br.readLine();
            }
            line = br.readLine();
            line = br.readLine().trim();
            line = removeTag(line);
            String time = line;
            String horas = "";
            String min = "";

            if(time.length() > 3){
                horas = time.substring(0,1); //armazenar numero de horas em uma string
                int h = Integer.parseInt(horas); //passar string horas para int
                min = time.substring(3,5); //armazenar numero de min em uma string
                int m = justInt(min); //passar string min para int
                setDuration((h * 60) + m); // calacular o valor de horas
            }else{ // caso o filme tenha só min tratar somente min
                min = time.substring(0,2);
                int m = justInt(min); 
                setDuration(m);
            }

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    public void lerTitle(String film)throws ParseException{
        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));
            String line = br.readLine() ;

            //setar title
            String titulo = null;
            while(line != null){
                if(line.contains("Título original")){ // ler linha até Título original
                    titulo = removeTag(line.replace("Título original", "").trim()); 
                    break;
                }
                line = br.readLine();
            }

            if(titulo == null){ //se o titulo for null, retorna o nome do filme
                this.title = name;
            }else{
                this.title = titulo.substring(1);
            }
            //System.out.println(removeTag(line));

            //fechar arquivo
            br.close();
            //tratar exceções 
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        }catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    public void lerSituation(String film)throws ParseException{
        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));
            String line = br.readLine() ;

           //setar situation
           while(!(line = br.readLine()).contains("Situação</bdi>")); //enquanto não encontrar a tag situação o arquivo continua sendo lido
           line = tratatSituation(line);
           setSituation(removeTag(line));
           //System.out.println(removeTag(line));

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        }catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    public void lerIdiom(String film)throws ParseException{
        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));
            String line = br.readLine() ;

           //setar original idiom
           while(!(line = br.readLine()).contains("Idioma original")); //enquanto não encontrar a string Idioma original o arquivo continua sendo lido
           line = tratarIdioma(line);
           setOriginalIdiom(removeTag(line));
           //System.out.println(removeTag(line));

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    public void lerBudget(String film)throws ParseException{
        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));
            String line = br.readLine() ;

            //setar budget
            //line = br.readLine();
            while(!(line = br.readLine()).contains("Orçamento</bdi>")); //enquanto não encontrar a tag Orçamento o arquivo continua sendo lido
            //line = br.readLine().trim();
            line = removeTag(line); 
            line = line.replaceAll("Orçamento","");
            line = line.replace("$","");  //remove o cifrão de orçamento
                        
            setBudget(isFloat(line));
            //System.out.println(isFloat(line));

            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }

    public void lerKey(String film)throws ParseException{
        try{
            BufferedReader br = new BufferedReader(new FileReader("/tmp/filmes/" + film));

            //setar keywords
            this.keyWord = new ArrayList<String>();

            String line = br.readLine();
            while(!line.contains("Palavras-chave")) //ler linha até Palavras-chave
                line = br.readLine();

            line = br.readLine();
            while(!line.contains("</ul>")) { //ler linha até fechar a tag ul
                line = br.readLine();
                if(line.contains("/keyword/")){ 
                    line = removeTag(line).trim();//remover espaços em branco
                    this.keyWord.add(line);
                }
            }


            //fechar arquivo
            br.close();
            //tratar exceções
        }catch(FileNotFoundException e) {
            System.out.println("Unable to open file '" + film + "'");                
        } catch(IOException e) {
            System.out.println("Error reading file '" + film + "'");
        }
    }
    
    //ler os atributos de um html
    public void ler(String film)throws ParseException{
        //chamar os metodos de leitura
        lerName(film);
        lerDate(film);
        lerGenre(film);
        lerDuration(film);
        lerTitle(film);
        lerSituation(film);
        lerIdiom(film);
        lerBudget(film);
        lerKey(film);

    }


    //abrir arquivo pela main
    public static void main(String args[])throws Exception{
        MyIO.setCharset("utf-8");
        String[] entrada = new String[2000];
        int count = 0;

        String line = MyIO.readLine();
        while(isFim(line) == false){
            entrada[count++] = line;
            line = MyIO.readLine();
        }
        
        Filme film;

        for(int i = 0; i < count; i++){
            film = new Filme();
            film.ler(entrada[i]);
            film.imprimirClass();
        }       
        
    }
}

class Lista {
    private Filme[] array;
    private int n = 0;

    public Lista(int tamanho) {
        array = new Filme[tamanho];
        n = 0;
    }

    public void inserirInicio(Filme x)throws Exception{
        //valida a inserção
        if(n >= array.length){
            throw new Exception("Erro ao inserir!");
        }

        //levar elementos para o fim do array
        for(int i = n; i > 0; i--){
            array[i] = array[i - 1];
        }

        array[0] = x; // a posição inicial recebe o elemento
        n++;
    }


    public void inserirFim(Filme x)throws Exception{
        //validar a inserção
        if(n >= array.length){
            throw new Exception("Erro ao inserir!");
        }

        array[n] = x; //o array recebe o elemento na ultima posição
        n++;
    }


    public void inserir(Filme x, int pos)throws Exception{
        //validar inserção
        if( n >= array.length || pos < 0 || pos > n){
            throw new Exception("Erro ao inserir!");
        }

        //levar elementos para o fim do array
        for(int i = n; i > pos; i--){
            array[i] = array[i - 1];
        }

        array[pos] = x; // o array recebe o elemento na posição indicada
        n++;
    }


    public Filme removerInicio() throws Exception{
        //validar remoção
        if(n == 0){
            throw new Exception("Erro ao remover!");
        }

        Filme resp = array[0];
        n--;

        for(int i = 0; i < n; i++){
            array[i] = array[i + 1];
        }

        return resp;
    }


    public Filme removerFim()throws Exception{
        //validar remoção
        if(n == 0){
            throw new Exception("Erro ao remover!");
        }

        return array[--n];
    }


    public Filme remover(int pos)throws Exception{
        //validar remoção
        if(n == 0 || pos < 0 || pos >= n){
            throw new Exception("Erro ao remover!");
        }

        Filme resp = array[pos];
        n--;

        for(int i = 0; i < n; i++){
            array[i] = array[i + 1];
        }

        return resp;
    }

    // mostra os elementos da lista separados por espacos
    public void mostrar() throws Exception{
       for(int i = 0; i < n; i++){
           
           System.out.print("[" + i + "]" + " ");
           array[i].imprimirClass();
       }
    }

}

public class ListaSequencial{
    public static void main(String[] args)throws Exception{
        MyIO.setCharset("utf-8");

        Lista lista = new Lista(1000);
        String file;

        //ler a entrada ate FIM
        while (true){ 
            file = MyIO.readLine();
            if (file.equals("FIM")) {

                break;
            }

            Filme film = new Filme();
            film.ler(file);
            lista.inserirFim(film);
        }
       
        String line = "";
        String tam = ""; //armazenar a string numero que indica a qntd de remoções e inserções
        tam = MyIO.readLine();
        int n = Integer.parseInt(tam);
        
        for(int i=0; i<n; i++) { // enquanto a entrada for menor que tam, continua as inserções e remoções
            line = MyIO.readLine();
            String metodo = line.substring(0, 2); //ler as tres primeiras posicoes para saber qual metdodo usar
            String name = ""; //html do filme
            int pos = 0; //armazenar as posições em que ocorrerão as inserções e remoções


            //if para saber a partir de qual linha é lido o html do filme, e as posicoes, quando fornecidas de insercao e remocao
            //inserir em qualquer posicao
            if(metodo.compareTo("I*") == 0) {

                pos = Integer.parseInt(line.substring(3, 5)); // passar para inteiro a posição de insercao
                name = line.substring(6);

                //remover em qualquer posicao
            }else if(metodo.compareTo("R*") == 0) {

                pos = Integer.parseInt(line.substring(3));

                //inserir no inicio e remover no inicio 
            }else if(metodo.compareTo("II") == 0 || metodo.compareTo("IF") == 0){

                name = line.substring(3);
            }
    
            //if para realizar as insercoes e remocoes 
            // inserir em determinada posicao
            if(metodo.compareTo("I*") == 0){

                Filme movie = new Filme();
                movie.ler(name);
                lista.inserir(movie, pos); //insere dados do filme na pos fornecida

                //inserir no inicio
            }else if(metodo.compareTo("II") == 0){

                Filme movie = new Filme();
                movie.ler(name);
                lista.inserirInicio(movie);

                //inserir no fim
            }else if(metodo.compareTo("IF") == 0){

                Filme movie = new Filme();
                movie.ler(name);
                lista.inserirFim(movie);


                //remover em uma posicao x
            }else if(metodo.compareTo("R*") == 0){

                MyIO.println("(R)" + " " + lista.remover(pos).getName());//retorna o nome do filme removido em uma posição x

                //remover no inicio 
            }else if(metodo.compareTo("RI") == 0){

                MyIO.println("(R)" + " " + lista.removerInicio().getName()); //retorna o nome do filme removido da primeira posicao

                //remover no fim
            }else if(metodo.compareTo("RF") == 0){

                MyIO.println("(R)" + " " + lista.removerFim().getName()); //retorna o nome do filme removido da ultima posição
            }
            
        }
        
        lista.mostrar(); //mostrar filmes que foram removidos e os filmes que permaneceram no array
    }
}