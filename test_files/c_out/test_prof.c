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

float sommac(int a, int d, float b, char* *size);
void stampa(char* messaggio);
void esercizio();
int c = 1;

float sommac(int a, int d, float b, char* *size){

	float result;
	result = a+b+c+d;
	if(result>100){

		char* valore = "grande";
		*size = valore;
	}else{

		char* valore = "piccola";
		*size = valore;
	}
	return result;
}

void stampa(char* messaggio){

	int a;
	int i;
	for(int x = 4; x >= 1; x--){

		printf("%s\n", "");
	}
	printf("%s\n", messaggio);
}

int main(int argc, char* argv[]){

	esercizio();
}


void esercizio(){

	int a;
	int x;
	float b;
	char* taglia;
	int ans = 0;
	float risultato;
	int c = 2;
	a = 1;
	b = 2.2;
	x = 3;
	printf("%d\n", c);
	risultato = sommac(a, x, b, &taglia);
	stampa("la somma  incrementata  è ");
	printf("%s\n", taglia);
	stampa(" ed è pari a ");
	printf("%f\n", risultato);
	printf("%s\n", "vuoi continuare? (1/si 0/no)");
	scanf("%d", &ans);
	while(ans==1){

		printf("%s", "inserisci un intero:");
		scanf("%d", &a);
		printf("%s", "inserisci un reale:");
		scanf("%f", &b);
		risultato = sommac(a, x, b, &taglia);
		stampa("la somma  incrementata  è ");
		printf("%s\n", taglia);
		stampa(" ed è pari a ");
		printf("%f\n", risultato);
		printf("%s", "vuoi continuare? (1/si 0/no):");
		scanf("%d", &ans);
	}
	printf("%s\n", "");
	printf("%s", "ciao");
}