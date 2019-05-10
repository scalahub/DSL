// $ANTLR 3.4 src/Mapping.g 2017-04-26 11:47:58
package trap.dsl.antlrparser.src;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class MappingLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int AGGR=4;
    public static final int ATTR=5;
    public static final int COMMENT=6;
    public static final int COUNT=7;
    public static final int ESC_SEQ=8;
    public static final int EXPONENT=9;
    public static final int HEX_DIGIT=10;
    public static final int ID=11;
    public static final int INT=12;
    public static final int KEY=13;
    public static final int MAP=14;
    public static final int OCTAL_ESC=15;
    public static final int PATTERN=16;
    public static final int STRING=17;
    public static final int UNICODE_ESC=18;
    public static final int WS=19;
     
        @Override    
        public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
            String hdr = getErrorHeader(e);
            String msg = getErrorMessage(e, tokenNames);
            throw new RuntimeException(hdr + ": " + msg);
        }


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public MappingLexer() {} 
    public MappingLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public MappingLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "src/Mapping.g"; }

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:12:7: ( ',' )
            // src/Mapping.g:12:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:13:7: ( '.' )
            // src/Mapping.g:13:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:14:7: ( ';' )
            // src/Mapping.g:14:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:15:7: ( '=>' )
            // src/Mapping.g:15:9: '=>'
            {
            match("=>"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:16:7: ( 'as' )
            // src/Mapping.g:16:9: 'as'
            {
            match("as"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:17:7: ( 'map' )
            // src/Mapping.g:17:9: 'map'
            {
            match("map"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:18:7: ( 'mappings' )
            // src/Mapping.g:18:9: 'mappings'
            {
            match("mappings"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "MAP"
    public final void mMAP() throws RecognitionException {
        try {
            int _type = MAP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:47:4: ( ':' ID )
            // src/Mapping.g:47:6: ':' ID
            {
            match(':'); 

            mID(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MAP"

    // $ANTLR start "PATTERN"
    public final void mPATTERN() throws RecognitionException {
        try {
            int _type = PATTERN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:58:9: ( '#' ID )
            // src/Mapping.g:58:11: '#' ID
            {
            match('#'); 

            mID(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PATTERN"

    // $ANTLR start "KEY"
    public final void mKEY() throws RecognitionException {
        try {
            int _type = KEY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:59:4: ( '$' ID )
            // src/Mapping.g:59:6: '$' ID
            {
            match('$'); 

            mID(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "KEY"

    // $ANTLR start "ATTR"
    public final void mATTR() throws RecognitionException {
        try {
            int _type = ATTR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:60:5: ( '@' ID )
            // src/Mapping.g:60:7: '@' ID
            {
            match('@'); 

            mID(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTR"

    // $ANTLR start "AGGR"
    public final void mAGGR() throws RecognitionException {
        try {
            int _type = AGGR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:62:5: ( 'max' | 'min' | 'sum' | 'avg' )
            int alt1=4;
            switch ( input.LA(1) ) {
            case 'm':
                {
                int LA1_1 = input.LA(2);

                if ( (LA1_1=='a') ) {
                    alt1=1;
                }
                else if ( (LA1_1=='i') ) {
                    alt1=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;

                }
                }
                break;
            case 's':
                {
                alt1=3;
                }
                break;
            case 'a':
                {
                alt1=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }

            switch (alt1) {
                case 1 :
                    // src/Mapping.g:62:7: 'max'
                    {
                    match("max"); 



                    }
                    break;
                case 2 :
                    // src/Mapping.g:62:13: 'min'
                    {
                    match("min"); 



                    }
                    break;
                case 3 :
                    // src/Mapping.g:62:19: 'sum'
                    {
                    match("sum"); 



                    }
                    break;
                case 4 :
                    // src/Mapping.g:62:25: 'avg'
                    {
                    match("avg"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AGGR"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:64:7: ( 'count' )
            // src/Mapping.g:64:9: 'count'
            {
            match("count"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:66:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src/Mapping.g:66:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // src/Mapping.g:66:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/Mapping.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:69:5: ( ( '0' .. '9' )+ )
            // src/Mapping.g:69:7: ( '0' .. '9' )+
            {
            // src/Mapping.g:69:7: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // src/Mapping.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:73:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? ( '\\n' | EOF ) | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='/') ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1=='/') ) {
                    alt8=1;
                }
                else if ( (LA8_1=='*') ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // src/Mapping.g:73:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? ( '\\n' | EOF )
                    {
                    match("//"); 



                    // src/Mapping.g:73:14: (~ ( '\\n' | '\\r' ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0 >= '\u0000' && LA4_0 <= '\t')||(LA4_0 >= '\u000B' && LA4_0 <= '\f')||(LA4_0 >= '\u000E' && LA4_0 <= '\uFFFF')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // src/Mapping.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    // src/Mapping.g:73:28: ( '\\r' )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0=='\r') ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // src/Mapping.g:73:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }


                    // src/Mapping.g:73:34: ( '\\n' | EOF )
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='\n') ) {
                        alt6=1;
                    }
                    else {
                        alt6=2;
                    }
                    switch (alt6) {
                        case 1 :
                            // src/Mapping.g:73:35: '\\n'
                            {
                            match('\n'); 

                            }
                            break;
                        case 2 :
                            // src/Mapping.g:73:40: EOF
                            {
                            match(EOF); 


                            }
                            break;

                    }


                    _channel=HIDDEN;

                    }
                    break;
                case 2 :
                    // src/Mapping.g:74:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 



                    // src/Mapping.g:74:14: ( options {greedy=false; } : . )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0=='*') ) {
                            int LA7_1 = input.LA(2);

                            if ( (LA7_1=='/') ) {
                                alt7=2;
                            }
                            else if ( ((LA7_1 >= '\u0000' && LA7_1 <= '.')||(LA7_1 >= '0' && LA7_1 <= '\uFFFF')) ) {
                                alt7=1;
                            }


                        }
                        else if ( ((LA7_0 >= '\u0000' && LA7_0 <= ')')||(LA7_0 >= '+' && LA7_0 <= '\uFFFF')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // src/Mapping.g:74:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);


                    match("*/"); 



                    _channel=HIDDEN;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:77:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // src/Mapping.g:77:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/Mapping.g:85:5: ( '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\'' )
            // src/Mapping.g:85:8: '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 

            // src/Mapping.g:85:13: ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )*
            loop9:
            do {
                int alt9=3;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='\\') ) {
                    alt9=1;
                }
                else if ( ((LA9_0 >= '\u0000' && LA9_0 <= '&')||(LA9_0 >= '(' && LA9_0 <= '[')||(LA9_0 >= ']' && LA9_0 <= '\uFFFF')) ) {
                    alt9=2;
                }


                switch (alt9) {
            	case 1 :
            	    // src/Mapping.g:85:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // src/Mapping.g:85:25: ~ ( '\\\\' | '\\'' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "EXPONENT"
    public final void mEXPONENT() throws RecognitionException {
        try {
            // src/Mapping.g:90:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // src/Mapping.g:90:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // src/Mapping.g:90:22: ( '+' | '-' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='+'||LA10_0=='-') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // src/Mapping.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // src/Mapping.g:90:33: ( '0' .. '9' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // src/Mapping.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EXPONENT"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // src/Mapping.g:93:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // src/Mapping.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // src/Mapping.g:97:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt12=1;
                    }
                    break;
                case 'u':
                    {
                    alt12=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt12=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }
            switch (alt12) {
                case 1 :
                    // src/Mapping.g:97:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 

                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // src/Mapping.g:98:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // src/Mapping.g:99:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // src/Mapping.g:104:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='\\') ) {
                int LA13_1 = input.LA(2);

                if ( ((LA13_1 >= '0' && LA13_1 <= '3')) ) {
                    int LA13_2 = input.LA(3);

                    if ( ((LA13_2 >= '0' && LA13_2 <= '7')) ) {
                        int LA13_4 = input.LA(4);

                        if ( ((LA13_4 >= '0' && LA13_4 <= '7')) ) {
                            alt13=1;
                        }
                        else {
                            alt13=2;
                        }
                    }
                    else {
                        alt13=3;
                    }
                }
                else if ( ((LA13_1 >= '4' && LA13_1 <= '7')) ) {
                    int LA13_3 = input.LA(3);

                    if ( ((LA13_3 >= '0' && LA13_3 <= '7')) ) {
                        alt13=2;
                    }
                    else {
                        alt13=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // src/Mapping.g:104:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // src/Mapping.g:105:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 3 :
                    // src/Mapping.g:106:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // src/Mapping.g:111:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // src/Mapping.g:111:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 

            match('u'); 

            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // src/Mapping.g:1:8: ( T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | MAP | PATTERN | KEY | ATTR | AGGR | COUNT | ID | INT | COMMENT | WS | STRING )
        int alt14=18;
        switch ( input.LA(1) ) {
        case ',':
            {
            alt14=1;
            }
            break;
        case '.':
            {
            alt14=2;
            }
            break;
        case ';':
            {
            alt14=3;
            }
            break;
        case '=':
            {
            alt14=4;
            }
            break;
        case 'a':
            {
            switch ( input.LA(2) ) {
            case 's':
                {
                int LA14_18 = input.LA(3);

                if ( ((LA14_18 >= '0' && LA14_18 <= '9')||(LA14_18 >= 'A' && LA14_18 <= 'Z')||LA14_18=='_'||(LA14_18 >= 'a' && LA14_18 <= 'z')) ) {
                    alt14=14;
                }
                else {
                    alt14=5;
                }
                }
                break;
            case 'v':
                {
                int LA14_19 = input.LA(3);

                if ( (LA14_19=='g') ) {
                    int LA14_25 = input.LA(4);

                    if ( ((LA14_25 >= '0' && LA14_25 <= '9')||(LA14_25 >= 'A' && LA14_25 <= 'Z')||LA14_25=='_'||(LA14_25 >= 'a' && LA14_25 <= 'z')) ) {
                        alt14=14;
                    }
                    else {
                        alt14=12;
                    }
                }
                else {
                    alt14=14;
                }
                }
                break;
            default:
                alt14=14;
            }

            }
            break;
        case 'm':
            {
            switch ( input.LA(2) ) {
            case 'a':
                {
                switch ( input.LA(3) ) {
                case 'p':
                    {
                    switch ( input.LA(4) ) {
                    case 'p':
                        {
                        int LA14_32 = input.LA(5);

                        if ( (LA14_32=='i') ) {
                            int LA14_35 = input.LA(6);

                            if ( (LA14_35=='n') ) {
                                int LA14_37 = input.LA(7);

                                if ( (LA14_37=='g') ) {
                                    int LA14_39 = input.LA(8);

                                    if ( (LA14_39=='s') ) {
                                        int LA14_40 = input.LA(9);

                                        if ( ((LA14_40 >= '0' && LA14_40 <= '9')||(LA14_40 >= 'A' && LA14_40 <= 'Z')||LA14_40=='_'||(LA14_40 >= 'a' && LA14_40 <= 'z')) ) {
                                            alt14=14;
                                        }
                                        else {
                                            alt14=7;
                                        }
                                    }
                                    else {
                                        alt14=14;
                                    }
                                }
                                else {
                                    alt14=14;
                                }
                            }
                            else {
                                alt14=14;
                            }
                        }
                        else {
                            alt14=14;
                        }
                        }
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '_':
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z':
                        {
                        alt14=14;
                        }
                        break;
                    default:
                        alt14=6;
                    }

                    }
                    break;
                case 'x':
                    {
                    int LA14_27 = input.LA(4);

                    if ( ((LA14_27 >= '0' && LA14_27 <= '9')||(LA14_27 >= 'A' && LA14_27 <= 'Z')||LA14_27=='_'||(LA14_27 >= 'a' && LA14_27 <= 'z')) ) {
                        alt14=14;
                    }
                    else {
                        alt14=12;
                    }
                    }
                    break;
                default:
                    alt14=14;
                }

                }
                break;
            case 'i':
                {
                int LA14_21 = input.LA(3);

                if ( (LA14_21=='n') ) {
                    int LA14_28 = input.LA(4);

                    if ( ((LA14_28 >= '0' && LA14_28 <= '9')||(LA14_28 >= 'A' && LA14_28 <= 'Z')||LA14_28=='_'||(LA14_28 >= 'a' && LA14_28 <= 'z')) ) {
                        alt14=14;
                    }
                    else {
                        alt14=12;
                    }
                }
                else {
                    alt14=14;
                }
                }
                break;
            default:
                alt14=14;
            }

            }
            break;
        case ':':
            {
            alt14=8;
            }
            break;
        case '#':
            {
            alt14=9;
            }
            break;
        case '$':
            {
            alt14=10;
            }
            break;
        case '@':
            {
            alt14=11;
            }
            break;
        case 's':
            {
            int LA14_11 = input.LA(2);

            if ( (LA14_11=='u') ) {
                int LA14_22 = input.LA(3);

                if ( (LA14_22=='m') ) {
                    int LA14_29 = input.LA(4);

                    if ( ((LA14_29 >= '0' && LA14_29 <= '9')||(LA14_29 >= 'A' && LA14_29 <= 'Z')||LA14_29=='_'||(LA14_29 >= 'a' && LA14_29 <= 'z')) ) {
                        alt14=14;
                    }
                    else {
                        alt14=12;
                    }
                }
                else {
                    alt14=14;
                }
            }
            else {
                alt14=14;
            }
            }
            break;
        case 'c':
            {
            int LA14_12 = input.LA(2);

            if ( (LA14_12=='o') ) {
                int LA14_23 = input.LA(3);

                if ( (LA14_23=='u') ) {
                    int LA14_30 = input.LA(4);

                    if ( (LA14_30=='n') ) {
                        int LA14_34 = input.LA(5);

                        if ( (LA14_34=='t') ) {
                            int LA14_36 = input.LA(6);

                            if ( ((LA14_36 >= '0' && LA14_36 <= '9')||(LA14_36 >= 'A' && LA14_36 <= 'Z')||LA14_36=='_'||(LA14_36 >= 'a' && LA14_36 <= 'z')) ) {
                                alt14=14;
                            }
                            else {
                                alt14=13;
                            }
                        }
                        else {
                            alt14=14;
                        }
                    }
                    else {
                        alt14=14;
                    }
                }
                else {
                    alt14=14;
                }
            }
            else {
                alt14=14;
            }
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '_':
        case 'b':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt14=14;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt14=15;
            }
            break;
        case '/':
            {
            alt14=16;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt14=17;
            }
            break;
        case '\'':
            {
            alt14=18;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 14, 0, input);

            throw nvae;

        }

        switch (alt14) {
            case 1 :
                // src/Mapping.g:1:10: T__20
                {
                mT__20(); 


                }
                break;
            case 2 :
                // src/Mapping.g:1:16: T__21
                {
                mT__21(); 


                }
                break;
            case 3 :
                // src/Mapping.g:1:22: T__22
                {
                mT__22(); 


                }
                break;
            case 4 :
                // src/Mapping.g:1:28: T__23
                {
                mT__23(); 


                }
                break;
            case 5 :
                // src/Mapping.g:1:34: T__24
                {
                mT__24(); 


                }
                break;
            case 6 :
                // src/Mapping.g:1:40: T__25
                {
                mT__25(); 


                }
                break;
            case 7 :
                // src/Mapping.g:1:46: T__26
                {
                mT__26(); 


                }
                break;
            case 8 :
                // src/Mapping.g:1:52: MAP
                {
                mMAP(); 


                }
                break;
            case 9 :
                // src/Mapping.g:1:56: PATTERN
                {
                mPATTERN(); 


                }
                break;
            case 10 :
                // src/Mapping.g:1:64: KEY
                {
                mKEY(); 


                }
                break;
            case 11 :
                // src/Mapping.g:1:68: ATTR
                {
                mATTR(); 


                }
                break;
            case 12 :
                // src/Mapping.g:1:73: AGGR
                {
                mAGGR(); 


                }
                break;
            case 13 :
                // src/Mapping.g:1:78: COUNT
                {
                mCOUNT(); 


                }
                break;
            case 14 :
                // src/Mapping.g:1:84: ID
                {
                mID(); 


                }
                break;
            case 15 :
                // src/Mapping.g:1:87: INT
                {
                mINT(); 


                }
                break;
            case 16 :
                // src/Mapping.g:1:91: COMMENT
                {
                mCOMMENT(); 


                }
                break;
            case 17 :
                // src/Mapping.g:1:99: WS
                {
                mWS(); 


                }
                break;
            case 18 :
                // src/Mapping.g:1:102: STRING
                {
                mSTRING(); 


                }
                break;

        }

    }


 

}