//
//  DatalogProgram.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DatalogProgram.h"

DatalogProgram::DatalogProgram(vector<Token*>* tokens){
    this->successful = true;
    this->offendingToken = NULL;
    this->currentToken = 0;
    this->parseTokens();
    this->tokens = tokens;
    this->factList = new FactList();
    this->schemeList = new SchemeList();
    this->ruleList = new RuleList();
    this->queryList = new QueryList();
}

DatalogProgram::~DatalogProgram(){
    // TODO delete everything
}

Token* DatalogProgram::nextToken(){
    if (this->currentToken < tokens->size()) {
        Token* token = tokens->at(this->currentToken);
        this->currentToken++;
        return token;
    }
    return NULL;
}

Token* DatalogProgram::getCurrentToken(){
    if (this->currentToken < tokens->size()) {
        return tokens->at(this->currentToken);
    }
    return NULL;
}

void DatalogProgram::setError(Token* token){
    this->offendingToken = token;
    this->successful = false;
}

void DatalogProgram::parseTokens(){
    Token* token = this->nextToken();
    switch (token->getTokenType()) {
        case SCHEMES:
            this->schemeList->parseSchemeList();
            break;
        
        case FACTS:
            this->factList->parseFactList();
            break;
            
        case RULES:
            this->ruleList->parseRuleList();
            break;
            
        case QUERIES:
            this->queryList->parseQueryList();
            break;
            
        default:
            cout << "Should never have gotten to default in parseTokens";
            break;
    }
}

string DatalogProgram::toString(){
    if (this->successful) {
        return "Success!";
    }
    return "Failure!\n" + this->offendingToken->toString();
}

int main(int argc, char* argv[]) {
	Lex *lex = NULL;
    DatalogProgram *dp = NULL;
	try{
        lex = new Lex(argv[1]);
        //	Lex lex("/users/ugrad/c/cmccord/Downloads/LexicalAnalyzer/LexicalAnalyzer/testFile1.txt");
        dp = new DatalogProgram(lex->getTokens());
        cout << dp->toString();
	}catch(exception& e){
		e.what();
	}
	delete lex;
    return 0;
}