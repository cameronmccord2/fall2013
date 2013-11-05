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
#include <sstream>

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
                Tuple t = Tuple();
                for (size_t k = 0; k < dp->factList->list->at(j)->parameters->size(); k++) {
                    t.push_back(dp->factList->list->at(j)->parameters->at(k)->value);
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
            r->queryParams->push_back(DParameter(dp->queryList->list->at(i)->parameters->at(j)));
            r->originalQueryParams->push_back(DParameter(dp->queryList->list->at(i)->parameters->at(j)));
        }
        this->queries->push_back(r);
    }
    this->runRules();
    this->run();
}

void Database::runRules(){
    
}

Database::~Database(){
for(size_t i = 0; i < this->relations->size(); i++){
	delete this->relations->at(i);
}
    delete this->relations;
for(size_t i = 0; i < this->queries->size(); i++){
	delete this->queries->at(i);
}
    delete this->queries;
for(size_t i = 0; i < this->results->size(); i++){
	delete this->results->at(i);
}
    delete this->results;
//    delete this->variablePositions;// always deleted after done using it in run()
}

Relation* Relation::selectConstant(int position, string value){
    set<Tuple>* newTuples = new set<Tuple>();
    set<Tuple>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {
        Tuple t = *it;
        if (t.at(position) == value) {
            newTuples->insert(t);
        }
    }
    this->tuples->clear();
    delete  this->tuples;
    this->tuples = newTuples;
    return this;
}

Relation* Relation::selectVariable(int position1, int position2){// return where these two positions are the same?
    if (this->variableNames->at(position1) != this->variableNames->at(position2)) {
        cout << "found error";
        return this;
    }
    set<Tuple>* newTuples = new set<Tuple>();
    set<Tuple>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {
        Tuple t = *it;
        if (t.at(position1) == t.at(position2)) {
           newTuples->insert(t);
        }
    }
    delete this->tuples;
    this->tuples = newTuples;
    return this;
}

Relation* Relation::project1(vector<int>* indexes){
    vector<DParameter>* newParameters = new vector<DParameter>();
    vector<string>* newVariableNames = new vector<string>();
    int count = 0;
    bool keepEverythingElse = false;
    for (size_t i = 0; i < this->queryParams->size(); i++) {// only keep the column headers for the variable values
        if (count == (int)indexes->size()) {
            keepEverythingElse = true;
        }
        if (keepEverythingElse || i != (unsigned)indexes->at(count)) {
            newParameters->push_back(DParameter(this->queryParams->at(i)));// keep the column headers that we need
            newVariableNames->push_back(this->variableNames->at(i));
        }else
            count++;
    }
	//for(size_t i = 0; i < this->queryParams->size(); i++){
	//	delete this->queryParams->at(i);
	//}
    delete this->queryParams;
    this->queryParams = new vector<DParameter>(*newParameters);
	//for(size_t i = 0; i < newParameters->size(); i++){
	//delete newParameters->at(i);
	//}
	delete newParameters;
    delete this->variableNames;
    this->variableNames = new vector<string>(*newVariableNames);
	delete newVariableNames;
    return this;
}

Relation* Relation::project2(vector<int>* indexes){
    for (size_t i = 0; i < this->queryParams->size(); i++) {// get indexes of non-variables
        DParameter p = this->queryParams->at(i);
        if(p.valueIsString){
            indexes->push_back((int)i);
        }
    }
    return this;
}

