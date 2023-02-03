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

int main(int argc, char* argv[]){

	mainfun();
}


void mainfun(){

	int a;
	int b;
	int e;
	int f;
	int g;
	float c;
	float h;
	float i;
	char* s;
	printf("%s", "Inserisci due numeri interi e uno decimale");
	scanf("%d", &a);
	scanf("%d", &b);
	scanf("%f", &c);
	printf("%s\n", str_concat(str_concat(str_concat(str_concat(castintToString(a), " "), castintToString(b)), " "), castfloatToString(c)));
	e = a+b-1;
	f = b-a+2;
	g = pow(2, 3);
	h = 10/3;
	i = 1.1;
	s = "TEST";
	printf("%d%s%d%s%d%s%f%s%f%s%s\n", e, " ", f, " ", g, " ", h, " ", i, " ", s);
}