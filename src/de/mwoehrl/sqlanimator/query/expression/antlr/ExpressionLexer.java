package de.mwoehrl.sqlanimator.query.expression.antlr;
// Generated from Expression.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, STRING=19, INT=20, AGGREGATE=21, NAME=22, WS=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "STRING", "INT", "AGGREGATE", "NAME", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'AS'", "'*'", "'/'", "'+'", "'-'", "'='", "'>'", 
			"'<'", "'>='", "'<='", "'<>'", "'LIKE'", "'AND'", "'OR'", "'NOT'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "STRING", "INT", "AGGREGATE", 
			"NAME", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0017\u008c\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f"+
		"\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012"+
		"\u0001\u0012\u0005\u0012b\b\u0012\n\u0012\f\u0012e\t\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0013\u0004\u0013j\b\u0013\u000b\u0013\f\u0013k\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u007f"+
		"\b\u0014\u0001\u0015\u0004\u0015\u0082\b\u0015\u000b\u0015\f\u0015\u0083"+
		"\u0001\u0016\u0004\u0016\u0087\b\u0016\u000b\u0016\f\u0016\u0088\u0001"+
		"\u0016\u0001\u0016\u0001c\u0000\u0017\u0001\u0001\u0003\u0002\u0005\u0003"+
		"\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015"+
		"\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012"+
		"%\u0013\'\u0014)\u0015+\u0016-\u0017\u0001\u0000\u0004\u0001\u0000\"\""+
		"\u0001\u000009\u0004\u000009AZ__az\u0003\u0000\t\n\r\r  \u0093\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005"+
		"\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001"+
		"\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000"+
		"\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000"+
		"\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000"+
		"\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000"+
		"\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000"+
		"\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000"+
		"\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000"+
		"\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001"+
		"\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0001/\u0001\u0000\u0000"+
		"\u0000\u00031\u0001\u0000\u0000\u0000\u00053\u0001\u0000\u0000\u0000\u0007"+
		"6\u0001\u0000\u0000\u0000\t8\u0001\u0000\u0000\u0000\u000b:\u0001\u0000"+
		"\u0000\u0000\r<\u0001\u0000\u0000\u0000\u000f>\u0001\u0000\u0000\u0000"+
		"\u0011@\u0001\u0000\u0000\u0000\u0013B\u0001\u0000\u0000\u0000\u0015D"+
		"\u0001\u0000\u0000\u0000\u0017G\u0001\u0000\u0000\u0000\u0019J\u0001\u0000"+
		"\u0000\u0000\u001bM\u0001\u0000\u0000\u0000\u001dR\u0001\u0000\u0000\u0000"+
		"\u001fV\u0001\u0000\u0000\u0000!Y\u0001\u0000\u0000\u0000#]\u0001\u0000"+
		"\u0000\u0000%_\u0001\u0000\u0000\u0000\'i\u0001\u0000\u0000\u0000)~\u0001"+
		"\u0000\u0000\u0000+\u0081\u0001\u0000\u0000\u0000-\u0086\u0001\u0000\u0000"+
		"\u0000/0\u0005(\u0000\u00000\u0002\u0001\u0000\u0000\u000012\u0005)\u0000"+
		"\u00002\u0004\u0001\u0000\u0000\u000034\u0005A\u0000\u000045\u0005S\u0000"+
		"\u00005\u0006\u0001\u0000\u0000\u000067\u0005*\u0000\u00007\b\u0001\u0000"+
		"\u0000\u000089\u0005/\u0000\u00009\n\u0001\u0000\u0000\u0000:;\u0005+"+
		"\u0000\u0000;\f\u0001\u0000\u0000\u0000<=\u0005-\u0000\u0000=\u000e\u0001"+
		"\u0000\u0000\u0000>?\u0005=\u0000\u0000?\u0010\u0001\u0000\u0000\u0000"+
		"@A\u0005>\u0000\u0000A\u0012\u0001\u0000\u0000\u0000BC\u0005<\u0000\u0000"+
		"C\u0014\u0001\u0000\u0000\u0000DE\u0005>\u0000\u0000EF\u0005=\u0000\u0000"+
		"F\u0016\u0001\u0000\u0000\u0000GH\u0005<\u0000\u0000HI\u0005=\u0000\u0000"+
		"I\u0018\u0001\u0000\u0000\u0000JK\u0005<\u0000\u0000KL\u0005>\u0000\u0000"+
		"L\u001a\u0001\u0000\u0000\u0000MN\u0005L\u0000\u0000NO\u0005I\u0000\u0000"+
		"OP\u0005K\u0000\u0000PQ\u0005E\u0000\u0000Q\u001c\u0001\u0000\u0000\u0000"+
		"RS\u0005A\u0000\u0000ST\u0005N\u0000\u0000TU\u0005D\u0000\u0000U\u001e"+
		"\u0001\u0000\u0000\u0000VW\u0005O\u0000\u0000WX\u0005R\u0000\u0000X \u0001"+
		"\u0000\u0000\u0000YZ\u0005N\u0000\u0000Z[\u0005O\u0000\u0000[\\\u0005"+
		"T\u0000\u0000\\\"\u0001\u0000\u0000\u0000]^\u0005.\u0000\u0000^$\u0001"+
		"\u0000\u0000\u0000_c\u0007\u0000\u0000\u0000`b\t\u0000\u0000\u0000a`\u0001"+
		"\u0000\u0000\u0000be\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000"+
		"ca\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000"+
		"\u0000fg\u0007\u0000\u0000\u0000g&\u0001\u0000\u0000\u0000hj\u0007\u0001"+
		"\u0000\u0000ih\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000ki\u0001"+
		"\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000l(\u0001\u0000\u0000\u0000"+
		"mn\u0005S\u0000\u0000no\u0005U\u0000\u0000o\u007f\u0005M\u0000\u0000p"+
		"q\u0005A\u0000\u0000qr\u0005V\u0000\u0000r\u007f\u0005G\u0000\u0000st"+
		"\u0005C\u0000\u0000tu\u0005O\u0000\u0000uv\u0005U\u0000\u0000vw\u0005"+
		"N\u0000\u0000w\u007f\u0005T\u0000\u0000xy\u0005M\u0000\u0000yz\u0005I"+
		"\u0000\u0000z\u007f\u0005N\u0000\u0000{|\u0005M\u0000\u0000|}\u0005A\u0000"+
		"\u0000}\u007f\u0005X\u0000\u0000~m\u0001\u0000\u0000\u0000~p\u0001\u0000"+
		"\u0000\u0000~s\u0001\u0000\u0000\u0000~x\u0001\u0000\u0000\u0000~{\u0001"+
		"\u0000\u0000\u0000\u007f*\u0001\u0000\u0000\u0000\u0080\u0082\u0007\u0002"+
		"\u0000\u0000\u0081\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000"+
		"\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000"+
		"\u0000\u0000\u0084,\u0001\u0000\u0000\u0000\u0085\u0087\u0007\u0003\u0000"+
		"\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000"+
		"\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088\u0089\u0001\u0000\u0000"+
		"\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u008b\u0006\u0016\u0000"+
		"\u0000\u008b.\u0001\u0000\u0000\u0000\u0006\u0000ck~\u0083\u0088\u0001"+
		"\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}