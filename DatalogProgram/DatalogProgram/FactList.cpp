//
//  FactList.cpp
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

#include "FactList.h"
#include "DatalogProgram.h"
#include "Predicate.h"

FactList::FactList(){
    this->list = new vector<Predicate*>();
}

FactList::~FactList(){
    for (size_t i = 0; i < this->list->size(); i++) {
        delete this->list->at(i);
    }
    delete this->list;
}

void FactList::parseFactList(DatalogProgram *dp){
//    cout << "should be colon: " << dp->getCurrentToken()->toString() << endl;
    if (dp->getCurrentToken()->getTokenType() == COLON) {
        dp->nextToken();
        while (dp->getCurrentToken()->getTokenType() == ID && dp->isSuccessful()) {
            this->list->push_back(new Predicate(dp));
//            cout << "should be period" << dp->getCurrentToken()->toString();
            if(dp->getCurrentToken()->getTokenType() == PERIOD){
                dp->nextToken();
            }else
                dp->setError(dp->getCurrentToken());
        }
    }else
        dp->setError(dp->getCurrentToken());
}

string FactList::toString(){
    string finalString = "";
    for (size_t i = 0; i < this->list->size(); i++) {
        finalString += "\t";
        finalString += this->list->at(i)->toString();
        finalString += ".\n";
    }
    return finalString;
}

int FactList::getCount(){
    return (int)this->list->size();
}
























