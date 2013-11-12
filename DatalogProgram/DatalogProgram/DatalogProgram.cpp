//
//  DatalogProgram.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DatalogProgram.h"
#include <sstream>
#include <iostream>
#include <fstream>
// nothing in rule list after :-

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
    delete this->factList;
    delete this->schemeList;
    delete this->ruleList;
    delete this->queryList;
    delete this->domainList;
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
                
		if(this->getCurrentToken()->getTokenType() == SCHEMES){
this->nextToken();
                	this->schemeList->parseSchemeList(this);
		}else
			this->setError(this->getCurrentToken());
                
if(this->getCurrentToken()->getTokenType() == FACTS){
this->nextToken();
                this->factList->parseFactList(this);
}else
			this->setError(this->getCurrentToken());
                
if(this->getCurrentToken()->getTokenType() == RULES){
this->nextToken();
                this->ruleList->parseRuleList(this);
}else
			this->setError(this->getCurrentToken());
                
if(this->getCurrentToken()->getTokenType() == QUERIES){
this->nextToken();
                this->queryList->parseQueryList(this);
}else
			this->setError(this->getCurrentToken());
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

void DatalogProgram::writeToFile(char* filename){
    try{
        ofstream myfile;
        myfile.open(filename);
        myfile << this->toString();
        myfile.close();
    }catch(exception& e){
        e.what();
    }
}

int main(int argc, char* argv[]) {
	Lex *lex = NULL;
    DatalogProgram *dp = NULL;
    Database *d = NULL;
    string temp;
	try{
        lex = new Lex(argv[1]);
//        cout << "started" << endl;
//        lex = new Lex("/Users/cameronmccord2/Dropbox/Eclipse Workspace/fall2013/DatalogProgram/DatalogProgram/in42.txt");
        dp = new DatalogProgram(lex->getTokens());
        d = new Database(dp);
        
//        cout << d->toString();
        //cin >> temp;
        d->writeToFile(argv[2]);
//        dp->writeToFile(argv[2]);
        //cout << dp->toString();
	}catch(exception& e){
		e.what();
	}
	delete lex;
    delete dp;
    delete d;
    return 0;
}
