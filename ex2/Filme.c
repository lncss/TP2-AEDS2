#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>

//criar a classe filme 
typedef struct {
    char name[100];
    char title[100];
    char releaseDate[10];
    int duration;
    char genre[1000];
    char originalIdiom[100];
    char situation[100];
    float budget; //orçamento
    char keyWords[100][20];
    int numeroPalavras;
}Filme;

//clonar a classe Filme
Filme clone(Filme *film){
    Filme clone;
    strcpy(clone.name , film->name);
    strcpy(clone.title,film->title);
    strcpy(clone.releaseDate,film->releaseDate);
    clone.duration=film->duration;
    strcpy(clone.genre,film->genre);
    strcpy(clone.situation,film->situation);
    clone.budget=film->budget;

    return clone;
}

//remover espaços em branco
char* removeSpaces(char* str){ // remover espaços em branco 
    static char str1[99];
    int count = 0, j, k;
    while (str[count] == ' ') {
        count++;
    }
    for (j = count, k = 0;
        str[j] != '\0'; j++, k++) {
        str1[k] = str[j];
    }
    str1[k] = '\0';
    return str1;
}

//remover tags da linha lida do arquivo
char *removeTags(char *html, char *line) {
    char *start = line;
    int contagem = 0;
    while (*html != '\0') {
        if (*html == '<') { //se aparecer alguns caracteres de uma tag dar break e sair do if
            if ((*(html + 1) == 'p') || (*(html + 1) == 'b' && *(html + 2) == 'r') || (*(html + 1) == '/' && *(html + 2) == 'h' && *(html + 3) == '1') || 
                (*(html + 1) == '/' && *(html + 2) == 't' && *(html + 3) == 'h') || 
                (*(html + 1) == '/' && *(html + 2) == 't' && *(html + 3) == 'd')    
            ) break;
            else contagem++;
        }
        else if (*html == '>') contagem--;
        else if (contagem == 0 && *html != '"') {
            if (*html == '&') html = strchr(html, ';');
            else if (*html != '\r' && *html != '\n') *line++ = *html;
        }
        html++;
    }
    *line = '\0';
    return *start == ' ' ? start + 1 : start;
}

//substring para pegar somente a parte da string desejada
char* substring(const char *src, int m, int n){
    int len = n - m;
    char *dest = (char*)malloc(sizeof(char) * (len + 1));
    for (int i = m; i < n && (*(src + i) != '\0'); i++)
    {
        *dest = *(src + i);
        dest++;
    }
    *dest = '\0';
    return dest - len;
}

//remover caracteres de strings que são numeros
void tiraChar(char * str, char charToRemmove){
    int i, j;
    int len = strlen(str);
    for(i=0; i<len; i++)
    {
        if(str[i] == charToRemmove)
        {
            for(j=i; j<len; j++)
            {
                str[j] = str[j+1];
            }
            len--;
            i--;
        }
    }
    
}

//imprimr a classe filme
void imprimirClass(Filme *film){
    //printf("%s\n",contaPalavras);
    printf("%s",removeSpaces(film->name));//remove espaços ante de imprimir os atributos
    printf("%s",removeSpaces(film->title));
    printf(" %s",removeSpaces(film->releaseDate));
    printf("%i",film->duration);
    printf(" %s",removeSpaces(film->genre));
    printf("%s",removeSpaces(film->originalIdiom));
    printf(" %s",removeSpaces(film->situation));
    printf(" %g",film->budget);
    
    printf(" [");//ler as keyword armazenadas no array imprimindo em chaves
    for(int aux=1;aux<=film->numeroPalavras;aux++){
        strcpy(film->keyWords[aux],removeSpaces(film->keyWords[aux]));
        if(aux==1){
            printf("%s",removeSpaces(film->keyWords[aux]));
        }else{
             printf(" %s",film->keyWords[aux]);
        }

    }
    printf("]");
    printf("\n");
}

