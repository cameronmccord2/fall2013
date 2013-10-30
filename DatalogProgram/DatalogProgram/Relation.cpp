//
//  Relation.cpp
//  DatalogProgram
//
//  Created by Cameron McCord on 10/25/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "Relation.h"

Relation::Relation(){
    this->tuples = new set<Tuple>();
    this->variableNames = new vector<string>();
    this->queryParams = new vector<Parameter*>();
    this->originalQueryParams = new vector<Parameter*>();
    this->relationsToDelete = new vector<Relation*>();
}

Relation::Relation(Relation *old){
    this->tuples = new set<Tuple>(*old->tuples);
    this->variableNames = new vector<string>(*old->variableNames);
    this->queryParams = new vector<Parameter*>(*old->queryParams);
    this->originalQueryParams = new vector<Parameter*>(*old->originalQueryParams);
    this->relationsToDelete = new vector<Relation*>();
    this->name = old->name;
}

Relation::~Relation(){
    delete this->tuples;
    delete this->variableNames;
    delete this->queryParams;
    delete this->relationsToDelete;
    delete this->result;
    delete this->keys;
    delete this->originalQueryParams;
}