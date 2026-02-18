grammar Enquanto;

@header {
package plp.enquanto.parser;
}

programa : seqComando;

seqComando
    : (comando ';')+
    ;

comando
       : ID ':=' expressao                               # atribuicao
       | 'skip'                                          # skip
       | 'se' booleano 'entao' comando 'senao' comando   # se
       | 'enquanto' booleano 'faca' comando              # enquanto
       | 'exiba' TEXTO                                   # exiba
       | 'escreva' expressao                             # escreva
       | '{' seqComando '}'                              # bloco
       ;

expressao
         : INT                                           # int
         | 'leia'                                        # leia
         | ID                                            # id
         | '(' expressao ')'                             # ExpPar
         | expressao '^' expressao                       # OpBin
         | expressao ('*' | '/') expressao               # OpBin
         | expressao ('+' | '-') expressao               # OpBin
         ;

booleano
        : BOOLEANO                                       # Bool
        | '(' booleano ')'                               # BoolPar
        | expressao '=' expressao                        # OpRel
        | expressao '<=' expressao                       # OpRel
        | expressao '<' expressao                        # OpRel
        | expressao '>' expressao                        # OpRel
        | expressao '>=' expressao                       # OpRel
        | expressao '<>' expressao                       # OpRel
        | 'nao' booleano                                 # NaoLogico
        | booleano 'e' booleano                          # ELogico
        | booleano 'ou' booleano                         # OuLogico
        | booleano 'xor' booleano                        # XorLogico
        ;

BOOLEANO: 'verdadeiro' | 'falso';
INT: ('0'..'9')+ ;
ID: ('a'..'z')+;
TEXTO: '"' .*? '"';

Comentario: '#' .*? '\n' -> skip;
Espaco: [ \t\n\r] -> skip;
