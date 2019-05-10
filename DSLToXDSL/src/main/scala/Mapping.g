grammar Mapping;

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

mappings: 'mappings'! ID^  (mapping)+ EOF!;
mapping: 'map'! MAP^ 'as'! keyMaps ';'!;
MAP: ':' ID;
keyMaps: keyMap | (keyMap ','! keyMaps);
keyMap:  KEY^ '=>'! value?;

//value: PATTERN ('.' ATTR ('.' AGGR)?)? -> ^(PATTERN ^(ATTR ^(AGGR)?)?); // older version (v0), without COUNT at root or pattern level
value : COUNT | pattern;
//pattern : PATTERN^ ('.'! countOrAttribute)?;
pattern : PATTERN^ ('.'! countOrAttribute (','! countOrAttribute)*)?;
countOrAttribute: COUNT | attribute;
attribute: ATTR^ ('.'! countOrAggr)?;
countOrAggr: COUNT | AGGR;
PATTERN : '#' ID;
KEY: '$' ID;
ATTR: '@' ID;
// AGGR: 'count' | 'max'|'min'|'sum'|'avg'; // older version (v0), with COUNT as an aggregate
AGGR: 'max'|'min'|'sum'|'avg';

COUNT : 'count';

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

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
