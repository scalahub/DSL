// $ANTLR 3.4 src/DSL.g 2017-03-06 04:26:07
package trap.dsl.antlrparser.src;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class DSLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ATTR", "BOOLEAN", "COMMENT", "DECIMAL", "EQ", "ESC_SEQ", "EXPONENT", "GE", "GLOB", "GT", "HEX_DIGIT", "ID", "INT", "LE", "LT", "MAPPING", "NE", "OCTAL_ESC", "PATTERN", "REG", "STRING", "UNICODE_ESC", "WS", "'('", "')'", "'and'", "'as'", "'def'", "'find'", "'not'", "'or'", "'patterns'", "'where'", "'xor'", "'{'", "'}'"
    };

    public static final int EOF=-1;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int ATTR=4;
    public static final int BOOLEAN=5;
    public static final int COMMENT=6;
    public static final int DECIMAL=7;
    public static final int EQ=8;
    public static final int ESC_SEQ=9;
    public static final int EXPONENT=10;
    public static final int GE=11;
    public static final int GLOB=12;
    public static final int GT=13;
    public static final int HEX_DIGIT=14;
    public static final int ID=15;
    public static final int INT=16;
    public static final int LE=17;
    public static final int LT=18;
    public static final int MAPPING=19;
    public static final int NE=20;
    public static final int OCTAL_ESC=21;
    public static final int PATTERN=22;
    public static final int REG=23;
    public static final int STRING=24;
    public static final int UNICODE_ESC=25;
    public static final int WS=26;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public DSLParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public DSLParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return DSLParser.tokenNames; }
    public String getGrammarFileName() { return "src/DSL.g"; }

     
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


    public static class patterns_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "patterns"
    // src/DSL.g:44:1: patterns : 'patterns' ^ ID ( define | find )* EOF !;
    public final DSLParser.patterns_return patterns() throws RecognitionException {
        DSLParser.patterns_return retval = new DSLParser.patterns_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal1=null;
        Token ID2=null;
        Token EOF5=null;
        DSLParser.define_return define3 =null;

        DSLParser.find_return find4 =null;


        Object string_literal1_tree=null;
        Object ID2_tree=null;
        Object EOF5_tree=null;

        try {
            // src/DSL.g:44:9: ( 'patterns' ^ ID ( define | find )* EOF !)
            // src/DSL.g:44:11: 'patterns' ^ ID ( define | find )* EOF !
            {
            root_0 = (Object)adaptor.nil();


            string_literal1=(Token)match(input,35,FOLLOW_35_in_patterns78); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal1_tree = 
            (Object)adaptor.create(string_literal1)
            ;
            root_0 = (Object)adaptor.becomeRoot(string_literal1_tree, root_0);
            }

            ID2=(Token)match(input,ID,FOLLOW_ID_in_patterns82); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID2_tree = 
            (Object)adaptor.create(ID2)
            ;
            adaptor.addChild(root_0, ID2_tree);
            }

            // src/DSL.g:44:27: ( define | find )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==31) ) {
                    alt1=1;
                }
                else if ( (LA1_0==32) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // src/DSL.g:44:28: define
            	    {
            	    pushFollow(FOLLOW_define_in_patterns85);
            	    define3=define();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, define3.getTree());

            	    }
            	    break;
            	case 2 :
            	    // src/DSL.g:44:35: find
            	    {
            	    pushFollow(FOLLOW_find_in_patterns87);
            	    find4=find();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, find4.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            EOF5=(Token)match(input,EOF,FOLLOW_EOF_in_patterns91); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "patterns"


    public static class find_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "find"
    // src/DSL.g:45:1: find : 'find' PATTERN ( MAPPING )* ( filter )? -> ^( 'find' ^( PATTERN ( filter )? ) ( MAPPING )* ) ;
    public final DSLParser.find_return find() throws RecognitionException {
        DSLParser.find_return retval = new DSLParser.find_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal6=null;
        Token PATTERN7=null;
        Token MAPPING8=null;
        DSLParser.filter_return filter9 =null;


        Object string_literal6_tree=null;
        Object PATTERN7_tree=null;
        Object MAPPING8_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleTokenStream stream_MAPPING=new RewriteRuleTokenStream(adaptor,"token MAPPING");
        RewriteRuleTokenStream stream_32=new RewriteRuleTokenStream(adaptor,"token 32");
        RewriteRuleSubtreeStream stream_filter=new RewriteRuleSubtreeStream(adaptor,"rule filter");
        try {
            // src/DSL.g:45:5: ( 'find' PATTERN ( MAPPING )* ( filter )? -> ^( 'find' ^( PATTERN ( filter )? ) ( MAPPING )* ) )
            // src/DSL.g:45:11: 'find' PATTERN ( MAPPING )* ( filter )?
            {
            string_literal6=(Token)match(input,32,FOLLOW_32_in_find102); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_32.add(string_literal6);


            PATTERN7=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_find104); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN7);


            // src/DSL.g:45:26: ( MAPPING )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==MAPPING) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/DSL.g:45:26: MAPPING
            	    {
            	    MAPPING8=(Token)match(input,MAPPING,FOLLOW_MAPPING_in_find106); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_MAPPING.add(MAPPING8);


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            // src/DSL.g:45:35: ( filter )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==36) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // src/DSL.g:45:35: filter
                    {
                    pushFollow(FOLLOW_filter_in_find109);
                    filter9=filter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_filter.add(filter9.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: PATTERN, filter, 32, MAPPING
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 45:43: -> ^( 'find' ^( PATTERN ( filter )? ) ( MAPPING )* )
            {
                // src/DSL.g:45:46: ^( 'find' ^( PATTERN ( filter )? ) ( MAPPING )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_32.nextNode()
                , root_1);

                // src/DSL.g:45:55: ^( PATTERN ( filter )? )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                stream_PATTERN.nextNode()
                , root_2);

                // src/DSL.g:45:65: ( filter )?
                if ( stream_filter.hasNext() ) {
                    adaptor.addChild(root_2, stream_filter.nextTree());

                }
                stream_filter.reset();

                adaptor.addChild(root_1, root_2);
                }

                // src/DSL.g:45:74: ( MAPPING )*
                while ( stream_MAPPING.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_MAPPING.nextNode()
                    );

                }
                stream_MAPPING.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "find"


    public static class define_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "define"
    // src/DSL.g:46:1: define : 'def' PATTERN 'as' patternmatcher -> ^( 'def' ^( PATTERN patternmatcher ) ) ;
    public final DSLParser.define_return define() throws RecognitionException {
        DSLParser.define_return retval = new DSLParser.define_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal10=null;
        Token PATTERN11=null;
        Token string_literal12=null;
        DSLParser.patternmatcher_return patternmatcher13 =null;


        Object string_literal10_tree=null;
        Object PATTERN11_tree=null;
        Object string_literal12_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
        RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
        RewriteRuleSubtreeStream stream_patternmatcher=new RewriteRuleSubtreeStream(adaptor,"rule patternmatcher");
        try {
            // src/DSL.g:46:7: ( 'def' PATTERN 'as' patternmatcher -> ^( 'def' ^( PATTERN patternmatcher ) ) )
            // src/DSL.g:46:9: 'def' PATTERN 'as' patternmatcher
            {
            string_literal10=(Token)match(input,31,FOLLOW_31_in_define132); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_31.add(string_literal10);


            PATTERN11=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_define134); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN11);


            string_literal12=(Token)match(input,30,FOLLOW_30_in_define136); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_30.add(string_literal12);


            pushFollow(FOLLOW_patternmatcher_in_define138);
            patternmatcher13=patternmatcher();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternmatcher.add(patternmatcher13.getTree());

            // AST REWRITE
            // elements: PATTERN, patternmatcher, 31
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 46:43: -> ^( 'def' ^( PATTERN patternmatcher ) )
            {
                // src/DSL.g:46:46: ^( 'def' ^( PATTERN patternmatcher ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_31.nextNode()
                , root_1);

                // src/DSL.g:46:54: ^( PATTERN patternmatcher )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                stream_PATTERN.nextNode()
                , root_2);

                adaptor.addChild(root_2, stream_patternmatcher.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "define"


    public static class simplematcher_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "simplematcher"
    // src/DSL.g:47:1: simplematcher : PATTERN ^ ( filter )? ;
    public final DSLParser.simplematcher_return simplematcher() throws RecognitionException {
        DSLParser.simplematcher_return retval = new DSLParser.simplematcher_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PATTERN14=null;
        DSLParser.filter_return filter15 =null;


        Object PATTERN14_tree=null;

        try {
            // src/DSL.g:47:14: ( PATTERN ^ ( filter )? )
            // src/DSL.g:47:16: PATTERN ^ ( filter )?
            {
            root_0 = (Object)adaptor.nil();


            PATTERN14=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_simplematcher156); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PATTERN14_tree = 
            (Object)adaptor.create(PATTERN14)
            ;
            root_0 = (Object)adaptor.becomeRoot(PATTERN14_tree, root_0);
            }

            // src/DSL.g:47:25: ( filter )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==36) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // src/DSL.g:47:25: filter
                    {
                    pushFollow(FOLLOW_filter_in_simplematcher159);
                    filter15=filter();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, filter15.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "simplematcher"


    public static class patternmatcher_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "patternmatcher"
    // src/DSL.g:48:1: patternmatcher : ( simplematcher | ( '{' ! complexmatcher '}' !) );
    public final DSLParser.patternmatcher_return patternmatcher() throws RecognitionException {
        DSLParser.patternmatcher_return retval = new DSLParser.patternmatcher_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal17=null;
        Token char_literal19=null;
        DSLParser.simplematcher_return simplematcher16 =null;

        DSLParser.complexmatcher_return complexmatcher18 =null;


        Object char_literal17_tree=null;
        Object char_literal19_tree=null;

        try {
            // src/DSL.g:48:15: ( simplematcher | ( '{' ! complexmatcher '}' !) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==PATTERN) ) {
                alt5=1;
            }
            else if ( (LA5_0==38) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // src/DSL.g:48:17: simplematcher
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_simplematcher_in_patternmatcher166);
                    simplematcher16=simplematcher();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simplematcher16.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:48:33: ( '{' ! complexmatcher '}' !)
                    {
                    root_0 = (Object)adaptor.nil();


                    // src/DSL.g:48:33: ( '{' ! complexmatcher '}' !)
                    // src/DSL.g:48:34: '{' ! complexmatcher '}' !
                    {
                    char_literal17=(Token)match(input,38,FOLLOW_38_in_patternmatcher171); if (state.failed) return retval;

                    pushFollow(FOLLOW_complexmatcher_in_patternmatcher174);
                    complexmatcher18=complexmatcher();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, complexmatcher18.getTree());

                    char_literal19=(Token)match(input,39,FOLLOW_39_in_patternmatcher176); if (state.failed) return retval;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "patternmatcher"


    public static class complexmatcher_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "complexmatcher"
    // src/DSL.g:49:1: complexmatcher : patternmatcher boolMatcher ^ patternmatcher ;
    public final DSLParser.complexmatcher_return complexmatcher() throws RecognitionException {
        DSLParser.complexmatcher_return retval = new DSLParser.complexmatcher_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        DSLParser.patternmatcher_return patternmatcher20 =null;

        DSLParser.boolMatcher_return boolMatcher21 =null;

        DSLParser.patternmatcher_return patternmatcher22 =null;



        try {
            // src/DSL.g:49:15: ( patternmatcher boolMatcher ^ patternmatcher )
            // src/DSL.g:49:17: patternmatcher boolMatcher ^ patternmatcher
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_patternmatcher_in_complexmatcher184);
            patternmatcher20=patternmatcher();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, patternmatcher20.getTree());

            pushFollow(FOLLOW_boolMatcher_in_complexmatcher186);
            boolMatcher21=boolMatcher();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(boolMatcher21.getTree(), root_0);

            pushFollow(FOLLOW_patternmatcher_in_complexmatcher189);
            patternmatcher22=patternmatcher();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, patternmatcher22.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "complexmatcher"


    public static class filter_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "filter"
    // src/DSL.g:50:1: filter : 'where' ! '{' ! rule ^ '}' !;
    public final DSLParser.filter_return filter() throws RecognitionException {
        DSLParser.filter_return retval = new DSLParser.filter_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal23=null;
        Token char_literal24=null;
        Token char_literal26=null;
        DSLParser.rule_return rule25 =null;


        Object string_literal23_tree=null;
        Object char_literal24_tree=null;
        Object char_literal26_tree=null;

        try {
            // src/DSL.g:50:7: ( 'where' ! '{' ! rule ^ '}' !)
            // src/DSL.g:50:9: 'where' ! '{' ! rule ^ '}' !
            {
            root_0 = (Object)adaptor.nil();


            string_literal23=(Token)match(input,36,FOLLOW_36_in_filter195); if (state.failed) return retval;

            char_literal24=(Token)match(input,38,FOLLOW_38_in_filter198); if (state.failed) return retval;

            pushFollow(FOLLOW_rule_in_filter201);
            rule25=rule();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(rule25.getTree(), root_0);

            char_literal26=(Token)match(input,39,FOLLOW_39_in_filter204); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "filter"


    public static class rule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "rule"
    // src/DSL.g:51:1: rule : ( simplerule | complexrule );
    public final DSLParser.rule_return rule() throws RecognitionException {
        DSLParser.rule_return retval = new DSLParser.rule_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        DSLParser.simplerule_return simplerule27 =null;

        DSLParser.complexrule_return complexrule28 =null;



        try {
            // src/DSL.g:51:5: ( simplerule | complexrule )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==ATTR) ) {
                switch ( input.LA(2) ) {
                case EQ:
                case NE:
                    {
                    switch ( input.LA(3) ) {
                    case BOOLEAN:
                        {
                        int LA6_10 = input.LA(4);

                        if ( (LA6_10==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_10==29||LA6_10==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 10, input);

                            throw nvae;

                        }
                        }
                        break;
                    case DECIMAL:
                        {
                        int LA6_11 = input.LA(4);

                        if ( (LA6_11==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_11==29||LA6_11==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 11, input);

                            throw nvae;

                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA6_12 = input.LA(4);

                        if ( (LA6_12==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_12==29||LA6_12==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 12, input);

                            throw nvae;

                        }
                        }
                        break;
                    case INT:
                        {
                        int LA6_13 = input.LA(4);

                        if ( (LA6_13==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_13==29||LA6_13==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 13, input);

                            throw nvae;

                        }
                        }
                        break;
                    case PATTERN:
                        {
                        int LA6_14 = input.LA(4);

                        if ( (LA6_14==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_14==29||LA6_14==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 14, input);

                            throw nvae;

                        }
                        }
                        break;
                    case ATTR:
                        {
                        int LA6_15 = input.LA(4);

                        if ( (LA6_15==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_15==29||LA6_15==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 15, input);

                            throw nvae;

                        }
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 3, input);

                        throw nvae;

                    }

                    }
                    break;
                case LT:
                    {
                    int LA6_4 = input.LA(3);

                    if ( (LA6_4==DECIMAL) ) {
                        int LA6_11 = input.LA(4);

                        if ( (LA6_11==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_11==29||LA6_11==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 11, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA6_4==INT) ) {
                        int LA6_13 = input.LA(4);

                        if ( (LA6_13==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_13==29||LA6_13==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 13, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 4, input);

                        throw nvae;

                    }
                    }
                    break;
                case GT:
                    {
                    int LA6_5 = input.LA(3);

                    if ( (LA6_5==DECIMAL) ) {
                        int LA6_11 = input.LA(4);

                        if ( (LA6_11==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_11==29||LA6_11==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 11, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA6_5==INT) ) {
                        int LA6_13 = input.LA(4);

                        if ( (LA6_13==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_13==29||LA6_13==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 13, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 5, input);

                        throw nvae;

                    }
                    }
                    break;
                case GE:
                    {
                    int LA6_6 = input.LA(3);

                    if ( (LA6_6==DECIMAL) ) {
                        int LA6_11 = input.LA(4);

                        if ( (LA6_11==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_11==29||LA6_11==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 11, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA6_6==INT) ) {
                        int LA6_13 = input.LA(4);

                        if ( (LA6_13==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_13==29||LA6_13==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 13, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 6, input);

                        throw nvae;

                    }
                    }
                    break;
                case LE:
                    {
                    int LA6_7 = input.LA(3);

                    if ( (LA6_7==DECIMAL) ) {
                        int LA6_11 = input.LA(4);

                        if ( (LA6_11==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_11==29||LA6_11==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 11, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA6_7==INT) ) {
                        int LA6_13 = input.LA(4);

                        if ( (LA6_13==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_13==29||LA6_13==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 13, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 7, input);

                        throw nvae;

                    }
                    }
                    break;
                case REG:
                    {
                    int LA6_8 = input.LA(3);

                    if ( (LA6_8==STRING) ) {
                        int LA6_12 = input.LA(4);

                        if ( (LA6_12==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_12==29||LA6_12==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 12, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 8, input);

                        throw nvae;

                    }
                    }
                    break;
                case GLOB:
                    {
                    int LA6_9 = input.LA(3);

                    if ( (LA6_9==STRING) ) {
                        int LA6_12 = input.LA(4);

                        if ( (LA6_12==39) ) {
                            alt6=1;
                        }
                        else if ( (LA6_12==29||LA6_12==34) ) {
                            alt6=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 12, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 9, input);

                        throw nvae;

                    }
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }

            }
            else if ( (LA6_0==27) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // src/DSL.g:51:7: simplerule
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_simplerule_in_rule211);
                    simplerule27=simplerule();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simplerule27.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:51:20: complexrule
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_complexrule_in_rule215);
                    complexrule28=complexrule();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, complexrule28.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rule"


    public static class mixedRule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mixedRule"
    // src/DSL.g:52:1: mixedRule : ( simplerule | ( '(' ! complexrule ')' !) );
    public final DSLParser.mixedRule_return mixedRule() throws RecognitionException {
        DSLParser.mixedRule_return retval = new DSLParser.mixedRule_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal30=null;
        Token char_literal32=null;
        DSLParser.simplerule_return simplerule29 =null;

        DSLParser.complexrule_return complexrule31 =null;


        Object char_literal30_tree=null;
        Object char_literal32_tree=null;

        try {
            // src/DSL.g:52:10: ( simplerule | ( '(' ! complexrule ')' !) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ATTR) ) {
                alt7=1;
            }
            else if ( (LA7_0==27) ) {
                alt7=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // src/DSL.g:52:12: simplerule
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_simplerule_in_mixedRule221);
                    simplerule29=simplerule();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simplerule29.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:52:25: ( '(' ! complexrule ')' !)
                    {
                    root_0 = (Object)adaptor.nil();


                    // src/DSL.g:52:25: ( '(' ! complexrule ')' !)
                    // src/DSL.g:52:26: '(' ! complexrule ')' !
                    {
                    char_literal30=(Token)match(input,27,FOLLOW_27_in_mixedRule226); if (state.failed) return retval;

                    pushFollow(FOLLOW_complexrule_in_mixedRule229);
                    complexrule31=complexrule();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, complexrule31.getTree());

                    char_literal32=(Token)match(input,28,FOLLOW_28_in_mixedRule231); if (state.failed) return retval;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mixedRule"


    public static class simplerule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "simplerule"
    // src/DSL.g:54:1: simplerule : ( attrvalbool | attrvaldecimal | attrvalstr | attrvalint | attrvaldef | attrvalattr );
    public final DSLParser.simplerule_return simplerule() throws RecognitionException {
        DSLParser.simplerule_return retval = new DSLParser.simplerule_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        DSLParser.attrvalbool_return attrvalbool33 =null;

        DSLParser.attrvaldecimal_return attrvaldecimal34 =null;

        DSLParser.attrvalstr_return attrvalstr35 =null;

        DSLParser.attrvalint_return attrvalint36 =null;

        DSLParser.attrvaldef_return attrvaldef37 =null;

        DSLParser.attrvalattr_return attrvalattr38 =null;



        try {
            // src/DSL.g:54:11: ( attrvalbool | attrvaldecimal | attrvalstr | attrvalint | attrvaldef | attrvalattr )
            int alt8=6;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ATTR) ) {
                switch ( input.LA(2) ) {
                case EQ:
                case NE:
                    {
                    switch ( input.LA(3) ) {
                    case BOOLEAN:
                        {
                        alt8=1;
                        }
                        break;
                    case DECIMAL:
                        {
                        alt8=2;
                        }
                        break;
                    case STRING:
                        {
                        alt8=3;
                        }
                        break;
                    case INT:
                        {
                        alt8=4;
                        }
                        break;
                    case PATTERN:
                        {
                        alt8=5;
                        }
                        break;
                    case ATTR:
                        {
                        alt8=6;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 2, input);

                        throw nvae;

                    }

                    }
                    break;
                case LT:
                    {
                    int LA8_3 = input.LA(3);

                    if ( (LA8_3==DECIMAL) ) {
                        alt8=2;
                    }
                    else if ( (LA8_3==INT) ) {
                        alt8=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 3, input);

                        throw nvae;

                    }
                    }
                    break;
                case GT:
                    {
                    int LA8_4 = input.LA(3);

                    if ( (LA8_4==DECIMAL) ) {
                        alt8=2;
                    }
                    else if ( (LA8_4==INT) ) {
                        alt8=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 4, input);

                        throw nvae;

                    }
                    }
                    break;
                case GE:
                    {
                    int LA8_5 = input.LA(3);

                    if ( (LA8_5==DECIMAL) ) {
                        alt8=2;
                    }
                    else if ( (LA8_5==INT) ) {
                        alt8=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 5, input);

                        throw nvae;

                    }
                    }
                    break;
                case LE:
                    {
                    int LA8_6 = input.LA(3);

                    if ( (LA8_6==DECIMAL) ) {
                        alt8=2;
                    }
                    else if ( (LA8_6==INT) ) {
                        alt8=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 6, input);

                        throw nvae;

                    }
                    }
                    break;
                case GLOB:
                case REG:
                    {
                    alt8=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // src/DSL.g:54:13: attrvalbool
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attrvalbool_in_simplerule240);
                    attrvalbool33=attrvalbool();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attrvalbool33.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:54:27: attrvaldecimal
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attrvaldecimal_in_simplerule244);
                    attrvaldecimal34=attrvaldecimal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attrvaldecimal34.getTree());

                    }
                    break;
                case 3 :
                    // src/DSL.g:54:44: attrvalstr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attrvalstr_in_simplerule248);
                    attrvalstr35=attrvalstr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attrvalstr35.getTree());

                    }
                    break;
                case 4 :
                    // src/DSL.g:54:57: attrvalint
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attrvalint_in_simplerule252);
                    attrvalint36=attrvalint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attrvalint36.getTree());

                    }
                    break;
                case 5 :
                    // src/DSL.g:54:70: attrvaldef
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attrvaldef_in_simplerule256);
                    attrvaldef37=attrvaldef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attrvaldef37.getTree());

                    }
                    break;
                case 6 :
                    // src/DSL.g:54:83: attrvalattr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attrvalattr_in_simplerule260);
                    attrvalattr38=attrvalattr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attrvalattr38.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "simplerule"


    public static class complexrule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "complexrule"
    // src/DSL.g:55:1: complexrule : mixedRule bool ^ mixedRule ;
    public final DSLParser.complexrule_return complexrule() throws RecognitionException {
        DSLParser.complexrule_return retval = new DSLParser.complexrule_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        DSLParser.mixedRule_return mixedRule39 =null;

        DSLParser.bool_return bool40 =null;

        DSLParser.mixedRule_return mixedRule41 =null;



        try {
            // src/DSL.g:55:12: ( mixedRule bool ^ mixedRule )
            // src/DSL.g:55:14: mixedRule bool ^ mixedRule
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_mixedRule_in_complexrule266);
            mixedRule39=mixedRule();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mixedRule39.getTree());

            pushFollow(FOLLOW_bool_in_complexrule268);
            bool40=bool();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(bool40.getTree(), root_0);

            pushFollow(FOLLOW_mixedRule_in_complexrule271);
            mixedRule41=mixedRule();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mixedRule41.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "complexrule"


    public static class boolMatcher_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "boolMatcher"
    // src/DSL.g:56:1: boolMatcher : ( bool | 'not' | 'xor' );
    public final DSLParser.boolMatcher_return boolMatcher() throws RecognitionException {
        DSLParser.boolMatcher_return retval = new DSLParser.boolMatcher_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal43=null;
        Token string_literal44=null;
        DSLParser.bool_return bool42 =null;


        Object string_literal43_tree=null;
        Object string_literal44_tree=null;

        try {
            // src/DSL.g:56:13: ( bool | 'not' | 'xor' )
            int alt9=3;
            switch ( input.LA(1) ) {
            case 29:
            case 34:
                {
                alt9=1;
                }
                break;
            case 33:
                {
                alt9=2;
                }
                break;
            case 37:
                {
                alt9=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }

            switch (alt9) {
                case 1 :
                    // src/DSL.g:56:15: bool
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_bool_in_boolMatcher278);
                    bool42=bool();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, bool42.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:56:22: 'not'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal43=(Token)match(input,33,FOLLOW_33_in_boolMatcher282); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal43_tree = 
                    (Object)adaptor.create(string_literal43)
                    ;
                    adaptor.addChild(root_0, string_literal43_tree);
                    }

                    }
                    break;
                case 3 :
                    // src/DSL.g:56:30: 'xor'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal44=(Token)match(input,37,FOLLOW_37_in_boolMatcher286); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal44_tree = 
                    (Object)adaptor.create(string_literal44)
                    ;
                    adaptor.addChild(root_0, string_literal44_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "boolMatcher"


    public static class bool_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "bool"
    // src/DSL.g:57:1: bool : ( 'and' | 'or' );
    public final DSLParser.bool_return bool() throws RecognitionException {
        DSLParser.bool_return retval = new DSLParser.bool_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set45=null;

        Object set45_tree=null;

        try {
            // src/DSL.g:57:6: ( 'and' | 'or' )
            // src/DSL.g:
            {
            root_0 = (Object)adaptor.nil();


            set45=(Token)input.LT(1);

            if ( input.LA(1)==29||input.LA(1)==34 ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set45)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "bool"


    public static class attrvalbool_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attrvalbool"
    // src/DSL.g:58:1: attrvalbool : ATTR opereq BOOLEAN -> ^( ATTR ^( opereq BOOLEAN ) ) ;
    public final DSLParser.attrvalbool_return attrvalbool() throws RecognitionException {
        DSLParser.attrvalbool_return retval = new DSLParser.attrvalbool_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR46=null;
        Token BOOLEAN48=null;
        DSLParser.opereq_return opereq47 =null;


        Object ATTR46_tree=null;
        Object BOOLEAN48_tree=null;
        RewriteRuleTokenStream stream_BOOLEAN=new RewriteRuleTokenStream(adaptor,"token BOOLEAN");
        RewriteRuleTokenStream stream_ATTR=new RewriteRuleTokenStream(adaptor,"token ATTR");
        RewriteRuleSubtreeStream stream_opereq=new RewriteRuleSubtreeStream(adaptor,"rule opereq");
        try {
            // src/DSL.g:58:12: ( ATTR opereq BOOLEAN -> ^( ATTR ^( opereq BOOLEAN ) ) )
            // src/DSL.g:58:14: ATTR opereq BOOLEAN
            {
            ATTR46=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvalbool304); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR46);


            pushFollow(FOLLOW_opereq_in_attrvalbool306);
            opereq47=opereq();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_opereq.add(opereq47.getTree());

            BOOLEAN48=(Token)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_attrvalbool308); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BOOLEAN.add(BOOLEAN48);


            // AST REWRITE
            // elements: BOOLEAN, opereq, ATTR
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 58:34: -> ^( ATTR ^( opereq BOOLEAN ) )
            {
                // src/DSL.g:58:37: ^( ATTR ^( opereq BOOLEAN ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_ATTR.nextNode()
                , root_1);

                // src/DSL.g:58:44: ^( opereq BOOLEAN )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(stream_opereq.nextNode(), root_2);

                adaptor.addChild(root_2, 
                stream_BOOLEAN.nextNode()
                );

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrvalbool"


    public static class attrvaldecimal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attrvaldecimal"
    // src/DSL.g:59:1: attrvaldecimal : ATTR operint DECIMAL -> ^( ATTR ^( operint DECIMAL ) ) ;
    public final DSLParser.attrvaldecimal_return attrvaldecimal() throws RecognitionException {
        DSLParser.attrvaldecimal_return retval = new DSLParser.attrvaldecimal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR49=null;
        Token DECIMAL51=null;
        DSLParser.operint_return operint50 =null;


        Object ATTR49_tree=null;
        Object DECIMAL51_tree=null;
        RewriteRuleTokenStream stream_DECIMAL=new RewriteRuleTokenStream(adaptor,"token DECIMAL");
        RewriteRuleTokenStream stream_ATTR=new RewriteRuleTokenStream(adaptor,"token ATTR");
        RewriteRuleSubtreeStream stream_operint=new RewriteRuleSubtreeStream(adaptor,"rule operint");
        try {
            // src/DSL.g:59:15: ( ATTR operint DECIMAL -> ^( ATTR ^( operint DECIMAL ) ) )
            // src/DSL.g:59:17: ATTR operint DECIMAL
            {
            ATTR49=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvaldecimal327); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR49);


            pushFollow(FOLLOW_operint_in_attrvaldecimal329);
            operint50=operint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_operint.add(operint50.getTree());

            DECIMAL51=(Token)match(input,DECIMAL,FOLLOW_DECIMAL_in_attrvaldecimal331); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DECIMAL.add(DECIMAL51);


            // AST REWRITE
            // elements: ATTR, DECIMAL, operint
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 59:38: -> ^( ATTR ^( operint DECIMAL ) )
            {
                // src/DSL.g:59:41: ^( ATTR ^( operint DECIMAL ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_ATTR.nextNode()
                , root_1);

                // src/DSL.g:59:48: ^( operint DECIMAL )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(stream_operint.nextNode(), root_2);

                adaptor.addChild(root_2, 
                stream_DECIMAL.nextNode()
                );

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrvaldecimal"


    public static class attrvalstr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attrvalstr"
    // src/DSL.g:60:1: attrvalstr : ATTR operstr STRING -> ^( ATTR ^( operstr STRING ) ) ;
    public final DSLParser.attrvalstr_return attrvalstr() throws RecognitionException {
        DSLParser.attrvalstr_return retval = new DSLParser.attrvalstr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR52=null;
        Token STRING54=null;
        DSLParser.operstr_return operstr53 =null;


        Object ATTR52_tree=null;
        Object STRING54_tree=null;
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");
        RewriteRuleTokenStream stream_ATTR=new RewriteRuleTokenStream(adaptor,"token ATTR");
        RewriteRuleSubtreeStream stream_operstr=new RewriteRuleSubtreeStream(adaptor,"rule operstr");
        try {
            // src/DSL.g:60:11: ( ATTR operstr STRING -> ^( ATTR ^( operstr STRING ) ) )
            // src/DSL.g:60:13: ATTR operstr STRING
            {
            ATTR52=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvalstr350); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR52);


            pushFollow(FOLLOW_operstr_in_attrvalstr352);
            operstr53=operstr();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_operstr.add(operstr53.getTree());

            STRING54=(Token)match(input,STRING,FOLLOW_STRING_in_attrvalstr354); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STRING.add(STRING54);


            // AST REWRITE
            // elements: STRING, ATTR, operstr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 60:33: -> ^( ATTR ^( operstr STRING ) )
            {
                // src/DSL.g:60:36: ^( ATTR ^( operstr STRING ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_ATTR.nextNode()
                , root_1);

                // src/DSL.g:60:43: ^( operstr STRING )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(stream_operstr.nextNode(), root_2);

                adaptor.addChild(root_2, 
                stream_STRING.nextNode()
                );

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrvalstr"


    public static class attrvalint_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attrvalint"
    // src/DSL.g:61:1: attrvalint : ATTR operint INT -> ^( ATTR ^( operint INT ) ) ;
    public final DSLParser.attrvalint_return attrvalint() throws RecognitionException {
        DSLParser.attrvalint_return retval = new DSLParser.attrvalint_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR55=null;
        Token INT57=null;
        DSLParser.operint_return operint56 =null;


        Object ATTR55_tree=null;
        Object INT57_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_ATTR=new RewriteRuleTokenStream(adaptor,"token ATTR");
        RewriteRuleSubtreeStream stream_operint=new RewriteRuleSubtreeStream(adaptor,"rule operint");
        try {
            // src/DSL.g:61:11: ( ATTR operint INT -> ^( ATTR ^( operint INT ) ) )
            // src/DSL.g:61:13: ATTR operint INT
            {
            ATTR55=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvalint372); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR55);


            pushFollow(FOLLOW_operint_in_attrvalint374);
            operint56=operint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_operint.add(operint56.getTree());

            INT57=(Token)match(input,INT,FOLLOW_INT_in_attrvalint376); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_INT.add(INT57);


            // AST REWRITE
            // elements: INT, operint, ATTR
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 61:30: -> ^( ATTR ^( operint INT ) )
            {
                // src/DSL.g:61:33: ^( ATTR ^( operint INT ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_ATTR.nextNode()
                , root_1);

                // src/DSL.g:61:40: ^( operint INT )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(stream_operint.nextNode(), root_2);

                adaptor.addChild(root_2, 
                stream_INT.nextNode()
                );

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrvalint"


    public static class attrvalattr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attrvalattr"
    // src/DSL.g:62:1: attrvalattr : ATTR opereq ATTR -> ^( ATTR ^( opereq ATTR ) ) ;
    public final DSLParser.attrvalattr_return attrvalattr() throws RecognitionException {
        DSLParser.attrvalattr_return retval = new DSLParser.attrvalattr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR58=null;
        Token ATTR60=null;
        DSLParser.opereq_return opereq59 =null;


        Object ATTR58_tree=null;
        Object ATTR60_tree=null;
        RewriteRuleTokenStream stream_ATTR=new RewriteRuleTokenStream(adaptor,"token ATTR");
        RewriteRuleSubtreeStream stream_opereq=new RewriteRuleSubtreeStream(adaptor,"rule opereq");
        try {
            // src/DSL.g:62:12: ( ATTR opereq ATTR -> ^( ATTR ^( opereq ATTR ) ) )
            // src/DSL.g:62:14: ATTR opereq ATTR
            {
            ATTR58=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvalattr394); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR58);


            pushFollow(FOLLOW_opereq_in_attrvalattr396);
            opereq59=opereq();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_opereq.add(opereq59.getTree());

            ATTR60=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvalattr398); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR60);


            // AST REWRITE
            // elements: ATTR, opereq, ATTR
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 62:31: -> ^( ATTR ^( opereq ATTR ) )
            {
                // src/DSL.g:62:34: ^( ATTR ^( opereq ATTR ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_ATTR.nextNode()
                , root_1);

                // src/DSL.g:62:41: ^( opereq ATTR )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(stream_opereq.nextNode(), root_2);

                adaptor.addChild(root_2, 
                stream_ATTR.nextNode()
                );

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrvalattr"


    public static class attrvaldef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attrvaldef"
    // src/DSL.g:63:1: attrvaldef : ATTR opereq PATTERN -> ^( ATTR ^( opereq PATTERN ) ) ;
    public final DSLParser.attrvaldef_return attrvaldef() throws RecognitionException {
        DSLParser.attrvaldef_return retval = new DSLParser.attrvaldef_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR61=null;
        Token PATTERN63=null;
        DSLParser.opereq_return opereq62 =null;


        Object ATTR61_tree=null;
        Object PATTERN63_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleTokenStream stream_ATTR=new RewriteRuleTokenStream(adaptor,"token ATTR");
        RewriteRuleSubtreeStream stream_opereq=new RewriteRuleSubtreeStream(adaptor,"rule opereq");
        try {
            // src/DSL.g:63:11: ( ATTR opereq PATTERN -> ^( ATTR ^( opereq PATTERN ) ) )
            // src/DSL.g:63:13: ATTR opereq PATTERN
            {
            ATTR61=(Token)match(input,ATTR,FOLLOW_ATTR_in_attrvaldef416); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ATTR.add(ATTR61);


            pushFollow(FOLLOW_opereq_in_attrvaldef418);
            opereq62=opereq();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_opereq.add(opereq62.getTree());

            PATTERN63=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_attrvaldef420); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN63);


            // AST REWRITE
            // elements: ATTR, PATTERN, opereq
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 63:33: -> ^( ATTR ^( opereq PATTERN ) )
            {
                // src/DSL.g:63:36: ^( ATTR ^( opereq PATTERN ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                stream_ATTR.nextNode()
                , root_1);

                // src/DSL.g:63:43: ^( opereq PATTERN )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(stream_opereq.nextNode(), root_2);

                adaptor.addChild(root_2, 
                stream_PATTERN.nextNode()
                );

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrvaldef"


    public static class opereq_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "opereq"
    // src/DSL.g:65:1: opereq : ( EQ | NE );
    public final DSLParser.opereq_return opereq() throws RecognitionException {
        DSLParser.opereq_return retval = new DSLParser.opereq_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set64=null;

        Object set64_tree=null;

        try {
            // src/DSL.g:65:7: ( EQ | NE )
            // src/DSL.g:
            {
            root_0 = (Object)adaptor.nil();


            set64=(Token)input.LT(1);

            if ( input.LA(1)==EQ||input.LA(1)==NE ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set64)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "opereq"


    public static class operint_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "operint"
    // src/DSL.g:66:1: operint : ( opereq | LT | GT | GE | LE );
    public final DSLParser.operint_return operint() throws RecognitionException {
        DSLParser.operint_return retval = new DSLParser.operint_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LT66=null;
        Token GT67=null;
        Token GE68=null;
        Token LE69=null;
        DSLParser.opereq_return opereq65 =null;


        Object LT66_tree=null;
        Object GT67_tree=null;
        Object GE68_tree=null;
        Object LE69_tree=null;

        try {
            // src/DSL.g:66:8: ( opereq | LT | GT | GE | LE )
            int alt10=5;
            switch ( input.LA(1) ) {
            case EQ:
            case NE:
                {
                alt10=1;
                }
                break;
            case LT:
                {
                alt10=2;
                }
                break;
            case GT:
                {
                alt10=3;
                }
                break;
            case GE:
                {
                alt10=4;
                }
                break;
            case LE:
                {
                alt10=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // src/DSL.g:66:10: opereq
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_opereq_in_operint449);
                    opereq65=opereq();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, opereq65.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:66:19: LT
                    {
                    root_0 = (Object)adaptor.nil();


                    LT66=(Token)match(input,LT,FOLLOW_LT_in_operint453); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LT66_tree = 
                    (Object)adaptor.create(LT66)
                    ;
                    adaptor.addChild(root_0, LT66_tree);
                    }

                    }
                    break;
                case 3 :
                    // src/DSL.g:66:24: GT
                    {
                    root_0 = (Object)adaptor.nil();


                    GT67=(Token)match(input,GT,FOLLOW_GT_in_operint457); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GT67_tree = 
                    (Object)adaptor.create(GT67)
                    ;
                    adaptor.addChild(root_0, GT67_tree);
                    }

                    }
                    break;
                case 4 :
                    // src/DSL.g:66:29: GE
                    {
                    root_0 = (Object)adaptor.nil();


                    GE68=(Token)match(input,GE,FOLLOW_GE_in_operint461); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GE68_tree = 
                    (Object)adaptor.create(GE68)
                    ;
                    adaptor.addChild(root_0, GE68_tree);
                    }

                    }
                    break;
                case 5 :
                    // src/DSL.g:66:34: LE
                    {
                    root_0 = (Object)adaptor.nil();


                    LE69=(Token)match(input,LE,FOLLOW_LE_in_operint465); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LE69_tree = 
                    (Object)adaptor.create(LE69)
                    ;
                    adaptor.addChild(root_0, LE69_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "operint"


    public static class operstr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "operstr"
    // src/DSL.g:67:1: operstr : ( opereq | REG | GLOB );
    public final DSLParser.operstr_return operstr() throws RecognitionException {
        DSLParser.operstr_return retval = new DSLParser.operstr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token REG71=null;
        Token GLOB72=null;
        DSLParser.opereq_return opereq70 =null;


        Object REG71_tree=null;
        Object GLOB72_tree=null;

        try {
            // src/DSL.g:67:8: ( opereq | REG | GLOB )
            int alt11=3;
            switch ( input.LA(1) ) {
            case EQ:
            case NE:
                {
                alt11=1;
                }
                break;
            case REG:
                {
                alt11=2;
                }
                break;
            case GLOB:
                {
                alt11=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // src/DSL.g:67:10: opereq
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_opereq_in_operstr472);
                    opereq70=opereq();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, opereq70.getTree());

                    }
                    break;
                case 2 :
                    // src/DSL.g:67:19: REG
                    {
                    root_0 = (Object)adaptor.nil();


                    REG71=(Token)match(input,REG,FOLLOW_REG_in_operstr476); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    REG71_tree = 
                    (Object)adaptor.create(REG71)
                    ;
                    adaptor.addChild(root_0, REG71_tree);
                    }

                    }
                    break;
                case 3 :
                    // src/DSL.g:67:25: GLOB
                    {
                    root_0 = (Object)adaptor.nil();


                    GLOB72=(Token)match(input,GLOB,FOLLOW_GLOB_in_operstr480); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GLOB72_tree = 
                    (Object)adaptor.create(GLOB72)
                    ;
                    adaptor.addChild(root_0, GLOB72_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "operstr"


    public static class mapping_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mapping"
    // src/DSL.g:80:1: mapping : ID ;
    public final DSLParser.mapping_return mapping() throws RecognitionException {
        DSLParser.mapping_return retval = new DSLParser.mapping_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ID73=null;

        Object ID73_tree=null;

        try {
            // src/DSL.g:80:9: ( ID )
            // src/DSL.g:80:11: ID
            {
            root_0 = (Object)adaptor.nil();


            ID73=(Token)match(input,ID,FOLLOW_ID_in_mapping583); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID73_tree = 
            (Object)adaptor.create(ID73)
            ;
            adaptor.addChild(root_0, ID73_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mapping"

    // Delegated rules


 

    public static final BitSet FOLLOW_35_in_patterns78 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_ID_in_patterns82 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_define_in_patterns85 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_find_in_patterns87 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_EOF_in_patterns91 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_find102 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_PATTERN_in_find104 = new BitSet(new long[]{0x0000001000080002L});
    public static final BitSet FOLLOW_MAPPING_in_find106 = new BitSet(new long[]{0x0000001000080002L});
    public static final BitSet FOLLOW_filter_in_find109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_define132 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_PATTERN_in_define134 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_define136 = new BitSet(new long[]{0x0000004000400000L});
    public static final BitSet FOLLOW_patternmatcher_in_define138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_simplematcher156 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_filter_in_simplematcher159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simplematcher_in_patternmatcher166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_patternmatcher171 = new BitSet(new long[]{0x0000004000400000L});
    public static final BitSet FOLLOW_complexmatcher_in_patternmatcher174 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_patternmatcher176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternmatcher_in_complexmatcher184 = new BitSet(new long[]{0x0000002620000000L});
    public static final BitSet FOLLOW_boolMatcher_in_complexmatcher186 = new BitSet(new long[]{0x0000004000400000L});
    public static final BitSet FOLLOW_patternmatcher_in_complexmatcher189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_filter195 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_filter198 = new BitSet(new long[]{0x0000000008000010L});
    public static final BitSet FOLLOW_rule_in_filter201 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_filter204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simplerule_in_rule211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complexrule_in_rule215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simplerule_in_mixedRule221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_mixedRule226 = new BitSet(new long[]{0x0000000008000010L});
    public static final BitSet FOLLOW_complexrule_in_mixedRule229 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_mixedRule231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attrvalbool_in_simplerule240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attrvaldecimal_in_simplerule244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attrvalstr_in_simplerule248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attrvalint_in_simplerule252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attrvaldef_in_simplerule256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attrvalattr_in_simplerule260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mixedRule_in_complexrule266 = new BitSet(new long[]{0x0000000420000000L});
    public static final BitSet FOLLOW_bool_in_complexrule268 = new BitSet(new long[]{0x0000000008000010L});
    public static final BitSet FOLLOW_mixedRule_in_complexrule271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bool_in_boolMatcher278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_boolMatcher282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_boolMatcher286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attrvalbool304 = new BitSet(new long[]{0x0000000000100100L});
    public static final BitSet FOLLOW_opereq_in_attrvalbool306 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_BOOLEAN_in_attrvalbool308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attrvaldecimal327 = new BitSet(new long[]{0x0000000000162900L});
    public static final BitSet FOLLOW_operint_in_attrvaldecimal329 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_DECIMAL_in_attrvaldecimal331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attrvalstr350 = new BitSet(new long[]{0x0000000000901100L});
    public static final BitSet FOLLOW_operstr_in_attrvalstr352 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_STRING_in_attrvalstr354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attrvalint372 = new BitSet(new long[]{0x0000000000162900L});
    public static final BitSet FOLLOW_operint_in_attrvalint374 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_INT_in_attrvalint376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attrvalattr394 = new BitSet(new long[]{0x0000000000100100L});
    public static final BitSet FOLLOW_opereq_in_attrvalattr396 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ATTR_in_attrvalattr398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attrvaldef416 = new BitSet(new long[]{0x0000000000100100L});
    public static final BitSet FOLLOW_opereq_in_attrvaldef418 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_PATTERN_in_attrvaldef420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opereq_in_operint449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_operint453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_operint457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GE_in_operint461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LE_in_operint465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opereq_in_operstr472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REG_in_operstr476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GLOB_in_operstr480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_mapping583 = new BitSet(new long[]{0x0000000000000002L});

}