#include "Lex.h"

#include "Input.h"
#include "TokenType.h"
#include "Utils.h"
#include <iostream>
#include <fstream>

using namespace std;

Lex::Lex() {
	errorLine = 0;
	try{
	input = new Input();
	}catch(exception& e){

	}
    generateTokens(input);
//    delete input;
}

Lex::Lex(const char* filename) {
	errorLine = 0;
	try{
    input = new Input(filename);
	}catch(exception& e){

	}
    generateTokens(input);
//    delete input;
}

Lex::Lex(istream& istream) {
	errorLine = 0;
	try{
    input = new Input(istream);
	}catch(exception& e){

	}
    generateTokens(input);
//    delete input;
}

Lex::Lex(const Lex& lex) {
	errorLine = 0;
    input = new Input(*lex.input);
    tokens = new vector<Token*>();

    vector<Token*>::iterator iter;
    for(iter=lex.tokens->begin(); iter != lex.tokens->end(); iter++) {
        Token* newToken = new Token(**iter);
        tokens->push_back(newToken);
    }

    index = lex.index;
    state = lex.state;
}

Lex::~Lex(){
    for (size_t i = 0; i < tokens->size(); i++) {
        delete (*tokens)[i];
    }
    delete tokens;
    delete input;
}

bool Lex::operator==(const Lex& lex) {
    bool result = (tokens->size() == lex.tokens->size()) && (index == lex.index);
    if(result) {
        vector<Token*>::iterator iter1;
        vector<Token*>::iterator iter2;
        iter1 = tokens->begin();
        iter2 = lex.tokens->begin();
        while(result && iter1 != tokens->end() && iter2 != lex.tokens->end()) {
            result = **iter1 == **iter2;
            iter1++;
            iter2++;
        }
        result = result && iter1 == tokens->end() && iter2 == lex.tokens->end();
    }
    return result;
}

vector<Token*>* Lex::getTokens(){
    return this->tokens;
}

string Lex::toString() const {
    size_t count = 0;
    string result;
    while(count < tokens->size()) {
        Token* token = (*tokens)[count];
        result += token->toString();
        count++;
    }
    result += "Total Tokens = ";
    string countToString;
    result += itoa(countToString, count);
    result += "\n";
    return result;
}

void Lex::generateTokens(Input* input) {
    tokens = new vector<Token*>();
    index = 0;

    state = Start;
    while(state != End) {
        state = nextState();
    }

}

Token* Lex::getCurrentToken() {
    return (*tokens)[index];
}

void Lex::advance() {
    index++;
}

bool Lex::hasNext() {
    return index < tokens->size();
}

