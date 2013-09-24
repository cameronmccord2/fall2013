//
//  Rule.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/24/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "Rule.h"
#include "Predicate.h"

Rule::Rule(){
    this->list = new std::vector<Predicate*>();
    this->firstPredicate = NULL;
}

Rule::~Rule(){
    for (size_t i = 0; i < this->list->size(); i++) {
        delete this->list->at(i);
    }
    delete this->list;
    delete this->firstPredicate;
}

std::string Rule::toString(){
    std::string finalString = this->firstPredicate->toString();
    finalString += " :- ";
    for (size_t i = 0; i < this->list->size(); i++) {
        finalString += this->list->at(i)->toString();
        if (i != this->list->size() - 1) {
            finalString += ",";
        }
    }
    finalString += ".";
    return finalString;
}