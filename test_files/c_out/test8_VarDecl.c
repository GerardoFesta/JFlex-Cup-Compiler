#include <stdio.h> 
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
typedef enum { false, true } bool;
char * str_concat(char* str1 , char* str2) {   char *buffer = malloc(sizeof(char) * 9999999);   *buffer = '\0';   strcat(buffer , str1);   strcat(buffer , str2);   return buffer; }

char * castintToString(int num) {   char *buffer = malloc(sizeof(char) * 9999999);   *buffer = '\0';   sprintf(buffer , "%d" ,num);   return buffer; }

char * castfloatToString(float num) {   char *buffer = malloc(sizeof(char) * 1000000);   *buffer = '\0';   sprintf(buffer , "%f" ,num);   return buffer; }

char * castboolToString(int num) {   char *buffer = "true";   if(num == 0) { buffer = "false"; }   return buffer; }

int castStringToint(char* num) { int ritorno; sscanf (num,"%d",&ritorno);   return ritorno; }

float castStringTofloat(char* num) { float ritorno; sscanf (num,"%f",&ritorno);   return ritorno; }

char castStringTochar(char* num) { char ritorno; sscanf (num,"%c",&ritorno);   return ritorno; }

bool castStringTobool(bool* b, char*str) { if(strcmp(str,"1")==0 || strcmp(str,"true")==0){   *b = true;   return 1;}if(strcmp(str,"0")==0 || strcmp(str,"false")==0){   *b = false;   return 0;}return -1;}
char* caststringTostring(char* num) { return num; }

char * leggiStringa() { char *buffer = malloc(sizeof(char) * 1000); scanf("%s" ,buffer);   return buffer; }
bool leggibool(bool* b){char* str = leggiStringa();if(strcmp(str,"1")==0 || strcmp(str,"true")==0){   *b = true;   return 1;}if(strcmp(str,"0")==0 || strcmp(str,"false")==0){*b = false;return 0;}return -1;}

void mainfun();
int c = 10;

int main(int argc, char* argv[]){

	mainfun();
}


void mainfun(){

	int c = 9;
	int f;
	int d = 5;
	int b = c;
	int e = d;
	printf("%s%d%s%d\n", "Fuori dal for, c e b dovrebbero valere 9 e 9 e valgono rispettivamente: ", c, " ", b);
	printf("%s%d%s%d\n", "Invece, e e d dovrebbero valere 5 e 5 e valgono rispettivamente: ", e, " ", d);
	for(int i = 3; i >= 1; i--){

		int b = 1;
		int c = b;
		printf("%s%d%s%d\n", "Nel for, c e b dovrebbero valere 1 e 1 e valgono rispettivamente: ", c, " ", b);
	}
}