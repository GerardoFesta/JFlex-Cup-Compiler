<h1>JFlex and Cup Compiler</h1>
This project was developed as part of the Compiler exam, in the M.Sc. course in Computer Science at Università degli Studi di Salerno.

In the repository, imported from GitLab, the Lexical Analyzer is developed via JFlex, the abstract syntax tree is built with Cup (Parser) and visited through the Visitor design pattern to perform Semantic Analysis, i.e. scoping and type checking. After that, the compiler is able to translate from the starting language to the C language, meaning that this implements just the **front-end of the compiler**.


The Context Free Grammar and Lexical rules of the starting Language - namely NewLang - can be found in the 'Grammar and Lexicon.txt' file.
The main challenge of NewLang is the possibility of declaring variables after they are used, while C does not grant this flexibility. 

To start the compiler, you can either import the project in IntelliJIdea and change the configurations to translate your own file, or use the 'NewLangCompiler.bat', changing the test file path.