Relation* Relation::project(){// removes constants that we have already found
    vector<int>* indexes = new vector<int>();// indexes of non-variables
    this->project2(indexes);
    if (indexes->size() == 0) {
        delete indexes;
        return this;
    }
    int count = 0;
    set<Tuple>* newTuples = new set<Tuple>();
    set<Tuple>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {// for each tuple
        Tuple t = *it;
        count = 0;
        Tuple newTuple = Tuple();
        bool keepEverythingElse = false;
        for (size_t i = 0; i < this->queryParams->size(); i++) {// for each value in the query param/tuple
            if (count == (int)indexes->size()) {
                keepEverythingElse = true;// finished
            }
            if (keepEverythingElse || i != (unsigned)indexes->at(count)) {
                newTuple.push_back(t.at(i));// keep the values in the tuples that are variables in the query
            }else
                count++;
        }
        newTuples->insert(newTuple);
    }
    
    delete this->tuples;// delete the old set
    this->tuples = newTuples;
    this->project1(indexes);
    delete indexes;
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

Relation* Relation::reduceSideways2(Relation *query, Database *db){
    bool finishedRemovingDuplicates = false;
    while (!finishedRemovingDuplicates) {
        set<int> pos = set<int>();
        for (size_t k = 0; k < this->variableNames->size(); k++) {// itemize duplicate variables
            for (size_t l = 0; l < this->variableNames->size(); l++) {
                if (l <= k) {
                    continue;
                }
                if (this->variableNames->at(k) == this->variableNames->at(l)) {
                    pos.insert((int)l);
                }
            }
        }
        
        if (pos.size() == 0) {
            finishedRemovingDuplicates = true;
            continue;
        }
        // delete duplicates
        set<int>::reverse_iterator rit;
        for (rit = pos.rbegin(); rit != pos.rend(); ++rit) {// reverse loop positions that need to be deleted
            int t = *rit;
            
            this->variableNames->erase(this->variableNames->begin() + t);
            
            this->deleteTupleValueAtPosition(t);
        }
    }
    return this;
}

void Relation::generateKeysAndVariablePositions(vector<string>* keys, map<string, vector<int>*>* variablePositions, Database *db){
    for (size_t k = 0; k < this->queryParams->size();) {// check each param in the query
        DParameter parameter = this->queryParams->at(k);
        if (parameter.valueIsString) {
            this->selectConstant((int)k, parameter.value);// gives all tuples back that match string for that column
            k++;
        }else{// value is a variable
            //   query column             table column
            if (parameter.value == this->variableNames->at(k)) {
                if (variablePositions->find(parameter.value) == variablePositions->end()) {
                    variablePositions->insert(make_pair(parameter.value, new vector<int>()));
                }
                variablePositions->find(parameter.value)->second->push_back((int)k);

                keys->push_back(parameter.value);// add to later check for possible duplicate keys
                k++;
            }else{
                this->rename((int)k, parameter.value);// must need to rename the column name to be the query's variable name
            }
        }
    }
}

Relation* Relation::reduceSideways(Relation *query, Database *db){
    vector<string>* keys = new vector<string>();
    map<string, vector<int>*>* variablePositions = new map<string, vector<int>*>();
    this->generateKeysAndVariablePositions(keys, variablePositions, db);
    // relation has had all the constants removed, now do variables, find any that are the same
    for (size_t k = 0; k < keys->size(); k++) {
        vector<int> *positions = variablePositions->find(keys->at(k))->second;
        if (positions->size() == 1) {
            continue;
        }
        for (size_t l = 0; l < positions->size(); l++) {// keep the tuples that have the same value in both positions
            this->selectVariable(positions->at(0), positions->at(l+1));// always compare the 0 position to whatever is next
            if (positions->size() - 2 == l) {// last one
                break;
            }
        }
    }
    
    delete keys;
    map<string, vector<int>*>::iterator it;
    for (it = variablePositions->begin(); it != variablePositions->end(); ++it) {//loop through map and delete variable positions for this query
        delete it->second;
    }
    delete variablePositions;
    
    this->reduceSideways2(query, db);
    
    delete query->tuples;
    query->tuples = new set<Tuple>(*this->tuples);
	delete query->variableNames;
    query->variableNames = new vector<string>(*this->variableNames);
    return query;
}

Relation* Relation::reduceVertical(Relation* query, Database *db){
    for (size_t a = 0; a < this->variableNames->size(); a++) {
        this->variableNames->at(a) = this->variableNames->at(a) + "Cameron";
    }
    
    delete this->queryParams;
    this->queryParams = new vector<DParameter>(*query->queryParams);
    query->keys = new vector<string>();// This itemizes the query's variable names, later we can keep all tuples that are the same for keys that have multiple indexes in the result data columns - slam(X, X, A)// make sure Xs are the same
    
    for (size_t k = 0; k < query->queryParams->size();) {// check each param in the query
        DParameter parameter = query->queryParams->at(k);
        if (parameter.valueIsString) {
            this->selectConstant((int)k, parameter.value);// gives all tuples back that match string for that column
            k++;
        }else{// value is a variable
            //   query column             table column
            if (parameter.value == this->variableNames->at(k)) {
                db->addVariableToSet((int)k, parameter.value);// variable name in the query is the same as the column name in the fact list
                query->keys->push_back(parameter.value);// add to later check for possible duplicate keys
                k++;
            }else{
                this->rename((int)k, parameter.value);// must need to rename the column name to be the query's variable name
            }
        }
    }
    return this;
}

bool Database::run(){
    for (size_t i = 0; i < this->queries->size(); i++) {// run all queries
        Relation *query = this->queries->at(i);
        this->variablePositions = new map<string, vector<int>*>();
        
        for (size_t j = 0; j < this->relations->size(); j++) {// find the relation matching this query
            Relation *relation = new Relation(this->relations->at(j));
            
            if (relation->name == query->name) {// this should only ever happen once
                
                relation->reduceVertical(query, this);
                
                relation->project();// do projection, remove all constants in the result data, these we dont care about now
                
                query = relation->reduceSideways(query, this);// doesnt do anything with query params
		delete query->keys;
            }
		delete relation;
        }
	map<string, vector<int>*>::iterator it;
    for (it = this->variablePositions->begin(); it != this->variablePositions->end(); ++it) {//loop through map and delete variable positions for this query
        delete it->second;
    }
        delete this->variablePositions;
//        cout << this->toString();
    }
//    cout << this->toString();
    return true;
}

Relation* Relation::deleteTupleValueAtPosition(int pos){
    set<Tuple>* newTuples = new set<Tuple>();
    set<Tuple>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {//loop through tuples and delete current position
        Tuple tuple = *it;
        Tuple newTuple = Tuple();
        for (size_t i = 0; i < tuple.size(); i++) {
            if (i != (unsigned)pos) {
                newTuple.push_back(tuple.at(i));
            }
        }
        newTuples->insert(newTuple);
    }
    delete this->tuples;
    this->tuples = newTuples;
    return this;
}

string Relation::toStringRemainingTuples(){
    string output = "";
    set<Tuple>::iterator it;
    for (it = this->tuples->begin(); it != this->tuples->end(); ++it) {
        if(this->variableNames->size() > 0)
            output += "  ";
        Tuple t = *it;
        for (size_t i = 0; i < this->variableNames->size(); i++) {
            output += this->variableNames->at(i) + "='" + t.at(i) + "'";
            if (i != this->variableNames->size() - 1) {
                output += ", ";
            }else
                output += "\n";
        }
    }
    return output;
}

string Relation::toStringOriginalQueryColumns(){
    string output = "";
    for (size_t i = 0; i < this->originalQueryParams->size(); i++) {
        if(this->originalQueryParams->at(i).valueIsString)
            output += "'";
        output += this->originalQueryParams->at(i).value;
        if(this->originalQueryParams->at(i).valueIsString)
            output += "'";
        if (i != this->originalQueryParams->size() - 1) {
            output += ",";
        }
    }
    return output;
}

string Database::toString(){
    ostringstream oss;
    for (size_t i = 0; i < this->queries->size(); i++) {
        Relation *query = this->queries->at(i);
        oss << query->name << "(" << query->toStringOriginalQueryColumns() << ")? ";
        if (query->tuples->size() > 0) {
            oss << "Yes(" << query->tuples->size() << ")" << "\n" << query->toStringRemainingTuples();;
        }else
            oss << "NO\n";
    }
    return oss.str();
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































































