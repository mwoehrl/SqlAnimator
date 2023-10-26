grammar Expression;
expression: expression ('*'|'/') expression
          | expression ('+'|'-') expression
          | expression '=' expression
          | expression 'LIKE' expression
          | expression 'AND' expression
          | expression 'OR' expression
          | 'NOT' expression
          | COLUMN
		  | literal
		  | '(' expression ')'
		  ;
literal: INT
       | STRING
	   ;
STRING  : ["].*?["] ;
INT     : [0-9]+ ;
COLUMN  : [a-zA-Z0-9_]+ ;
WS : [ \t\r\n]+ -> skip ;