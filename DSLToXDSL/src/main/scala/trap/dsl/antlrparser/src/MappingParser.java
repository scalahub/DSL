// $ANTLR 3.4 src/Mapping.g 2017-04-26 11:47:58
package trap.dsl.antlrparser.src;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class MappingParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AGGR", "ATTR", "COMMENT", "COUNT", "ESC_SEQ", "EXPONENT", "HEX_DIGIT", "ID", "INT", "KEY", "MAP", "OCTAL_ESC", "PATTERN", "STRING", "UNICODE_ESC", "WS", "','", "'.'", "';'", "'=>'", "'as'", "'map'", "'mappings'"
    };

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

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public MappingParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public MappingParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return MappingParser.tokenNames; }
    public String getGrammarFileName() { return "src/Mapping.g"; }

     
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


    public static class mappings_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mappings"
    // src/Mapping.g:45:1: mappings : 'mappings' ! ID ^ ( mapping )+ EOF !;
    public final MappingParser.mappings_return mappings() throws RecognitionException {
        MappingParser.mappings_return retval = new MappingParser.mappings_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal1=null;
        Token ID2=null;
        Token EOF4=null;
        MappingParser.mapping_return mapping3 =null;


        Object string_literal1_tree=null;
        Object ID2_tree=null;
        Object EOF4_tree=null;

        try {
            // src/Mapping.g:45:9: ( 'mappings' ! ID ^ ( mapping )+ EOF !)
            // src/Mapping.g:45:11: 'mappings' ! ID ^ ( mapping )+ EOF !
            {
            root_0 = (Object)adaptor.nil();


            string_literal1=(Token)match(input,26,FOLLOW_26_in_mappings78); if (state.failed) return retval;

            ID2=(Token)match(input,ID,FOLLOW_ID_in_mappings81); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID2_tree = 
            (Object)adaptor.create(ID2)
            ;
            root_0 = (Object)adaptor.becomeRoot(ID2_tree, root_0);
            }

            // src/Mapping.g:45:28: ( mapping )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==25) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/Mapping.g:45:29: mapping
            	    {
            	    pushFollow(FOLLOW_mapping_in_mappings86);
            	    mapping3=mapping();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mapping3.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_mappings90); if (state.failed) return retval;

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
    // $ANTLR end "mappings"


    public static class mapping_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mapping"
    // src/Mapping.g:46:1: mapping : 'map' ! MAP ^ 'as' ! keyMaps ';' !;
    public final MappingParser.mapping_return mapping() throws RecognitionException {
        MappingParser.mapping_return retval = new MappingParser.mapping_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal5=null;
        Token MAP6=null;
        Token string_literal7=null;
        Token char_literal9=null;
        MappingParser.keyMaps_return keyMaps8 =null;


        Object string_literal5_tree=null;
        Object MAP6_tree=null;
        Object string_literal7_tree=null;
        Object char_literal9_tree=null;

        try {
            // src/Mapping.g:46:8: ( 'map' ! MAP ^ 'as' ! keyMaps ';' !)
            // src/Mapping.g:46:10: 'map' ! MAP ^ 'as' ! keyMaps ';' !
            {
            root_0 = (Object)adaptor.nil();


            string_literal5=(Token)match(input,25,FOLLOW_25_in_mapping97); if (state.failed) return retval;

            MAP6=(Token)match(input,MAP,FOLLOW_MAP_in_mapping100); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MAP6_tree = 
            (Object)adaptor.create(MAP6)
            ;
            root_0 = (Object)adaptor.becomeRoot(MAP6_tree, root_0);
            }

            string_literal7=(Token)match(input,24,FOLLOW_24_in_mapping103); if (state.failed) return retval;

            pushFollow(FOLLOW_keyMaps_in_mapping106);
            keyMaps8=keyMaps();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, keyMaps8.getTree());

            char_literal9=(Token)match(input,22,FOLLOW_22_in_mapping108); if (state.failed) return retval;

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


    public static class keyMaps_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "keyMaps"
    // src/Mapping.g:48:1: keyMaps : ( keyMap | ( keyMap ',' ! keyMaps ) );
    public final MappingParser.keyMaps_return keyMaps() throws RecognitionException {
        MappingParser.keyMaps_return retval = new MappingParser.keyMaps_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal12=null;
        MappingParser.keyMap_return keyMap10 =null;

        MappingParser.keyMap_return keyMap11 =null;

        MappingParser.keyMaps_return keyMaps13 =null;


        Object char_literal12_tree=null;

        try {
            // src/Mapping.g:48:8: ( keyMap | ( keyMap ',' ! keyMaps ) )
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // src/Mapping.g:48:10: keyMap
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_keyMap_in_keyMaps123);
                    keyMap10=keyMap();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, keyMap10.getTree());

                    }
                    break;
                case 2 :
                    // src/Mapping.g:48:19: ( keyMap ',' ! keyMaps )
                    {
                    root_0 = (Object)adaptor.nil();


                    // src/Mapping.g:48:19: ( keyMap ',' ! keyMaps )
                    // src/Mapping.g:48:20: keyMap ',' ! keyMaps
                    {
                    pushFollow(FOLLOW_keyMap_in_keyMaps128);
                    keyMap11=keyMap();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, keyMap11.getTree());

                    char_literal12=(Token)match(input,20,FOLLOW_20_in_keyMaps130); if (state.failed) return retval;

                    pushFollow(FOLLOW_keyMaps_in_keyMaps133);
                    keyMaps13=keyMaps();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, keyMaps13.getTree());

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
    // $ANTLR end "keyMaps"


    public static class keyMap_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "keyMap"
    // src/Mapping.g:49:1: keyMap : KEY ^ '=>' ! ( value )? ;
    public final MappingParser.keyMap_return keyMap() throws RecognitionException {
        MappingParser.keyMap_return retval = new MappingParser.keyMap_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token KEY14=null;
        Token string_literal15=null;
        MappingParser.value_return value16 =null;


        Object KEY14_tree=null;
        Object string_literal15_tree=null;

        try {
            // src/Mapping.g:49:7: ( KEY ^ '=>' ! ( value )? )
            // src/Mapping.g:49:10: KEY ^ '=>' ! ( value )?
            {
            root_0 = (Object)adaptor.nil();


            KEY14=(Token)match(input,KEY,FOLLOW_KEY_in_keyMap141); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            KEY14_tree = 
            (Object)adaptor.create(KEY14)
            ;
            root_0 = (Object)adaptor.becomeRoot(KEY14_tree, root_0);
            }

            string_literal15=(Token)match(input,23,FOLLOW_23_in_keyMap144); if (state.failed) return retval;

            // src/Mapping.g:49:21: ( value )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==COUNT||LA3_0==PATTERN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // src/Mapping.g:49:21: value
                    {
                    pushFollow(FOLLOW_value_in_keyMap147);
                    value16=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value16.getTree());

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
    // $ANTLR end "keyMap"


    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "value"
    // src/Mapping.g:52:1: value : ( COUNT | pattern );
    public final MappingParser.value_return value() throws RecognitionException {
        MappingParser.value_return retval = new MappingParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token COUNT17=null;
        MappingParser.pattern_return pattern18 =null;


        Object COUNT17_tree=null;

        try {
            // src/Mapping.g:52:7: ( COUNT | pattern )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==COUNT) ) {
                alt4=1;
            }
            else if ( (LA4_0==PATTERN) ) {
                alt4=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // src/Mapping.g:52:9: COUNT
                    {
                    root_0 = (Object)adaptor.nil();


                    COUNT17=(Token)match(input,COUNT,FOLLOW_COUNT_in_value157); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    COUNT17_tree = 
                    (Object)adaptor.create(COUNT17)
                    ;
                    adaptor.addChild(root_0, COUNT17_tree);
                    }

                    }
                    break;
                case 2 :
                    // src/Mapping.g:52:17: pattern
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_pattern_in_value161);
                    pattern18=pattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern18.getTree());

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
    // $ANTLR end "value"


    public static class pattern_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "pattern"
    // src/Mapping.g:54:1: pattern : PATTERN ^ ( '.' ! countOrAttribute ( ',' ! countOrAttribute )* )? ;
    public final MappingParser.pattern_return pattern() throws RecognitionException {
        MappingParser.pattern_return retval = new MappingParser.pattern_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PATTERN19=null;
        Token char_literal20=null;
        Token char_literal22=null;
        MappingParser.countOrAttribute_return countOrAttribute21 =null;

        MappingParser.countOrAttribute_return countOrAttribute23 =null;


        Object PATTERN19_tree=null;
        Object char_literal20_tree=null;
        Object char_literal22_tree=null;

        try {
            // src/Mapping.g:54:9: ( PATTERN ^ ( '.' ! countOrAttribute ( ',' ! countOrAttribute )* )? )
            // src/Mapping.g:54:11: PATTERN ^ ( '.' ! countOrAttribute ( ',' ! countOrAttribute )* )?
            {
            root_0 = (Object)adaptor.nil();


            PATTERN19=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_pattern169); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PATTERN19_tree = 
            (Object)adaptor.create(PATTERN19)
            ;
            root_0 = (Object)adaptor.becomeRoot(PATTERN19_tree, root_0);
            }

            // src/Mapping.g:54:20: ( '.' ! countOrAttribute ( ',' ! countOrAttribute )* )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==21) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // src/Mapping.g:54:21: '.' ! countOrAttribute ( ',' ! countOrAttribute )*
                    {
                    char_literal20=(Token)match(input,21,FOLLOW_21_in_pattern173); if (state.failed) return retval;

                    pushFollow(FOLLOW_countOrAttribute_in_pattern176);
                    countOrAttribute21=countOrAttribute();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, countOrAttribute21.getTree());

                    // src/Mapping.g:54:43: ( ',' ! countOrAttribute )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==20) ) {
                            int LA5_2 = input.LA(2);

                            if ( (LA5_2==ATTR||LA5_2==COUNT) ) {
                                alt5=1;
                            }


                        }


                        switch (alt5) {
                    	case 1 :
                    	    // src/Mapping.g:54:44: ',' ! countOrAttribute
                    	    {
                    	    char_literal22=(Token)match(input,20,FOLLOW_20_in_pattern179); if (state.failed) return retval;

                    	    pushFollow(FOLLOW_countOrAttribute_in_pattern182);
                    	    countOrAttribute23=countOrAttribute();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, countOrAttribute23.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


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
    // $ANTLR end "pattern"


    public static class countOrAttribute_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "countOrAttribute"
    // src/Mapping.g:55:1: countOrAttribute : ( COUNT | attribute );
    public final MappingParser.countOrAttribute_return countOrAttribute() throws RecognitionException {
        MappingParser.countOrAttribute_return retval = new MappingParser.countOrAttribute_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token COUNT24=null;
        MappingParser.attribute_return attribute25 =null;


        Object COUNT24_tree=null;

        try {
            // src/Mapping.g:55:17: ( COUNT | attribute )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==COUNT) ) {
                alt7=1;
            }
            else if ( (LA7_0==ATTR) ) {
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
                    // src/Mapping.g:55:19: COUNT
                    {
                    root_0 = (Object)adaptor.nil();


                    COUNT24=(Token)match(input,COUNT,FOLLOW_COUNT_in_countOrAttribute192); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    COUNT24_tree = 
                    (Object)adaptor.create(COUNT24)
                    ;
                    adaptor.addChild(root_0, COUNT24_tree);
                    }

                    }
                    break;
                case 2 :
                    // src/Mapping.g:55:27: attribute
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_attribute_in_countOrAttribute196);
                    attribute25=attribute();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attribute25.getTree());

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
    // $ANTLR end "countOrAttribute"


    public static class attribute_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attribute"
    // src/Mapping.g:56:1: attribute : ATTR ^ ( '.' ! countOrAggr )? ;
    public final MappingParser.attribute_return attribute() throws RecognitionException {
        MappingParser.attribute_return retval = new MappingParser.attribute_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token ATTR26=null;
        Token char_literal27=null;
        MappingParser.countOrAggr_return countOrAggr28 =null;


        Object ATTR26_tree=null;
        Object char_literal27_tree=null;

        try {
            // src/Mapping.g:56:10: ( ATTR ^ ( '.' ! countOrAggr )? )
            // src/Mapping.g:56:12: ATTR ^ ( '.' ! countOrAggr )?
            {
            root_0 = (Object)adaptor.nil();


            ATTR26=(Token)match(input,ATTR,FOLLOW_ATTR_in_attribute202); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ATTR26_tree = 
            (Object)adaptor.create(ATTR26)
            ;
            root_0 = (Object)adaptor.becomeRoot(ATTR26_tree, root_0);
            }

            // src/Mapping.g:56:18: ( '.' ! countOrAggr )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==21) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // src/Mapping.g:56:19: '.' ! countOrAggr
                    {
                    char_literal27=(Token)match(input,21,FOLLOW_21_in_attribute206); if (state.failed) return retval;

                    pushFollow(FOLLOW_countOrAggr_in_attribute209);
                    countOrAggr28=countOrAggr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, countOrAggr28.getTree());

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
    // $ANTLR end "attribute"


    public static class countOrAggr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "countOrAggr"
    // src/Mapping.g:57:1: countOrAggr : ( COUNT | AGGR );
    public final MappingParser.countOrAggr_return countOrAggr() throws RecognitionException {
        MappingParser.countOrAggr_return retval = new MappingParser.countOrAggr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set29=null;

        Object set29_tree=null;

        try {
            // src/Mapping.g:57:12: ( COUNT | AGGR )
            // src/Mapping.g:
            {
            root_0 = (Object)adaptor.nil();


            set29=(Token)input.LT(1);

            if ( input.LA(1)==AGGR||input.LA(1)==COUNT ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set29)
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
    // $ANTLR end "countOrAggr"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\21\uffff";
    static final String DFA2_eofS =
        "\21\uffff";
    static final String DFA2_minS =
        "\1\15\1\27\1\7\2\24\2\uffff\1\5\2\24\1\5\1\4\3\24\1\4\1\24";
    static final String DFA2_maxS =
        "\1\15\1\27\3\26\2\uffff\1\7\2\26\1\15\1\7\3\26\1\7\1\26";
    static final String DFA2_acceptS =
        "\5\uffff\1\1\1\2\12\uffff";
    static final String DFA2_specialS =
        "\21\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3\10\uffff\1\4\3\uffff\1\6\1\uffff\1\5",
            "\1\6\1\uffff\1\5",
            "\1\6\1\7\1\5",
            "",
            "",
            "\1\11\1\uffff\1\10",
            "\1\12\1\uffff\1\5",
            "\1\12\1\13\1\5",
            "\1\15\1\uffff\1\14\5\uffff\1\6",
            "\1\16\2\uffff\1\16",
            "\1\12\1\uffff\1\5",
            "\1\12\1\17\1\5",
            "\1\12\1\uffff\1\5",
            "\1\20\2\uffff\1\20",
            "\1\12\1\uffff\1\5"
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "48:1: keyMaps : ( keyMap | ( keyMap ',' ! keyMaps ) );";
        }
    }
 

    public static final BitSet FOLLOW_26_in_mappings78 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_ID_in_mappings81 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_mapping_in_mappings86 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_EOF_in_mappings90 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_mapping97 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_MAP_in_mapping100 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_mapping103 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_keyMaps_in_mapping106 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_mapping108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyMap_in_keyMaps123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyMap_in_keyMaps128 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_keyMaps130 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_keyMaps_in_keyMaps133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEY_in_keyMap141 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_keyMap144 = new BitSet(new long[]{0x0000000000010082L});
    public static final BitSet FOLLOW_value_in_keyMap147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_value157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_value161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_pattern169 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_pattern173 = new BitSet(new long[]{0x00000000000000A0L});
    public static final BitSet FOLLOW_countOrAttribute_in_pattern176 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_20_in_pattern179 = new BitSet(new long[]{0x00000000000000A0L});
    public static final BitSet FOLLOW_countOrAttribute_in_pattern182 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_COUNT_in_countOrAttribute192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attribute_in_countOrAttribute196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATTR_in_attribute202 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_attribute206 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_countOrAggr_in_attribute209 = new BitSet(new long[]{0x0000000000000002L});

}