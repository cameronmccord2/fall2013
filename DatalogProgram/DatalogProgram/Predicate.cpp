//
//  Predicate.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//
//<Datalog Program>  ->  Schemes : <Scheme List>
//Facts   : <Fact List>
//Rules   : <Rule List>
//Queries : <Query List>
//EOF
//
//<Scheme List>     ->  <Scheme> <Scheme List>
//<Scheme List>     ->  <Scheme>
//<Scheme>          ->  <Predicate>
//
//<Fact List>       ->  <Fact> <Fact List>
//<Fact List>       ->  e
//<Fact>            ->  <Predicate> .
//
//<Rule List>       ->  <Rule> <Rule List>
//<Rule List>       ->  e
//<Rule>            ->  <Predicate> :- <Predicate List> .
//
//<Query List>      ->  <Query> <Query List>
//<Query List>      ->  <Query>
//<Query>           ->  <Predicate> ?
//
//<Predicate List>  ->  <Predicate> , <Predicate List>
//<Predicate List>  ->  <Predicate>
//<Predicate>       ->  Identifier ( <Parameter List> )
//
//<Parameter List>  ->  <Parameter> , <Parameter List>
//<Parameter List>  ->  <Parameter>
//<Parameter>       ->  String | Identifier

#include "Predicate.h"

Predicate::Predicate(DatalogProgram *dp){
    this->parameters = new vector<Parameter*>();
    this->identifier = "";
    this->parsePredicate(dp);
}

Predicate::~Predicate(){
    for (size_t i = 0; i < this->parameters->size(); i++) {
        delete this->parameters->at(i);
    }
    delete this->parameters;
}

void Predicate::parsePredicate(DatalogProgram *dp){
//    cout << "should be id" << dp->getCurrentToken()->toString();
    if (dp->getCurrentToken()->getTokenType() == ID) {
        this->identifier = dp->getCurrentToken()->getTokensValue();
        if(dp->nextToken()->getTokenType() == LEFT_PAREN){
//            cout << "in" << endl;
            this->readParameterList(dp);
            if(dp->getCurrentToken()->getTokenType() == RIGHT_PAREN){
//                cout << "should be right paren" << dp->getCurrentToken()->toString();
                dp->nextToken();
                return;
            }else
                dp->setError(dp->getCurrentToken());
        }else
            dp->setError(dp->getCurrentToken());
    }else
        dp->setError(dp->getCurrentToken());
}

void Predicate::readParameterList(DatalogProgram *dp){
//    cout << "should be left paren" << dp->getCurrentToken()->toString();
    if(dp->getCurrentToken()->getTokenType() == LEFT_PAREN){
        dp->nextToken();
        while (dp->getCurrentToken()->getTokenType() != RIGHT_PAREN && dp->isSuccessful()) {
//            cout << "should be id or string" << dp->getCurrentToken()->toString();
            Parameter *parameter = new Parameter(dp);
            this->parameters->push_back(parameter);
            if (dp->nextToken()->getTokenType() == COMMA) {// the last parameter wont have a token afterwards
                dp->nextToken();
            }
        }
//        cout << "finished reading parameter list" << endl;
        return;
    }else
        dp->setError(dp->getCurrentToken());
}

string Predicate::toString(){
    string finalString = "";
    finalString += this->identifier;
    finalString += "(";
    for (size_t i = 0; i < this->parameters->size(); i++) {
        finalString += this->parameters->at(i)->toString();
        if (i != this->parameters->size() - 1) {
            finalString += ",";
        }
    }
    finalString += ")";
    return finalString;
}
























