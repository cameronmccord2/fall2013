//
//  DataRule.cpp
//  DatalogProgram
//
//  Created by Cameron McCord on 11/8/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DataRule.h"
#include <sstream>

DataRule::DataRule(){
    this->predicates = new vector<Relation*>();
}

DataRule::~DataRule(){
for (size_t i = 0; i < this->predicates->size(); i++) {
        delete this->predicates->at(i);
    }
    delete this->predicates;
delete this->head;
}

DataRule::DataRule(DataRule *dr){
    this->predicates = new vector<Relation*>();
    this->head = new Relation(dr->head);
    for (size_t i = 0; i < dr->predicates->size(); i++) {
        this->predicates->push_back(new Relation(dr->predicates->at(i)));
    }
}

string DataRule::toString(){
    ostringstream os = ostringstream();
    os << this->head->name << "(" << this->head->toStringOriginalQueryColumns() << ") :- ";
    for (size_t i = 0; i < this->predicates->size(); i++) {
        os << this->predicates->at(i)->name << "(" << this->predicates->at(i)->toStringOriginalQueryColumns() << ")";
        if (i != this->predicates->size() - 1) {
            os << ",";
        }
    }
    
    return os.str();
}