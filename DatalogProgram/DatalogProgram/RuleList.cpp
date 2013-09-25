//
//  RuleList.cpp
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

#include "RuleList.h"
#include "Predicate.h"
#include "DatalogProgram.h"

RuleList::RuleList(){
    this->list = new std::vector<Rule*>();
}

RuleList::~RuleList(){
    for (size_t i = 0; i < this->list->size(); i++) {
        delete this->list->at(i);
    }
    delete this->list;
}

void RuleList::parseRuleList(DatalogProgram *dp){
//    cout << "parsing rule list, should be colon" << dp->getCurrentToken()->toString();
    if (dp->getCurrentToken()->getTokenType() == COLON) {
        dp->nextToken();
        while (dp->getCurrentToken()->getTokenType() == ID && dp->isSuccessful()) {
//            cout << "should be id" << dp->getCurrentToken()->toString();
            Rule *rule = new Rule();
            rule->firstPredicate = new Predicate(dp);
//            dp->nextToken();
//            cout << "should be colon dash" << dp->getCurrentToken()->toString();
            this->parseRuleList2(dp, rule);
        }
    }else
        dp->setError(dp->getCurrentToken());
}

void RuleList::parseRuleList2(DatalogProgram *dp, Rule *rule){
    if(dp->getCurrentToken()->getTokenType() == COLON_DASH){
    //                cout << "should be colon dash" << dp->getCurrentToken()->toString();
        dp->nextToken();
        while (dp->getCurrentToken()->getTokenType() == ID && dp->isSuccessful()) {
            rule->list->push_back(new Predicate(dp));
            if (dp->getCurrentToken()->getTokenType() == COMMA) {
                dp->nextToken();
            }
        }
        if (dp->getCurrentToken()->getTokenType() == PERIOD) {
            this->list->push_back(rule);
            dp->nextToken();
        }else
            dp->setError(dp->getCurrentToken());
    }else{
        dp->setError(dp->getCurrentToken());
    }
}

std::string RuleList::toString(){
    std::string finalString = "";
    for (size_t i = 0; i < this->list->size(); i++) {
        finalString += "\t";
        finalString += this->list->at(i)->toString();
        finalString += "\n";
    }
    return finalString;
}

int RuleList::getCount(){
    return (int)this->list->size();
}




















