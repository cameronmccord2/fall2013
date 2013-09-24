//
//  DatalogProgram.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DatalogProgram.h"
#include <sstream>

DatalogProgram::DatalogProgram(vector<Token*>* tokens){
    this->successful = true;
    this->offendingToken = NULL;
    this->currentToken = 0;
    this->tokens = tokens;
    this->factList = new FactList();
    this->schemeList = new SchemeList();
    this->ruleList = new RuleList();
    this->queryList = new QueryList();
    this->domainList = new DomainList();
    // after all setup
    this->parseTokens();
}

DatalogProgram::~DatalogProgram(){
    // TODO delete everything
}

Token* DatalogProgram::nextToken(){
    if (this->currentToken < tokens->size()) {
        this->currentToken++;
        return tokens->at(this->currentToken);
    }
    return NULL;
}

void DatalogProgram::setDomainElement(string element){
    this->domainList->setDomainElement(element);
}

Token* DatalogProgram::getCurrentToken(){
    if (this->currentToken < tokens->size()) {
        return tokens->at(this->currentToken);
    }
    return NULL;
}

void DatalogProgram::setError(Token* token){
    if(this->offendingToken != NULL)
        return;
    this->offendingToken = token;
    this->successful = false;
}

bool DatalogProgram::isSuccessful(){
    return this->successful;
}

void DatalogProgram::parseTokens(){
    while (this->getCurrentToken()->getTokenType() != EOTF && this->isSuccessful()) {
//        cout << "token type: " << this->getCurrentToken()->getTokenType() << ", facts: " << FACTS << endl;
        switch (this->getCurrentToken()->getTokenType()) {
            case SCHEMES:
//                cout << "going to read shcememe";
                this->nextToken();
                this->schemeList->parseSchemeList(this);
//                cout << "after after schemem list" << this->getCurrentToken()->toString();
                break;
                
            case FACTS:
//                cout << "going to read facts" << endl;
                this->nextToken();
                this->factList->parseFactList(this);
                break;
                
            case RULES:
//                cout << "going to read rules" << endl;
                this->nextToken();
                this->ruleList->parseRuleList(this);
                break;
                
            case QUERIES:
//                cout << "going to read queries" << endl;
                this->nextToken();
                this->queryList->parseQueryList(this);
                break;
                
            default:
//                cout << "failed on default" << this->getCurrentToken()->toString();
                this->setError(this->getCurrentToken());
                break;
        }
    }
    if (this->getCurrentToken()->getTokenType() == EOTF) {
        return;
    }else if (this->isSuccessful()){
//        cout << "should never have gotten here parseTokens isSuccessful at end" << endl;
        this->setError(this->getCurrentToken());
    }
}

string DatalogProgram::toString(){
    if (this->successful) {
        ostringstream oss;
        oss << "Success!\n" << "Schemes(" << this->schemeList->getCount() << "):\n" << this->schemeList->toString() << "Facts(" << this->factList->getCount() << "):\n" << this->factList->toString() << "Rules(" << this->ruleList->getCount() << "):\n" << this->ruleList->toString() << "Queries(" << this->queryList->getCount() << "):\n" << this->queryList->toString() << "Domain(" << this->domainList->getCount() << "):\n" << this->domainList->toString();
        return oss.str();
    }else
        return "Failure!\n\t" + this->offendingToken->toString();
}

int main(int argc, char* argv[]) {
	Lex *lex = NULL;
    DatalogProgram *dp = NULL;
	try{
//        lex = new Lex(argv[1]);
        lex = new Lex("/Users/taylormccord/Dropbox/Eclipse Workspace/fall2013/DatalogProgram/DatalogProgram/in21.txt");
        dp = new DatalogProgram(lex->getTokens());
        cout << dp->toString();
	}catch(exception& e){
		e.what();
	}
	delete lex;
    delete dp;
    return 0;
}