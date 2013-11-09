//
//  DataRule.cpp
//  DatalogProgram
//
//  Created by Cameron McCord on 11/8/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DataRule.h"

DataRule::DataRule(){
    this->predicates = new vector<Relation*>();
}

DataRule::~DataRule(){
    delete this->predicates;
}

DataRule::DataRule(DataRule *dr){
    this->predicates = new vector<Relation*>();
    this->head = new Relation(dr->head);
    for (size_t i = 0; i < dr->predicates->size(); i++) {
        this->predicates->push_back(new Relation(dr->predicates->at(i)));
    }
}