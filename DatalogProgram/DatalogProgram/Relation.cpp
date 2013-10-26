//
//  Relation.cpp
//  DatalogProgram
//
//  Created by Cameron McCord on 10/25/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "Relation.h"

Relation::Relation(){
    this->tuples = new set<Tuple*>();
    this->variableNames = new vector<string>();
    this->queryParams = new vector<Parameter*>();
}

Relation::~Relation(){
    delete this->tuples;
    delete this->variableNames;
    delete  this->queryParams;
    delete this->relationsToDelete;
    delete this->result;
    delete this->keys;
}