State Lex::nextState2() {
State result;
char character;
switch(state) {
case SawPound:
		character = input->getCurrentCharacter();
		cout << "saw pound: " << character << endl;
		if(character == '|'){// needs a \ before the |? **********************************
			cout << "saw or" << endl;
			result = ProcessingBlockComment;
			input->advance();
		} else if(character == -1) {
			emit(EOTF);
		} else {
			result = ProcessingComment;
		}
		break;
default:
	return nextState3();
}
return result;
}
State Lex::nextState3() {
State result;
char character;
switch(state) {
	case PossibleEndOfComment:
		character = input->getCurrentCharacter();

		if(isspace(character)){
			emit(COMMENT);
			result = getNextState();
		} else if(character == -1) {
			emit(EOTF);
		} else {
			result = ProcessingComment;
			input->advance();
		}
		break;
default:
	return nextState4();
}
return result;
}
State Lex::nextState4() {
State result;
char character;
switch(state) {
	case ProcessingComment:
		character = input->getCurrentCharacter();
		cout << "in comment: " << character << endl;
		if(character == '\n'){
			emit(COMMENT);
			result = getNextState();
		} else if(character == -1) {
			emit(EOTF);
		} else {// every other character
			result = ProcessingComment;
			input->advance();
		}
		break;
default:
	return nextState5();
}
return result;
}
State Lex::nextState5() {
State result;
char character;
switch(state) {
	case ProcessingBlockComment:
		character = input->getCurrentCharacter();
		if(character == '|'){
			result = PossibleEndOfBlockComment;
		} else if(character == -1) {// illegal
			result = Undefined;
		} else {// every other character
			result = ProcessingBlockComment;
		}
		input->advance();
		break;
default:
	return nextState6();
}
return result;
}
State Lex::nextState6() {
State result;
char character;
switch(state) {
	case PossibleEndOfBlockComment:
		character = input->getCurrentCharacter();
		if(character == '#') {
			input->advance();
			emit(COMMENT);
			result = getNextState();
		} else if(character == -1){// illegal
			result = Undefined;
		} else {// every other character
			result = ProcessingBlockComment;
			input->advance();
		}
		break;
default:
	return nextState7();
}
return result;
}
State Lex::nextState7() {
State result;
char character;
switch(state) {
    case SawColon:
        character = input->getCurrentCharacter();
        if(character == '-') {
            result = Colon_Dash;
            input->advance();
        } else if(character == -1){
			emit(EOTF);
		} else { //Every other character
            emit(COLON);
			result = getNextState();
        }
        break;
default:
	return nextState8();
      }
      return result;
}
State Lex::nextState8() {
State result;
switch(state) {
    case Colon_Dash:          emit(COLON_DASH); result = getNextState(); break;
default:
	return nextState9();
  }
  return result;
}
State Lex::nextState9() {
State result;
char character;
switch(state) {
    case SawAQuote:
        character = input->getCurrentCharacter();
        if(character == '\'') {
            emit(STRING);
            result = getNextState();
        } else if(character == -1) {
			emit(EOTF);
        } else { //Every other character
            result = ProcessingString;
            input->advance();
        }
        break;
default:
	return nextState10();
      }
      return result;
}
State Lex::nextState10() {
State result;
char character;
switch(state) {
    case ProcessingString:

        character = input->getCurrentCharacter();
//        cout << "in processing string state" << character << endl;
        if(character == '\'') {
            emit(STRING);
//            cout << "found string" << input->getCurrentTokensLineNumber() << endl;
            input->advance();
            input->mark();
            result = getNextState();

        } else if(character == -1) {
			result = Undefined;
        }else if(character == '\n'){
//        	cout << "found new line" << input->getCurrentTokensLineNumber() << endl;
        	result = Undefined;
        } else { //Every other character
            result = ProcessingString;
            input->advance();
        }
        break;
default:
	return nextState11();
      }
      return result;
}
State Lex::nextState11() {
State result;
switch(state) {
    case PossibleEndOfString:
        if(input->getCurrentCharacter() == '\'') {
            input->advance();
            result = ProcessingString;
        } else { //Every other character
            emit(STRING);
            result = getNextState();
        }
        break;
default:
	return nextState12();
      }
      return result;
}
State Lex::nextState12() {
State result;
char character;
switch(state) {
	case ProcessingId:
		character = input->getCurrentCharacter();
		cout << "processing id: " << character << endl;
		if(isalnum(character)){
			result = ProcessingId;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else if(character == '\n') {
			result = PossibleEndOfId;
		} else {
			result = PossibleEndOfId;
		}
		break;
default:
	return nextState13();
}
return result;
}
State Lex::nextState13() {
State result;
char character;
switch(state) {
	case PossibleEndOfId:
		character = input->getCurrentCharacter();
		if(character == '\n'){
			emit(ID);
			result = getNextState();
		} else if(!isalnum(character)){
			emit(ID);
			result = getNextState();
		} else if(character == -1){
			emit(EOTF);
		} else {// should never get here
			result = ProcessingId;
			input->advance();
		}
		break;
default:
	return nextState14();
}
return result;
}
State Lex::nextState14() {
State result;
switch(state) {
	case Whitespace:
		result = getNextState();
		break;
default:
	return nextState15();
}
return result;
}
State Lex::nextState15() {
State result;
switch(state) {
	case Undefined:
		emit(UNDEFINED);
		result = getNextState();
		break;
	default:
	return nextState16();
}

return result;
}
State Lex::nextState16() {
State result;
char character;
switch(state) {
	case SchemesC:
		character = input->getCurrentCharacter();
		if(character == 'c'){
			result = SchemesH;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState17();
}
return result;
}
State Lex::nextState17() {
State result;
char character;
switch(state) {
	case SchemesH:
		character = input->getCurrentCharacter();
		if(character == 'h'){
			result = SchemesE1;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState18();
}
return result;
}
State Lex::nextState18() {
State result;
char character;
switch(state) {
	case SchemesE1:
		character = input->getCurrentCharacter();
		if(character == 'e'){
			result = SchemesM;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState19();
}
return result;
}
State Lex::nextState19() {
State result;
char character;
switch(state) {
	case SchemesM:
		character = input->getCurrentCharacter();
		if(character == 'm'){
			result = SchemesE2;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState20();
}
return result;
}
State Lex::nextState20() {
State result;
char character;
switch(state) {
	case SchemesE2:
		character = input->getCurrentCharacter();
		if(character == 'e'){
			result = SchemesS2;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState21();
}
return result;
}
State Lex::nextState21() {
State result;
char character;
switch(state) {
	case SchemesS2:
		character = input->getCurrentCharacter();
		if(character == 's'){
			result = PossibleEndOfSchemes;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState22();
}
return result;
}
State Lex::nextState22() {
State result;
char character;
switch(state) {
	case FactsA:
		character = input->getCurrentCharacter();
		if(character == 'a'){
			result = FactsC;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState23();
}
return result;
}
State Lex::nextState23() {
State result;
char character;
switch(state) {
	case FactsC:
		character = input->getCurrentCharacter();
		if(character == 'c'){
			result = FactsT;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState24();
}
return result;
}
State Lex::nextState24() {
State result;
char character;
switch(state) {
	case FactsT:
		character = input->getCurrentCharacter();
		if(character == 't'){
			result = FactsS;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState25();
}
return result;
}
State Lex::nextState25() {
State result;
char character;
switch(state) {
	case FactsS:
		character = input->getCurrentCharacter();
		if(character == 's'){
			result = PossibleEndOfFacts;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState26();
}
return result;
}
State Lex::nextState26() {
State result;
char character;
switch(state) {
	case RulesU:
		character = input->getCurrentCharacter();
		if(character == 'u'){
			result = RulesL;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState27();
}
return result;
}
State Lex::nextState27() {
State result;
char character;
switch(state) {
	case RulesL:
		character = input->getCurrentCharacter();
		if(character == 'l'){
			result = RulesE;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState28();
}
return result;
}
State Lex::nextState28() {
State result;
char character;
switch(state) {
	case RulesE:
		character = input->getCurrentCharacter();
		if(character == 'e'){
			result = RulesS;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState29();
}
return result;
}
State Lex::nextState29() {
State result;
char character;
switch(state) {
	case RulesS:
		character = input->getCurrentCharacter();
		if(character == 's'){
			result = PossibleEndOfRules;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState30();
}
return result;
}
State Lex::nextState30() {
State result;
char character;
switch(state) {
	case QueriesU:
		character = input->getCurrentCharacter();
		if(character == 'u'){
			result = QueriesE1;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState31();
}
return result;
}
State Lex::nextState31() {
State result;
char character;
switch(state) {
	case QueriesE1:
		character = input->getCurrentCharacter();
		if(character == 'e'){
			result = QueriesR;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState32();
}
return result;
}
State Lex::nextState32() {
State result;
char character;
switch(state) {
	case QueriesR:
		character = input->getCurrentCharacter();
		if(character == 'r'){
			result = QueriesI;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState33();
}
return result;
}
State Lex::nextState33() {
State result;
char character;
switch(state) {
	case QueriesI:
		character = input->getCurrentCharacter();
		if(character == 'i'){
			result = QueriesE2;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState34();
}
return result;
}
State Lex::nextState34() {
State result;
char character;
switch(state) {
	case QueriesE2:
		character = input->getCurrentCharacter();
		if(character == 'e'){
			result = QueriesS;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState35();
}
return result;
}
State Lex::nextState35() {
State result;
char character;
switch(state) {
	case QueriesS:
		character = input->getCurrentCharacter();
		if(character == 's'){
			result = PossibleEndOfQueries;
			input->advance();
		} else if(character == -1){
			emit(EOTF);
		} else {
			result = ProcessingId;
		}
		break;
default:
	return nextState36();
}
return result;
}
State Lex::nextState36() {
State result;
char character;
switch(state) {
	case PossibleEndOfFacts:
		character = input->getCurrentCharacter();
		if(isalnum(character)){
			result = ProcessingId;
			input->advance();
		} else {
			emit(FACTS);
			result = getNextState();
		}
		break;
default:
	return nextState37();
}
return result;
}
State Lex::nextState37() {
State result;
char character;
switch(state) {
	case PossibleEndOfRules:
		character = input->getCurrentCharacter();
		if(isalnum(character)){
			result = ProcessingId;
			input->advance();
		} else {
			emit(RULES);
			result = getNextState();
		}
		break;
default:
	return nextState38();
}
return result;
}
State Lex::nextState38() {
State result;
char character;
switch(state) {
	case PossibleEndOfSchemes:
		character = input->getCurrentCharacter();
		if(isalnum(character)){
			result = ProcessingId;
			input->advance();
		} else {
			emit(SCHEMES);
			result = getNextState();
		}
		break;
default:
	return nextState39();
}
return result;
}
State Lex::nextState39() {
State result;
char character;
switch(state) {
	case PossibleEndOfQueries:
		character = input->getCurrentCharacter();
		if(isalnum(character)){
			result = ProcessingId;
			input->advance();
		} else {
			emit(QUERIES);
			result = getNextState();
		}
		break;
default:
	return nextState();
}
return result;
}


State Lex::nextState() {
    State result;
//    char character;
    switch(state) {
        case Start:               result = getNextState(); break;
        case Comma:               emit(COMMA); result = getNextState(); break;
        case Period:              emit(PERIOD); result = getNextState(); break;
		case Q_Mark:			  emit(Q_MARK); result = getNextState(); break;
		case Left_Paren:		  emit(LEFT_PAREN); result = getNextState(); break;
		case Right_Paren:		  emit(RIGHT_PAREN); result = getNextState(); break;
		case Multiply:			  emit(MULTIPLY); result = getNextState(); break;
		case Add:				  emit(ADD); result = getNextState(); break;
		default:

			return nextState2();
    };
    return result;
}

State Lex::getNextState() {
    State result;
    char currentCharacter = input->getCurrentCharacter();
	if(isspace(currentCharacter)){
		cout << "Found whitespace" << endl;
		result = Whitespace;
		input->advance();
		input->mark();
	} else if(isalpha(currentCharacter)){
		switch(currentCharacter){
			case 'S' : result = SchemesC; break;
			case 'F' : result = FactsA; break;
			case 'R' : result = RulesU; break;
			case 'Q' : result = QueriesU; break;
			default:
				if(isdigit(currentCharacter)){
					if(errorLine == 0)
						errorLine = input->getCurrentTokensLineNumber();
				}else
					result = ProcessingId; break;
		}
		input->advance();
	} else {
		switch(currentCharacter) {
			case ','  : result = Comma; break;
			case '.'  : result = Period; break;
			case '?'  : result = Q_Mark; break;
			case '('  : result = Left_Paren; break;
			case ')'  : result = Right_Paren; break;
			case ':'  : result = SawColon; break;
			case '*'  : result = Multiply; break;
			case '+'  : result = Add; break;
			case '\'' : result = ProcessingString; break;
			case '#'  : result = ProcessingComment; break;
			case -1   : result = End; input->advance(); input->mark(); emit(EOTF); break;
			default:
				string error = "ERROR:: in Lex::getNextState, Expecting  ";
				error += "'\'', '.', '?', '(', ')', '+', '*', 'S', 'F', 'R', 'Q', ':' but found ";
				error += currentCharacter;
				error += '.';
				//throw error.c_str();
				result = Undefined; // this correct?******************************
				break;
		}
		input->advance();
	}
    return result;
}

void Lex::emit(TokenType tokenType) {
	Token* token = NULL;
	if(tokenType == STRING)
		token = new Token(tokenType, input->getTokensValueString(), input->getCurrentTokensLineNumber());
	else if(tokenType == UNDEFINED){
		token = new Token(tokenType, input->getTokensValueString(), input->getCurrentTokensLineNumber());
		if(errorLine == 0)
			errorLine = input->getCurrentTokensLineNumber();
	}
	else
		token = new Token(tokenType, input->getTokensValue(), input->getCurrentTokensLineNumber());
    if(tokenType != COMMENT && tokenType != EOTF)
        storeToken(token);
    else
    	delete token;
    input->mark();
}

void Lex::storeToken(Token* token) {
    tokens->push_back(token);
}

void Lex::writeToFile(char* filename){
	try{
		ofstream myfile;
		myfile.open(filename);
		if(this->errorLine != 0)
			myfile << "Error on line " << errorLine;
		else
			myfile << this->toString();
		myfile.close();
	}catch(exception& e){
		e.what();
	}
}

//int main(int argc, char* argv[]) {
////	cout << "IasdfO am here" << endl;
//	Lex *lex = NULL;
//	try{
////		cout << "asdfasdffff" << endl;
//
//    lex = new Lex(argv[1]);
//
////	Lex lex("/users/ugrad/c/cmccord/Downloads/LexicalAnalyzer/LexicalAnalyzer/testFile1.txt");
//
//    lex->writeToFile(argv[2]);
//
//	}catch(exception& e){
//		e.what();
//	}
//	delete lex;
//    return 0;
//}

//		case SawPound:
//			character = input->getCurrentCharacter();
//			if(character == '|'){// needs a \ before the |? **********************************
//				result = ProcessingBlockComment;
//				input->advance();
//			} else if(character == -1) {
//				emit(EOTF);
//			} else {
//				result = ProcessingComment;
//			}
//			break;
//		case PossibleEndOfComment:
//			character = input->getCurrentCharacter();
//			if(isspace(character)){
//				emit(COMMENT);
//				result = getNextState();
//			} else if(character == -1) {
//				emit(EOTF);
//			} else {
//				result = ProcessingComment;
//				input->advance();
//			}
//			break;
//		case ProcessingComment:
//			character = input->getCurrentCharacter();
//			if(character == '\n'){
//				result = PossibleEndOfComment;
//			} else if(character == -1) {
//				emit(EOTF);
//			} else {// every other character
//				result = ProcessingComment;
//				input->advance();
//			}
//			break;
//		case ProcessingBlockComment:
//			character = input->getCurrentCharacter();
//			if(character == '|'){
//				result = PossibleEndOfBlockComment;
//			} else if(character == -1) {// illegal
//				result = Undefined;
//			} else {// every other character
//				result = ProcessingBlockComment;
//			}
//			input->advance();
//			break;
//		case PossibleEndOfBlockComment:
//			character = input->getCurrentCharacter();
//			if(character == '#') {
//				input->advance();
//				emit(COMMENT);
//				result = getNextState();
//			} else if(character == -1){// illegal
//				result = Undefined;
//			} else {// every other character
//				result = ProcessingBlockComment;
//				input->advance();
//			}
//			break;
//        case SawColon:
//            character = input->getCurrentCharacter();
//            if(character == '-') {
//                result = Colon_Dash;
//                input->advance();
//            } else if(character == -1){
//				emit(EOTF);
//			} else { //Every other character
//                emit(COLON);
//				result = getNextState();
//            }
//            break;
//        case Colon_Dash:          emit(COLON_DASH); result = getNextState(); break;
//        case SawAQuote:
//            character = input->getCurrentCharacter();
//            if(character == '\'') {
//                result = PossibleEndOfString;
//            } else if(character == -1) {
//				emit(EOTF);
//            } else { //Every other character
//                result = ProcessingString;
//            }
//            input->advance();
//            break;
//        case ProcessingString:
//            character = input->getCurrentCharacter();
//            if(character == '\'') {
//                result = PossibleEndOfString;
//            } else if(character == -1) {
//				result = Undefined;
//            } else { //Every other character
//                result = ProcessingString;
//            }
//            input->advance();
//            break;
//        case PossibleEndOfString:
//            if(input->getCurrentCharacter() == '\'') {
//                input->advance();
//                result = ProcessingString;
//            } else { //Every other character
//                emit(STRING);
//                result = getNextState();
//            }
//            break;
//		case ProcessingId:
//			character = input->getCurrentCharacter();
//			if(isalnum(character)){
//				result = ProcessingId;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else if(character == '\n') {
//				result = PossibleEndOfId;
//			} else {
//				result = PossibleEndOfId;
//			}
//			break;
//		case PossibleEndOfId:
//			character = input->getCurrentCharacter();
//			if(character == '\n'){
//				emit(ID);
//				result = getNextState();
//			} else if(!isalnum(character)){
//				emit(ID);
//				result = getNextState();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {// should never get here
//				result = ProcessingId;
//				input->advance();
//			}
//			break;
//		case Whitespace:
//			result = getNextState();
//			break;
//		case Undefined:
//			emit(UNDEFINED);
//			result = getNextState();
//			break;
//
//
//
//		// Keywords
//		case SchemesC:
//			character = input->getCurrentCharacter();
//			if(character == 'c'){
//				result = SchemesH;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case SchemesH:
//			character = input->getCurrentCharacter();
//			if(character == 'h'){
//				result = SchemesE1;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case SchemesE1:
//			character = input->getCurrentCharacter();
//			if(character == 'e'){
//				result = SchemesM;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case SchemesM:
//			character = input->getCurrentCharacter();
//			if(character == 'm'){
//				result = SchemesE2;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case SchemesE2:
//			character = input->getCurrentCharacter();
//			if(character == 'e'){
//				result = SchemesS2;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case SchemesS2:
//			character = input->getCurrentCharacter();
//			if(character == 's'){
//				result = PossibleEndOfSchemes;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case FactsA:
//			character = input->getCurrentCharacter();
//			if(character == 'a'){
//				result = FactsC;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case FactsC:
//			character = input->getCurrentCharacter();
//			if(character == 'c'){
//				result = FactsT;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case FactsT:
//			character = input->getCurrentCharacter();
//			if(character == 't'){
//				result = FactsS;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case FactsS:
//			character = input->getCurrentCharacter();
//			if(character == 's'){
//				result = PossibleEndOfFacts;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case RulesU:
//			character = input->getCurrentCharacter();
//			if(character == 'u'){
//				result = RulesL;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case RulesL:
//			character = input->getCurrentCharacter();
//			if(character == 'l'){
//				result = RulesE;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case RulesE:
//			character = input->getCurrentCharacter();
//			if(character == 'e'){
//				result = RulesS;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case RulesS:
//			character = input->getCurrentCharacter();
//			if(character == 's'){
//				result = PossibleEndOfRules;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case QueriesU:
//			character = input->getCurrentCharacter();
//			if(character == 'u'){
//				result = QueriesE1;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case QueriesE1:
//			character = input->getCurrentCharacter();
//			if(character == 'e'){
//				result = QueriesR;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case QueriesR:
//			character = input->getCurrentCharacter();
//			if(character == 'r'){
//				result = QueriesI;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case QueriesI:
//			character = input->getCurrentCharacter();
//			if(character == 'i'){
//				result = QueriesE2;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case QueriesE2:
//			character = input->getCurrentCharacter();
//			if(character == 'e'){
//				result = QueriesS;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//		case QueriesS:
//			character = input->getCurrentCharacter();
//			if(character == 's'){
//				result = PossibleEndOfQueries;
//				input->advance();
//			} else if(character == -1){
//				emit(EOTF);
//			} else {
//				result = ProcessingId;
//			}
//			break;
//
//
//
//		case PossibleEndOfFacts:
//			character = input->getCurrentCharacter();
//			if(isalnum(character)){
//				result = ProcessingId;
//				input->advance();
//			} else {
//				emit(FACTS);
//				result = getNextState();
//			}
//			break;
//		case PossibleEndOfRules:
//			character = input->getCurrentCharacter();
//			if(isalnum(character)){
//				result = ProcessingId;
//				input->advance();
//			} else {
//				emit(RULES);
//				result = getNextState();
//			}
//			break;
//		case PossibleEndOfSchemes:
//			character = input->getCurrentCharacter();
//			if(isalnum(character)){
//				result = ProcessingId;
//				input->advance();
//			} else {
//				emit(SCHEMES);
//				result = getNextState();
//			}
//			break;
//		case PossibleEndOfQueries:
//			character = input->getCurrentCharacter();
//			if(isalnum(character)){
//				result = ProcessingId;
//				input->advance();
//			} else {
//				emit(QUERIES);
//				result = getNextState();
//			}
//			break;
//
