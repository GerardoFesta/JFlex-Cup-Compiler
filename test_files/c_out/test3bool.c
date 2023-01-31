#include <stdio.h> 
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
typedef enum { false, true } bool;
char * str_concat(char* str1 , char* str2) {   char *buffer = malloc(sizeof(char) * 9999999);   *buffer = '\0';   strcat(buffer , str1);   strcat(buffer , str2);   return buffer; }

char * castIntToString(int num) {   char *buffer = malloc(sizeof(char) * 9999999);   *buffer = '\0';   sprintf(buffer , "%d" ,num);   return buffer; }

char * castFloatToString(float num) {   char *buffer = malloc(sizeof(char) * 1000000);   *buffer = '\0';   sprintf(buffer , "%f" ,num);   return buffer; }

char * castBoolToString(int num) {   char *buffer = "true";   if(num == 0) { buffer = "false"; }   return buffer; }

int castStringToint(char* num) { int ritorno; sscanf (num,"%d",&ritorno);   return ritorno; }

float castStringTofloat(char* num) { float ritorno; sscanf (num,"%f",&ritorno);   return ritorno; }

char castStringTochar(char* num) { char ritorno; sscanf (num,"%c",&ritorno);   return ritorno; }

bool castStringTobool(bool* b, char*str) { if(strcmp(str,"1")==0 || strcmp(str,"true")==0){   *b = true;   return 1;}if(strcmp(str,"0")==0 || strcmp(str,"false")==0){   *b = false;   return 0;}return -1;}
char* castStringTostring(char* num) { return num; }

char * leggiStringa() { char *buffer = malloc(sizeof(char) * 1000); scanf("%s" ,buffer);   return buffer; }
bool leggibool(bool* b){char* str = leggiStringa();if(strcmp(str,"1")==0 || strcmp(str,"true")==0){   *b = true;   return 1;}if(strcmp(str,"0")==0 || strcmp(str,"false")==0){*b = false;return 0;}return -1;}

char* principale(bool test);
bool checkbool(bool b1, bool b2, bool *risultato);

bool checkbool(bool b1, bool b2, bool *risultato){

	if((b1==b2)){

		*risultato = true;
		return true;
	}else{

		*risultato = false;
		return false;
	}
}

int main(int argc, char* argv[]){

	bool t1;
	int ts1 = castStringTobool(&t1, argv[1]);
	if(ts1==-1){
		 printf("%s","Errore, il parametro booleano dato in input non è ben formattato");
		 return 1;
	}

	principale(t1);
}


char* principale(bool test){

	bool b1;
	bool b2;
	bool b3;
	printf("%s", "Inserisci due booleani, scrivendo true/false o 1/0");
	if(leggibool(&b1)==-1){
		printf("%s","Errore lettura booleano (inserisci true/1, false/0)");
		return 1;
	}
	if(leggibool(&b2)==-1){
		printf("%s","Errore lettura booleano (inserisci true/1, false/0)");
		return 1;
	}
	if((checkbool(b1, b2, &b3)==test)){

		printf("%s%s%s%s%s%s%s%s", "L'espressione ", castBoolToString(b1), " = ", castBoolToString(b2), " ha risultato ", castBoolToString(b3), " che è uguale a quello dato in input, ovvero ", castBoolToString(test));
	}else{

		printf("%s%s%s%s%s%s%s%s", "L'espressione ", castBoolToString(b1), " = ", castBoolToString(b2), " ha risultato ", castBoolToString(b3), " che NON è uguale a quello dato in input, ovvero ", castBoolToString(test));
	}
}