void lerClass(char *arq,Filme *newFilm){
    FILE *file;
    char *line = NULL;
    size_t tam = 0;
    ssize_t read;
    char string[500];
    char pasta[100];
    //char troca[100];
    strcpy(pasta,"/tmp/filmes/");
    strcat(pasta,arq);  

    file = fopen(pasta,"r");

    if(file == NULL){// se o arquivo estiver vazio não vai ser possível lê-lo
        exit(EXIT_FAILURE);
    }

    char *tira = strtok(string,"(");
    

    while((read=getline(&line,&tam,file))!=-1){
        
        //Search Name
        if (strstr(line,"h2 class") != NULL){//procura por h2 class e verifica se a tag não é vazia
            read = getline(&line,&tam,file);
            strcpy(string,removeTags(line,string));
            //strcpy(string,tira + 7);
            strcpy(newFilm->name,string);//name recebe valor armazenado em string
            //printf("%s\n",newFilm.nome);
        }

        //Search Title
        if (strstr(line,"Título original") != NULL){
            strcpy(string,removeSpaces(line));
            //strcpy(string,removeTags(line,string));
            strcpy(string,(strtok(string,"</p")+48));
            strcpy(string,strtok(string,"</"));
            strcpy(newFilm->title,string);
            //printf("%s\n",newFilm.tituloOriginal);
        }

        //Search release date
        if(strstr(line,"class=\"release\">") != NULL){//procura pela class release e verifica se a tag não é vazia
            char day[2];
            char month[2];
            char year[4];
            char data[100];
            read = getline(&line,&tam,file);

            strcpy(string,removeSpaces(line));//trata a linha
            strcpy(string,tira);
            
            strcpy(day,tira);
            
            strcpy(data,strcat(day,"/"));
            strcpy(month,tira);
            strcat(data,month);
            strcat(data,"/");
            strcpy(year,tira);
            strcat(data,year);
            strcpy(newFilm->releaseDate,data);
            //printf("%s\n",newFilm.dataLancamento);
        }

        //Search duration
        if(strstr(line,"<span class=\"runtime") != NULL){
            char horas[2];
            char min[3];
            int duration = 0;

            read = getline(&line,&tam,file);
            read = getline(&line,&tam,file);
            strcpy(string,removeSpaces(line));

            if(strstr(line,"h")){ //
                strcpy(horas,strtok(string,"h"));
                strcpy(min,strtok(NULL,"m"));
                strcpy(min,removeSpaces(min));
                duration=(atoi(horas)*60)+atoi(min);
                newFilm->duration=duration;
                //printf("%i\n",duration);
            }else{
                strcpy(min,strtok(string,"m"));
                strcpy(min,removeSpaces(min));
                duration = atoi(min);
                newFilm->duration = duration;
                //printf("%i\n",duration);
            }
        }

        //Search genre
        if(strstr(line,"<a href=\"/genre/") != NULL){
            removeTags(line,string);
            strcpy(string,removeSpaces(string));
            strcpy(newFilm->genre,string);
            //printf("%s\n",newFilm.genero);
            
        }

        //Search situation
        if(strstr(line,"<strong><bdi>Situação") != NULL){
            removeTags(line,string);
            strcpy(string,removeSpaces(string));
            strcpy(newFilm->situation,string+11);
            //printf("%s\n",newFilm.situacao);

        }

        //Search idiom
        if(strstr(line,"Idioma") != NULL){
            strcpy(string,removeSpaces(line));
            strcpy(string,string+47);
            strcpy(string,substring(string,0,strlen(string)-5)); //manda para substring para obter somente o idioma
            //printf("%s\n",string);
        }


        //Search budget
        if(strstr(line,"Orçamento") != NULL){ //verifica se orçamento não é vazia
            float orcamento;

            strcpy(string,removeSpaces(line));
            if(strlen(string) == 1){
                newFilm->budget=0;
                orcamento = atof(string);
                newFilm->budget=orcamento;
                //printf("%.1e\n",atof(string)*1000000);
            }else{
            strcpy(string,string+41);
            strcpy(string,substring(string,2,strlen(string)-5));
            tiraChar(string,',');
            orcamento = atof(string);
            newFilm->budget=orcamento;
            //printf("%.1e\n",atof(string)*1000000);
            }
        }

        //search keyWords
        if(strstr(line,"Palavras-chave") != NULL){
            int count = 0;
            int i = 1;
            while(true){
                if(strstr(line,"</ul>") != NULL){break;}
                //printf("%s\n",line);
                if(strstr(line,"/keyword/") != NULL){
                    count++;
                    strcpy(string,removeSpaces(line));
                    int position = 0;  
                    for(int j = 0;j <= strlen(string);j++){
                        
                        if((string[j] == '\"') && (string[j+1]=='>')){
                            position = j+2;
                        }
                    }
                    strcpy(string,substring(string,position,strlen(string)-10));
                    strcpy(newFilm->keyWords[count],removeSpaces(string));
                }
                read = getline(&line,&tam,file);
                i++;
                
            }  
            newFilm->numeroPalavras=count;
            //strcpy(newFilm->palavrasChave[0],atoi(count)); 
                
        }

        if(strlen(newFilm->title)<2){
            strcpy(newFilm->title,newFilm->name);
        }
       

    }
    fclose(file); //feacha o arquivo
    if(line){
        free(line);
    } 

}

int main(){
    char arq[100];
    Filme newFilm;
    scanf(" %[^\n]",arq);
    
    while(true){
        if(strlen(arq)==3){
            break;
        }
        lerClass(arq,&newFilm);
        imprimirClass(&newFilm);
        scanf(" %[^\n]",arq);
    }
  return 0;
}
