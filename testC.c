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

bool castStringTobool(char* num) { bool ritorno; sscanf (num,"%d",&ritorno);   return ritorno; }

char* castStringTostring(char* num) { return num; }

char * leggiStringa() { char *buffer = malloc(sizeof(char) * 1000); scanf("%s" ,buffer);   return buffer; }

int fibonacci(int n);
float potenza(float a, float b);
float divisione_intera(float a, float b);
float moltViaSomma(float a, float b);
float somma(float *a, float *b);
void mainfun(int a, int b, float *c, float *d, char* mimmo);
int x = 10;

float somma(float *a, float *b){

	moltViaSomma(*a+1, 2+2+2);
	return *a+*b;
}

float moltViaSomma(float a, float b){

	float risultato = 0;
	float risultato_dec = 0;
	int dec_cont = 0;
	float mimmo = 1.0;
	while(b>1){

		if(b<1){

			dec_cont = dec_cont+1;
		}else{

			risultato = risultato+somma(&a, &a);
			b = b-1;
		}
	}
	while(b!=0){

		if(b<1){

			b = b*10;
			if(dec_cont!=0){

				risultato = risultato+risultato_dec/10*dec_cont;
			}
			risultato_dec = 0;
			dec_cont = dec_cont+1;
		}
		risultato_dec = risultato_dec+somma(&a, &a);
		b = b-1;
	}
	risultato = risultato+risultato_dec/10*dec_cont+1;
	return risultato;
}

float divisione_intera(float a, float b){

	float div = a/b;
	return div;
}

float potenza(float a, float b){

	return pow(a, b);
}

int fibonacci(int n){

	if(n==0||n==1){

		return 1;
	}
	return fibonacci(n-1)+fibonacci(n-2);
}

int main(int argc, char* argv[]){
	int t0 = castStringToint(argv[0]);
	int t1 = castStringToint(argv[1]);
	float t2 = castStringTofloat(argv[2]);
	float t3 = castStringTofloat(argv[3]);
	char *t4 = argv[4];

	mainfun(t0, t1, &t2, &t3, t4);
}


void mainfun(int a, int b, float *c, float *d, char* mimmo){

	int op = -1;
	printf("%s", "Inserisci il numero dell'operazione (0-esci, 1-somma, 2-molt, 3-div intera, 4-potenza, 5-fib)");
	scanf("%d", &op);
	while(op!=0){

		if(op!=5){

			int n1;
			int n2;
			printf("%s", "Inserisci due numeri");
			scanf("%d", &n1);
			scanf("%d", &n2);
			if(op==1){

				printf("%f", somma(&n1, &n2));
			}
			if(op==2){

				printf("%f", moltViaSomma(n1, n2));
			}
			if(op==3){

				printf("%f", divisione_intera(n1, n2));
			}
			if(op==4){

				printf("%f", potenza(n1, n2));
			}
		}else{

			int n;
			printf("%s", "Inserisci un numero");
			scanf("%d", &n);
			printf("%d", fibonacci(n));
		}
		printf("%s", "Inserisci il numero dell'operazione (0-esci, 1-somma, 2-molt, 3-div intera, 4-potenza, 5-fib)");
		scanf("%d", &op);
	}
}