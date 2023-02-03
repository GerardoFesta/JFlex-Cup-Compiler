#include <stdio.h> 
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
typedef enum { false, true } bool;
char * str_concat(char* str1 , char* str2) {   char *buffer = malloc(sizeof(char) * 9999999);   *buffer = '\0';   strcat(buffer , str1);   strcat(buffer , str2);   return buffer; }

char * castintToString(int num) {   char *buffer = malloc(sizeof(char) * 9999999);   *buffer = '\0';   sprintf(buffer , "%d" ,num);   return buffer; }

char * castfloatToString(float num) {   char *buffer = malloc(sizeof(char) * 1000000);   *buffer = '\0';   sprintf(buffer , "%f" ,num);   return buffer; }

char * castcharToString(char num) {   char *buffer = malloc(sizeof(char) * 1000000);   *buffer = '\0';   sprintf(buffer , "%c" ,num);   return buffer; }

char * castboolToString(int num) {   char *buffer = "true";   if(num == 0) { buffer = "false"; }   return buffer; }

int castStringToint(char* num) { int ritorno; sscanf (num,"%d",&ritorno);   return ritorno; }

float castStringTofloat(char* num) { float ritorno; sscanf (num,"%f",&ritorno);   return ritorno; }

char castStringTochar(char* num) { char ritorno; sscanf (num,"%c",&ritorno);   return ritorno; }

bool castStringTobool(bool* b, char*str) { if(strcmp(str,"1")==0 || strcmp(str,"true")==0){   *b = true;   return 1;}if(strcmp(str,"0")==0 || strcmp(str,"false")==0){   *b = false;   return 0;}return -1;}
char* caststringTostring(char* num) { return num; }

char * leggiStringa() { char *buffer = malloc(sizeof(char) * 1000); scanf("%s" ,buffer);   return buffer; }
bool leggibool(bool* b){char* str = leggiStringa();if(strcmp(str,"1")==0 || strcmp(str,"true")==0){   *b = true;   return 1;}if(strcmp(str,"0")==0 || strcmp(str,"false")==0){*b = false;return 0;}return -1;}

void addTwoExclamationPoints(char* *s);
void addExclamationPoints(char* *s);
void stringtester();

void addExclamationPoints(char* *s){

	*s = str_concat(*s, "!");
	addTwoExclamationPoints(&*s);
}

void addTwoExclamationPoints(char* *s){

	*s = str_concat(*s, "!!");
}

int main(int argc, char* argv[]){

	stringtester();
}


void stringtester(){

	float c = 1.9;
	int d = 2;
	char k = ' ';
	char test;
	char* str1 = str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat("La stringa ", castintToString(1)), " contiene "), castboolToString(true)), " "), castfloatToString(c)), castcharToString(k)), castcharToString('a')), "ed è costruita con str_concat"), castcharToString(k));
	char* strinput;
	printf("%s\n", str1);
	printf("%s%d%s%s%d%s\n", "La stringa ", 2, " invece, contiene ", castboolToString(false), 2, " ed è costruita con la exprlist della scrittura");
	printf("%s\n", str_concat(str_concat(str_concat(str_concat(str_concat(str_concat("La stringa ", castintToString(3)), " contiene "), castboolToString(true)), " "), castfloatToString(c)), " ed è costruita con la str_concat direttamente nella scrittura"));
	addExclamationPoints(&str1);
	printf("%s\n", "Adesso aggiungo tre punti esclamativi alla stringa 1 (sfruttando funzioni con parametro out). Il risultato è: ");
	printf("%s\n", str1);
	printf("%s", "Adesso dammi una parola in input: ");
	strinput = leggiStringa();
	addExclamationPoints(&strinput);
	printf("%s\n", str_concat("E anche la tua parola ha dei punti esclamativi: ", strinput));
	printf("%s\n", "Adesso inserisci un carattere, per prova: ");
	scanf(" %c", &test);
	printf("%c\n", test);
}