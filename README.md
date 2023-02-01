/* BOZZA/APPUNTI

Nelle dichiarazioni delle variabili all'interno dei costrutti, lo shadowing è applicato sulla base delle tabelle dei simboli create nella prima visita del syntax tree.
Ad esempio, il codice:


integer c << 10;

start: def mainfun():void{
    integer b << c;
    integer c << 9;

    for i<<3 to 1 loop{
        integer c<<b;
        integer b<<1;
    }
}

Diventerà

int c = 10;

void mainfun(){

	int c = 9;
	int b = c; //b assume valore 9
	for(int i = 3; i >= 1; i--){

		int b = 1;
		int c = b;
	}
}

Quando, nella generazione di codice C, si fa il lookup alla tabella relativa a mainfun (per integer b << c;), la variabile c è già presente, ma sarà marcata come non assegnata. Per avere shadowing in combinazione con la proprietà di poter dichiarare una variabile dopo l'utilizzo, la lookup non procederà alla tabella globals (dove c è già assegnato). Dunque, il generatore di codice C cercherà di ordinare le assegnazioni (VarDecl).

Un approccio alternativo è quello di cercare le variabili anche nelle tabelle di livello più alto, qualora queste non fossero assegnate nella tabella corrente. In quel caso, il codice sopra diventerebbe:

int c = 10;

void mainfun(){

	int b = c; //b assume valore 10
    int c = 9
	for(int i = 3; i >= 1; i--){

		int b = 1;
		int c = b;
	}
}

*/
