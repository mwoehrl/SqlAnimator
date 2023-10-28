package de.mwoehrl.sqlanimator.query.expression.antlr;
// Generated from Expression.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ExpressionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, STRING=19, INT=20, AGGREGATE=21, NAME=22, WS=23;
	public static final int
		RULE_colexpression = 0, RULE_expression = 1, RULE_literal = 2, RULE_column = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"colexpression", "expression", "literal", "column"
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

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColexpressionContext extends ParserRuleContext {
		public TerminalNode AGGREGATE() { return getToken(ExpressionParser.AGGREGATE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode NAME() { return getToken(ExpressionParser.NAME, 0); }
		public ColexpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colexpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterColexpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitColexpression(this);
		}
	}

	public final ColexpressionContext colexpression() throws RecognitionException {
		ColexpressionContext _localctx = new ColexpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_colexpression);
		try {
			setState(25);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(8);
				match(AGGREGATE);
				setState(9);
				match(T__0);
				setState(10);
				expression(0);
				setState(11);
				match(T__1);
				setState(12);
				match(T__2);
				setState(13);
				match(NAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(15);
				match(AGGREGATE);
				setState(16);
				match(T__0);
				setState(17);
				expression(0);
				setState(18);
				match(T__1);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(20);
				expression(0);
				setState(21);
				match(T__2);
				setState(22);
				match(NAME);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(24);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ColumnContext column() {
			return getRuleContext(ColumnContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
				{
				setState(28);
				match(T__16);
				setState(29);
				expression(4);
				}
				break;
			case NAME:
				{
				setState(30);
				column();
				}
				break;
			case STRING:
			case INT:
				{
				setState(31);
				literal();
				}
				break;
			case T__0:
				{
				setState(32);
				match(T__0);
				setState(33);
				expression(0);
				setState(34);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(58);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(56);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(38);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(39);
						_la = _input.LA(1);
						if ( !(_la==T__3 || _la==T__4) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(40);
						expression(11);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(41);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(42);
						_la = _input.LA(1);
						if ( !(_la==T__5 || _la==T__6) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(43);
						expression(10);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(44);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(45);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16128L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(46);
						expression(9);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(47);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(48);
						match(T__13);
						setState(49);
						expression(8);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(50);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(51);
						match(T__14);
						setState(52);
						expression(7);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(53);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(54);
						match(T__15);
						setState(55);
						expression(6);
						}
						break;
					}
					} 
				}
				setState(60);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(ExpressionParser.INT, 0); }
		public TerminalNode STRING() { return getToken(ExpressionParser.STRING, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			_la = _input.LA(1);
			if ( !(_la==STRING || _la==INT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ColumnContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(ExpressionParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(ExpressionParser.NAME, i);
		}
		public ColumnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			//if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitColumn(this);
		}
	}

	public final ColumnContext column() throws RecognitionException {
		ColumnContext _localctx = new ColumnContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_column);
		try {
			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(63);
				match(NAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(64);
				match(NAME);
				setState(65);
				match(T__17);
				setState(66);
				match(NAME);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 7);
		case 4:
			return precpred(_ctx, 6);
		case 5:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0017F\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u001a\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u0001%\b\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u00019\b\u0001\n\u0001"+
		"\f\u0001<\t\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u0003D\b\u0003\u0001\u0003\u0000\u0001\u0002"+
		"\u0004\u0000\u0002\u0004\u0006\u0000\u0004\u0001\u0000\u0004\u0005\u0001"+
		"\u0000\u0006\u0007\u0001\u0000\b\r\u0001\u0000\u0013\u0014N\u0000\u0019"+
		"\u0001\u0000\u0000\u0000\u0002$\u0001\u0000\u0000\u0000\u0004=\u0001\u0000"+
		"\u0000\u0000\u0006C\u0001\u0000\u0000\u0000\b\t\u0005\u0015\u0000\u0000"+
		"\t\n\u0005\u0001\u0000\u0000\n\u000b\u0003\u0002\u0001\u0000\u000b\f\u0005"+
		"\u0002\u0000\u0000\f\r\u0005\u0003\u0000\u0000\r\u000e\u0005\u0016\u0000"+
		"\u0000\u000e\u001a\u0001\u0000\u0000\u0000\u000f\u0010\u0005\u0015\u0000"+
		"\u0000\u0010\u0011\u0005\u0001\u0000\u0000\u0011\u0012\u0003\u0002\u0001"+
		"\u0000\u0012\u0013\u0005\u0002\u0000\u0000\u0013\u001a\u0001\u0000\u0000"+
		"\u0000\u0014\u0015\u0003\u0002\u0001\u0000\u0015\u0016\u0005\u0003\u0000"+
		"\u0000\u0016\u0017\u0005\u0016\u0000\u0000\u0017\u001a\u0001\u0000\u0000"+
		"\u0000\u0018\u001a\u0003\u0002\u0001\u0000\u0019\b\u0001\u0000\u0000\u0000"+
		"\u0019\u000f\u0001\u0000\u0000\u0000\u0019\u0014\u0001\u0000\u0000\u0000"+
		"\u0019\u0018\u0001\u0000\u0000\u0000\u001a\u0001\u0001\u0000\u0000\u0000"+
		"\u001b\u001c\u0006\u0001\uffff\uffff\u0000\u001c\u001d\u0005\u0011\u0000"+
		"\u0000\u001d%\u0003\u0002\u0001\u0004\u001e%\u0003\u0006\u0003\u0000\u001f"+
		"%\u0003\u0004\u0002\u0000 !\u0005\u0001\u0000\u0000!\"\u0003\u0002\u0001"+
		"\u0000\"#\u0005\u0002\u0000\u0000#%\u0001\u0000\u0000\u0000$\u001b\u0001"+
		"\u0000\u0000\u0000$\u001e\u0001\u0000\u0000\u0000$\u001f\u0001\u0000\u0000"+
		"\u0000$ \u0001\u0000\u0000\u0000%:\u0001\u0000\u0000\u0000&\'\n\n\u0000"+
		"\u0000\'(\u0007\u0000\u0000\u0000(9\u0003\u0002\u0001\u000b)*\n\t\u0000"+
		"\u0000*+\u0007\u0001\u0000\u0000+9\u0003\u0002\u0001\n,-\n\b\u0000\u0000"+
		"-.\u0007\u0002\u0000\u0000.9\u0003\u0002\u0001\t/0\n\u0007\u0000\u0000"+
		"01\u0005\u000e\u0000\u000019\u0003\u0002\u0001\b23\n\u0006\u0000\u0000"+
		"34\u0005\u000f\u0000\u000049\u0003\u0002\u0001\u000756\n\u0005\u0000\u0000"+
		"67\u0005\u0010\u0000\u000079\u0003\u0002\u0001\u00068&\u0001\u0000\u0000"+
		"\u00008)\u0001\u0000\u0000\u00008,\u0001\u0000\u0000\u00008/\u0001\u0000"+
		"\u0000\u000082\u0001\u0000\u0000\u000085\u0001\u0000\u0000\u00009<\u0001"+
		"\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000"+
		";\u0003\u0001\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000=>\u0007\u0003"+
		"\u0000\u0000>\u0005\u0001\u0000\u0000\u0000?D\u0005\u0016\u0000\u0000"+
		"@A\u0005\u0016\u0000\u0000AB\u0005\u0012\u0000\u0000BD\u0005\u0016\u0000"+
		"\u0000C?\u0001\u0000\u0000\u0000C@\u0001\u0000\u0000\u0000D\u0007\u0001"+
		"\u0000\u0000\u0000\u0005\u0019$8:C";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}