import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


class Lista {
    private filme[] array;
    private int n;
 
 
    public Lista (int tamanho){
       array = new filme[tamanho];
       n = 0;
    }
 
 

    public void inserirFim(filme x) throws Exception {
 
       //validar insercao
       if(n >= array.length){
          throw new Exception("Erro ao inserir!");
       }
 
       array[n] = x.clonar();
       n++;
    }
 


    public boolean pesqSeq(String vet){
        boolean resp = false;
  
        // MyIO.println("vet: "+vet);

        for(int i = 0; i < n; i++){
            
            //MyIO.println("array i : " + array[i].getNome());

           if(array[i].getNome().equals(vet)){
              resp = true;
              i = n;
           }
        }
        return resp;
     }
}





class filme{
    private String nome;
    

    /* Construtores */
    public filme () {
    }

    public filme(String nome){
        this.nome = nome;
    }

    /* gets e sets */
    public void setNome(String nome) {this.nome = nome;}
    public String getNome() {return nome;}


    /* Remove Tags */ 
    static String removeTags(String entrada){
        String resp = "";
        for(int i=0 ; i<entrada.length() ; i++){
            if(entrada.charAt(i) == '<'){
                while(entrada.charAt(i) != '>'){
                    i++;
                }
            }
            else if(entrada.charAt(i) == '&'){ 
                i++;
                while(entrada.charAt(i) != ';') i++;
                }
            else{ 
                resp += entrada.charAt(i);
            }
        }
        return resp;
    }


    /* Abrindo o arquivo  */
    public void readDados(String file){

        BufferedReader br;
        try{
            br = new BufferedReader(new FileReader("/tmp/filmes/" + file));   
            String linha = br.readLine();

            //Enquanto for diferente de null continua lendo o arquivo
            while(linha != null){


                //Achando o nome
                if(linha.contains("h2 class")){
                    linha = br.readLine().trim();
                    linha = removeTags(linha);
                    setNome(linha);
                }

            linha = br.readLine();
            }
        }
        catch(IOException e){e.printStackTrace();}
    }


    /* Criar vetor nomes */
    public void criaVetor(String file[], int i){

    }

    public filme clonar(){
        filme clone = new filme();
        clone.nome = nome;
        return clone;
    }


    public static void main (String args[]){

        MyIO.setCharset("utf-8");

        String nomeFilme = "";
        filme novoFilme = new filme();
        Lista Filme = new Lista(100);
        

        nomeFilme = MyIO.readLine();
        
        while(!nomeFilme.equals("FIM")){

            novoFilme.readDados(nomeFilme);
            try {
                Filme.inserirFim(novoFilme);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            nomeFilme = MyIO.readLine();
        }
        
        nomeFilme = "";

        while(!nomeFilme.equals("FIM")){

            nomeFilme = MyIO.readLine();

            MyIO.println();

            if(nomeFilme.equals("FIM")){
                continue;
            }
            

            MyIO.println(Filme.pesqSeq(nomeFilme) ? "SIM" : "NAO");
        }
    }
}