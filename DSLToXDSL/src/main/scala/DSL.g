grammar DSL;

options { 
    output=AST; 
    backtrack=true; 
}

@parser::header {package trap.dsl.antlrparser.src;} 

@lexer::header {package trap.dsl.antlrparser.src;}
@parser::members { 
    public static final String version = "1.0";
    @Override    
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        throw new RuntimeException(hdr + ": " + msg);
    }
    protected Object recoverFromMismatchedToken(IntStream input,
                                                int ttype,
                                                BitSet follow)
        throws RecognitionException
    {  
        throw new MismatchedTokenException(ttype, input);
    }
}

@lexer::members { 
    @Override    
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        throw new RuntimeException(hdr + ": " + msg);
    }
}
// Lexer rules always start with an uppercase letter, while parser rules start with lowercase letter.
//  
//	.. creates a character range
//	+ means "repeat one or more times". 
//	* means "repeat zero or more times".    
//      ? means "optional" (i.e., "repeat zero or one time")
//	| means "or"        

patterns: 'patterns'^  ID (define|find)* EOF!;
find:     'find' PATTERN MAPPING* filter? -> ^('find' ^(PATTERN filter?) MAPPING*);
define: 'def' PATTERN 'as' patternmatcher -> ^('def' ^(PATTERN patternmatcher));
simplematcher: PATTERN^ filter?;
patternmatcher: simplematcher | ('{'! complexmatcher '}'!);
complexmatcher: patternmatcher boolMatcher^ patternmatcher;
filter: 'where'! '{'! rule^ '}'!;
rule: simplerule | complexrule;
mixedRule: simplerule | ('('! complexrule ')'!);
//simplerule: attrvalstr | attrvalint | attrvaldef | attrvalattr;
simplerule: attrvalbool | attrvaldecimal | attrvalstr | attrvalint | attrvaldef | attrvalattr;
complexrule: mixedRule bool^ mixedRule;
boolMatcher : bool | 'not' | 'xor';
bool : 'and' | 'or' ;
attrvalbool: ATTR opereq BOOLEAN -> ^(ATTR ^(opereq BOOLEAN)) ;
attrvaldecimal: ATTR operint DECIMAL -> ^(ATTR ^(operint DECIMAL)) ;
attrvalstr: ATTR operstr STRING -> ^(ATTR ^(operstr STRING));
attrvalint: ATTR operint INT -> ^(ATTR ^(operint INT));
attrvalattr: ATTR opereq ATTR -> ^(ATTR ^(opereq ATTR));
attrvaldef: ATTR opereq PATTERN -> ^(ATTR ^(opereq PATTERN));

opereq: EQ | NE;
operint: opereq | LT | GT | GE | LE ;
operstr: opereq | REG | GLOB;
BOOLEAN: 'true' | 'false';
MAPPING: ':' ID;
EQ: '=';
LT: '<';
GT: '>';
LE: '<=';
GE: '>=';
NE: '!=';
REG: '~~';
GLOB: '~';
ATTR : '@' ID | (PATTERN '.' '@' ID);
PATTERN : '#' ID;
mapping : ID;
ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

DECIMAL
    :    '0'..'9'+ '.' '0'..'9'+
    ;

//FLOAT
//    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
//    |   '.' ('0'..'9')+ EXPONENT?
//    |   ('0'..'9')+ EXPONENT
//    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? ('\n'|EOF) {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\''
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
