grammar Expression;
colexpression: AGGREGATE '(' expression ')' 'AS' NAME
			 | AGGREGATE '(' expression ')'
             | expression 'AS' NAME
			 | expression ;
expression: expression ('*'|'/') expression
          | expression ('+'|'-') expression
          | expression ('='|'>'|'<'|'>='|'<='|'<>') expression
          | expression 'LIKE' expression
          | expression 'AND' expression
          | expression 'OR' expression
          | 'NOT' expression
          | column
		  | literal
		  | '(' expression ')'
		  ;
literal: INT
       | STRING
	   ;
column  : NAME
        | NAME '.' NAME ;
STRING  : ["].*?["] ;
INT     : [0-9]+ ;
AGGREGATE: 'SUM'
         | 'AVG'
         | 'COUNT'
         | 'MIN'
         | 'MAX';
NAME    : [a-zA-Z0-9_]+ ;
WS : [ \t\r\n]+ -> skip ;