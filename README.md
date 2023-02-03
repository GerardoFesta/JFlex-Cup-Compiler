/* BOZZA/APPUNTI

Nelle dichiarazioni delle variabili all'interno dei costrutti, lo shadowing è applicato sulla base delle tabelle dei simboli create nella prima visita del syntax tree.
Ad esempio, il codice:

```
integer c << 10;

start: def mainfun():void{
    integer b << c;
    integer c << 9;
    for i<<3 to 1 loop{
        integer c<<b;
        integer b<<1;
    }
}
```
Diventerà
```
int c = 10;

void mainfun(){

	int c = 9;
	int b = c; //b assume valore 9
	for(int i = 3; i >= 1; i--){

		int b = 1;
		int c = b;
	}
}
```
Quando, nella generazione di codice C, si fa il lookup alla tabella relativa a mainfun (per integer b << c;), la variabile c è già presente, ma sarà marcata come non assegnata. Per avere shadowing in combinazione con la proprietà di poter dichiarare una variabile dopo l'utilizzo, la lookup non procederà alla tabella globals (dove c è già assegnato). Dunque, il generatore di codice C cercherà di ordinare le assegnazioni (VarDecl).

Un approccio alternativo è quello di cercare le variabili anche nelle tabelle di livello più alto, qualora queste non fossero assegnate nella tabella corrente. In quel caso, il codice sopra diventerebbe:
```
int c = 10;

void mainfun(){

	int b = c; //b assume valore 10
	int c = 9
	for(int i = 3; i >= 1; i--){

		int b = 1;
		int c = b;
	}
}
```

TYPE SYSTEM

Aggiunte al type system dato sul sito, le seguenti regole:

READ
![](docs/type_rules/Read.png)

READ CON STRINGA
![](docs/type_rules/ReadConStringa.png)

WRITE
![](docs/type_rules/Write.png)

CHIAMATA A FUNZIONE CON RITORNO O SENZA E CON CONTROLLO PARAMETRI OUT
![](docs/type_rules/FunCall_out_RitornoPROVA.png)
![](docs/type_rules/FunCall_out_NoRitornoPROVA.png)

Inerentemente alle regole di tipo, si riportano i casi limite per le tabelle optype1 e optype2
presenti nel documento da Lei redatto.
Tutte le operazioni di moltiplicazione, addizione, potenza, divisione, sono applicabili solo su interi e float.
 Il risultato dell'operazione è float laddove almeno uno dei due operatori sia float, intero altrimenti.
La concatenazione STR_CONC (&), è permessa non solo tra due stringhe, ma su tutti i tipi presenti nel linguaggio.
 Ad esempio, è possibile usare STR_CONC su un intero e un float, ma il risultato sarà sempre una stringa.

*/
