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

int fibonacci(int n);
void starter();
void askContinue(bool *continua);
float somma(float a, float b);
float moltiplicazioneViaSomma(int a, int b);
float divisione(float a, float b);
void askIntNumbers(int *n1, int *n2);
float potenza(float a, float b);
float moltiplicazione(float a, float b);
float sottrazione(float a, float b);
void selectOp(int *op);
void askNumbers(float *n1, float *n2);
int divisioneIntera(int a, int b);

float somma(float a, float b){

	return a+b;
}

float sottrazione(float a, float b){

	return a-b;
}

float moltiplicazione(float a, float b){

	return a*b;
}

float moltiplicazioneViaSomma(int a, int b){

	int contatore = 0;
	float risSomma = 0;
	while((contatore!=b)){

		risSomma = somma(a, a);
		contatore = contatore+1;
	}
	return risSomma;
}

int divisioneIntera(int a, int b){

	return a/b;
}

float divisione(float a, float b){

	return a/b;
}

float potenza(float a, float b){

	return pow(a, b);
}

void selectOp(int *op){

	printf("%s", "Inserisci l'operazione (1 somma, 2 sott, 3 molt, 4 div, 5 potenza, 6 fibonacci)");
	scanf("%d", &*op);
	return ;
}

void askContinue(bool *continua){

	int risposta;
	printf("%s", "Vuoi continuare? (1 Si, 0 no)");
	scanf("%d", &risposta);
	if((risposta==1)){

		*continua = true;
	}else{

		*continua = false;
	}
	return ;
}

void askNumbers(float *n1, float *n2){

	printf("%s", "Inserisci il primo numero: ");
	scanf("%f", &*n1);
	printf("%s", "Inserisci il secondo numero: ");
	scanf("%f", &*n2);
}

int main(int argc, char* argv[]){

	starter();
}


void starter(){

	int operazione;
	bool continua;
	float num1;
	float num2;
	int intnum1;
	int intnum2;
	continua = true;
	while((continua)){

		selectOp(&operazione);
		if((operazione==1)){

			askNumbers(&num1, &num2);
			printf("%f\n", somma(num1, num2));
		}else{

			if((operazione==2)){

				askNumbers(&num1, &num2);
				printf("%f\n", sottrazione(num1, num2));
			}else{

				if((operazione==3)){

					askIntNumbers(&intnum1, &intnum2);
					printf("%f\n", moltiplicazione(intnum1, intnum2));
				}else{

					if((operazione==4)){

						askIntNumbers(&intnum1, &intnum2);
						printf("%d\n", divisioneIntera(intnum1, intnum2));
					}else{

						if((operazione==5)){

							askNumbers(&num1, &num2);
							printf("%f\n", potenza(intnum1, intnum2));
						}else{

							if((operazione==6)){

								int intnum1;
								printf("%s", "Inserisci un numero intero: ");
								scanf("%d", &intnum1);
								printf("%d\n", fibonacci(intnum1));
							}else{

								printf("%s\n", "Hai inserito un numero non corretto!");
							}
						}
					}
				}
			}
		}
		askContinue(&continua);
	}
}

void askIntNumbers(int *n1, int *n2){

	printf("%s", "Inserisci il primo numero INTERO: ");
	scanf("%d", &*n1);
	printf("%s", "Inserisci il secondo numero INTERO: ");
	scanf("%d", &*n2);
}

int fibonacci(int n){

	if((n==0)){

		return 0;
	}
	if((n==1)){

		return 1;
	}
	return fibonacci(n-1)+fibonacci(n-2);
}