//
//  Database.cpp
//  DatalogProgram
//
//  Created by Cameron McCord on 10/25/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "Database.h"
#include "Predicate.h"
#include <fstream>

Database::Database(DatalogProgram *dp){
    this->results = new vector<Relation*>();
    this->relations = new vector<Relation*>();
    for (size_t i = 0; i < dp->schemeList->list->size(); i++) {// assemble relations
        // create relation and variable names
        Relation *r = new Relation();
        r->name = dp->schemeList->list->at(i)->identifier;
        for (size_t j = 0; j < dp->schemeList->list->at(i)->parameters->size(); j++) {
            r->variableNames->push_back(dp->schemeList->list->at(i)->parameters->at(j)->value);
        }
        this->relations->push_back(r);
        
        // assemble tuples(facts) for this relation(scheme)
        for (size_t j = 0; j < dp->factList->list->size(); j++) {
            if (r->name == dp->factList->list->at(j)->getIdentifier()) {//found tuple list for this relation
                Tuple *t = new Tuple();
                for (size_t k = 0; k < dp->factList->list->at(j)->parameters->size(); k++) {
                    t->push_back(dp->factList->list->at(j)->parameters->at(k)->value);
                }
                r->tuples->insert(t);
            }
        }
    }
    
    // generate queries
    this->queries = new vector<Relation*>();
    for (size_t i = 0; i < dp->queryList->list->size(); i++) {
        Relation *r = new Relation();
        r->name = dp->queryList->list->at(i)->identifier;
        for (size_t j = 0; j < dp->queryList->list->at(i)->parameters->size(); j++) {
            r->queryParams->push_back(dp->queryList->list->at(i)->parameters->at(j));
        }
        this->queries->push_back(r);
    }
    
    this->run();
}

Database::~Database(){
    delete this->relations;
    delete this->queries;
    delete this->results;
//    delete this->variablePositions;// always deleted after done using it in run()
}

Relation* Relation::selectConstant(int position, string value){
    Relation *r = new Relation();
    set<Tuple*>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {
        Tuple *t = *it;
        if (t->at(position) == value) {
            r->tuples->insert(t);
        }
    }
    this->relationsToDelete->push_back(r);
    return r;
}

Relation* Relation::selectVariable(int position1, int position2){// return where these two positions are the same?
    Relation *r = new Relation();
    set<Tuple*>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {
        Tuple *t = *it;
        if (t->at(position1) == t->at(position2)) {
            r->tuples->insert(t);
        }
    }
    this->relationsToDelete->push_back(r);
    return r;
}

Relation* Relation::project(int position, string toValue){// what is the difference between project and rename?// I think this is the middle-man
    this->variableNames->at(position) = toValue;
    return this;
}

Relation* Relation::rename(int position, string toValue){// this is the end result
    this->variableNames->at(position) = toValue;
    return this;
}

void Database::addVariableToSet(int position, string value){
    if (this->variablePositions->find(value) == this->variablePositions->end()) {
        this->variablePositions->insert(make_pair(value, new vector<int>()));
    }
    this->variablePositions->find(value)->second->push_back(position);
}

bool Database::run(){
    for (size_t i = 0; i < this->queries->size(); i++) {
        Relation *query = this->queries->at(i);
        this->variablePositions = new map<string, vector<int>*>();
        for (size_t j = 0; j < this->relations->size(); j++) {
            Relation *relation = this->relations->at(j);
            if (relation->name == query->name) {
                query->keys = new vector<string>();
                for (size_t k = 0; k < query->queryParams->size(); k++) {
                    Parameter *parameter = query->queryParams->at(k);
                    if (parameter->valueIsString) {
                         relation = relation->selectConstant(0, parameter->value);// gives all back that match string
                    }else{// value is a variable
                        if (parameter->value == relation->variableNames->at(k)) {// if variable name is the same as the column name in the fact list
                            this->addVariableToSet((int)k, parameter->value);
                            query->keys->push_back(parameter->value);
                        }else{// must need to project or rename, which?
                            relation->project((int)k, parameter->value);
                        }
                    }
                }
                
                // relation has had all the constants done, now do variables
                for (size_t k = 0; k < query->keys->size(); k++) {
                    vector<int> *positions = this->variablePositions->find(query->keys->at(k))->second;
                    if (positions->size() == 1) {
                        continue;
                    }
                    for (size_t l = 0; l < positions->size(); l++) {
                        relation = relation->selectVariable(positions->at(l), positions->at(l+1));
                        if (positions->size() - 2 == l) {// last one
                            break;
                        }
                    }
                }
                
                
                

                break;
            }
        }
        delete this->variablePositions;
    }
    return true;
}

string Database::toString(){
    string output = "";
    for (size_t i = 0; i < this->queries->size(); i++) {
        Relation *query = this->queries->at(i);
        
    }
    return output;
}

void Database::writeToFile(char *filename){
    try{
        ofstream myfile;
        myfile.open(filename);
        myfile << this->toString();
        myfile.close();
    }catch(exception& e){
        e.what();
    }
}































































