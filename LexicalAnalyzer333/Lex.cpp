#include "Lex.h"

#include "Input.h"
#include "TokenType.h"
#include "Utils.h"
#include <iostream>

using namespace std;

Lex::Lex() {
	input = new Input();
    generateTokens(input);
}

Lex::Lex(const char* filename) {
    input = new Input(filename);
    generateTokens(input);
}

Lex::Lex(istream& istream) {
    input = new Input(istream);
    generateTokens(input);
}

Lex::Lex(const Lex& lex) {
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
    for (int i = 0; i < tokens->size(); i++) {
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

string Lex::toString() const {
    int count = 0;
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

State Lex::nextState() { // check possible ends********************************
    State result;
    char character;
    switch(state) {
        case Start:               result = getNextState(); break;
        case Comma:               emit(COMMA); result = getNextState(); break;
        case Period:              emit(PERIOD); result = getNextState(); break;
		case Q_Mark:			  emit(Q_MARK); result = getNextState(); break;
		case Left_Paren:		  emit(LEFT_PAREN); result = getNextState(); break; // have to follow with right?
		case Right_Paren:		  emit(RIGHT_PAREN); result = getNextState(); break;
		case Multiply:			  emit(MULTIPLY); result = getNextState(); break;
		case Add:				  emit(ADD); result = getNextState();
		case SawPound:
			character = input->getCurrentCharacter();
			if(character == '|'){// needs a \ before the |? **********************************
				result = ProcessingBlockComment;
				input->advance();
			} else if(character == -1) {
				emit(EOTF);
			} else {
				result = ProcessingComment;
			}
			break;
		case PossibleEndOfComment:
			character = input->getCurrentCharacter();
			if(isspace(character)){
				emit(COMMENT);
				result = getNextState();
			} else if(character == -1) {
				emit(EOTF);
			} else { // all other characters, should never get here
				result = ProcessingComment;
				input->advance();
			}
			break;
		case ProcessingComment:
			character = input->getCurrentCharacter();
			if(isspace(character)){ // this is a for sure end of the comment, right?*********
				result = PossibleEndOfComment;
			} else if(character == -1) {
				emit(EOTF);
			} else {// every other character
				result = ProcessingComment;
				input->advance();		// only want this after processing comment, not possibleend too?******
			}
			break;
		case ProcessingBlockComment:
			character = input->getCurrentCharacter();
			if(character == '|'){
				result = PossibleEndOfBlockComment;
			} else if(character == -1) {// illegal
				result = Undefined;
			} else {// every other character
				result = ProcessingBlockComment;
			}
			input->advance(); // good here cuz always looking for next character, whether any or #
			break;
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
				//throw "ERROR:: in case SawColon:, Expecting  '-' but found " + character + '.';
            }
            break;
        case Colon_Dash:          emit(COLON_DASH); result = getNextState(); break;
        case SawAQuote:  
            character = input->getCurrentCharacter();
            if(character == '\'') {
                result = PossibleEndOfString;
            } else if(character == -1) {
				emit(EOTF);
            } else { //Every other character
                result = ProcessingString;
            }
            input->advance();
            break;
        case ProcessingString:  
            character = input->getCurrentCharacter();
            if(character == '\'') {
                result = PossibleEndOfString;
            } else if(character == -1) {
				result = Undefined;
            } else { //Every other character
                result = ProcessingString;
            }
            input->advance();
            break;
        case PossibleEndOfString:
            if(input->getCurrentCharacter() == '\'') {
                input->advance();
                result = ProcessingString;
            } else { //Every other character
                emit(STRING);
                result = getNextState();
            }
            break;
		case ProcessingId:
			character = input->getCurrentCharacter();	
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
		case Whitespace:
			result = getNextState();
			break;
		case Undefined:
			emit(UNDEFINED);
			result = getNextState();
			break;
		


		// Keywords
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

        case End:
            throw "ERROR:: in End state:, the Input should be empty once you reach the End state."; 
            break;
    };
    return result;
}

State Lex::getNextState() {
    State result;
    char currentCharacter = input->getCurrentCharacter();
	
    //The handling of checking for whitespace and setting the result to Whilespace and
    //checking for letters and setting the result to Id will probably best be handled by
    //if statements rather then the switch statement.
	if(isspace(currentCharacter)){
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
			case '#'  : result = SawPound; break;
			case -1   : result = End; input->advance(); input->mark(); emit(EOTF); break;
			default: 
				string error = "ERROR:: in Lex::getNextState, Expecting  ";
				error += "'\'', '.', '?', '(', ')', '+', '*', 'S', 'F', 'R', 'Q', ':' but found ";
				error += currentCharacter;
				error += '.';
				//throw error.c_str();
				result = Undefined; // this correct?******************************
		}
		input->advance();
	}
    return result;
}

void Lex::emit(TokenType tokenType) {
    Token* token = new Token(tokenType, input->getTokensValue(), input->getCurrentTokensLineNumber());
    storeToken(token);
    input->mark();
}

void Lex::storeToken(Token* token) {
    //This section shoud ignore whitespace and comments and change the token type to the appropriate value
    //if the value of the token is "Schemes", "Facts", "Rules", or "Queries".
    tokens->push_back(token);
}

int main(int argc, char* argv[]) {
    Lex lex(argv[1]);
	//Lex lex("C:\\Users\\Cameron McCord\\Dropbox\\Visual Studio 2010\\Projects\\236pj1\\Debug\\TestFile4");
    cout << lex.toString();
    return 0;
}
