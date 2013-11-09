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
    cout << "before generate rules" << endl;
    this->generateRules(dp);
    cout << "generated rules" << endl;
    cout << this->masterRules->size() << endl;
    unsigned long count = this->getFactsCount();
    this->passesThroughRules = 0;
    do {
        cout << "ran times: " << this->passesThroughRules << ", count: " << count << ", relations size: " << this->getFactsCount() << endl;
        this->passesThroughRules++;
        count = this->getFactsCount();
        this->runRules();
    } while (count != this->getFactsCount());// run until no more facts are generated
    cout << "times ran: " << this->passesThroughRules << endl;
    this->run(this->queries, true);
    this->printTuplesForRelation(this->relations, "cn");
}

void Database::printTuplesForRelation(vector<Relation*>* list, string schemaName){
    cout << "tuples for schema: " << schemaName << endl;
    for (size_t i = 0; i < list->size(); i++) {
        
        if (list->at(i)->name == schemaName) {
            set<Tuple>::iterator it;
            for (it = list->at(i)->tuples->begin(); it != list->at(i)->tuples->end(); ++it) {
                Tuple t = *it;
                cout << this->tupleToString(t) << endl;
            }
            break;
        }
    }
}

unsigned long Database::getFactsCount(){
    unsigned long count = 0;
    for (size_t i = 0; i < this->relations->size(); i++) {
        count += this->relations->at(i)->tuples->size();
    }
    return count;
}

void Database::runRules(){
    this->copyRules();
    
    for (size_t i = 0; i < this->rules->size(); i++) {
        this->run(this->rules->at(i)->predicates, false);
        this->joinRule(this->rules->at(i));
    }
    
    this->deleteRulesCopy();
}

void Database::joinRule(DataRule *dr){
//    cout << "join rule: " << dr->head->name << endl;
    Relation *r1 = new Relation(dr->predicates->at(0));
    for (size_t i = 1; i < dr->predicates->size(); i++) {
        Relation *r2 = new Relation(dr->predicates->at(i));
        // generate matching columns
        set<pair<int, int>>* matches = this->findMatchingColumns(r1, r2);
        unsigned long size = r1->variableNames->size() + r2->variableNames->size() - matches->size();
//        cout << "generated matches: " << matches->size() << endl;
        // keep only the columns that match in the tuples
        r1 = this->keepGoodTuples(r1, r2, matches);
//        cout << "tuples returned: " << r1->tuples->size() << endl;
        if(r1->variableNames->size() != size)
            cout << "r1 variable names size: " << r1->variableNames->size() << ", should be: " << size << ", is this a valid assumpsion?" << endl;
        delete r2;
        delete matches;
    }
    // add to facts
    this->simplifyToHeadAndAddToFacts(dr, r1);
    delete r1;
}

void Database::simplifyToHeadAndAddToFacts(DataRule *dr, Relation *r){
    vector<unsigned long> indexes = vector<unsigned long>();
    set<unsigned long> indexesSet = set<unsigned long>();
//    cout << "dr head variable names size: " << dr->head->variableNames->size() << endl;
//    cout << "relation variable names size: " << r->variableNames->size() << endl;
    for (size_t i = 0; i < dr->head->variableNames->size(); i++) {
        for (size_t j = 0; j < r->variableNames->size(); j++) {
            if (dr->head->variableNames->at(i) == r->variableNames->at(j)) {
                indexes.push_back(j);
                indexesSet.insert(j);
                cout << "index kept: " << j << ", for variable name: " << dr->head->variableNames->at(i) << endl;
                break;
            }
        }
    }
//    cout << "index set size:" << indexesSet.size() << ", indxes size: " << indexes.size() << endl;
    set<Tuple>* newTuples = new set<Tuple>();
    set<Tuple>::iterator it;
    for (it = r->tuples->begin(); it != r->tuples->end(); ++it) {
        Tuple t = *it;
        Tuple newTuple = Tuple();
        cout << this->tupleToString(t) << endl;
        
        for (size_t i = 0; i < indexes.size(); i++) {
            cout << "kept value: " << t.at(indexes.at(i)) << ", at index: " << indexes.at(i) << endl;
            newTuple.push_back(t.at(indexes.at(i)));
        }
//        cout << "new tuple: " << this->tupleToString(newTuple) << endl;
//        for (size_t i = 0; i < t.size(); i++) {
//            if (indexesSet.find(i) != indexesSet.end()) {
//                cout << "index: " << i << endl;
//                newTuple.push_back(t.at(i));
//            }
//        }
        newTuples->insert(newTuple);
        cout << "result tuple: " << this->tupleToString(newTuple) << endl;
    }
    cout << "new tuples after simplify: " << newTuples->size() << endl;
    this->insertTuplesIntoRelation(dr->head, newTuples);
    newTuples->empty();
    delete newTuples;
}

void Database::insertTuplesIntoRelation(Relation *r, set<Tuple>* tuples){
    for (size_t i = 0; i < this->relations->size(); i++) {
        if (this->relations->at(i)->name == r->name) {// find the relation/schema these have to go to
//            cout << "schema: " << r->name << ", tuple count before: " << this->relations->at(i)->tuples->size() << endl;
//            cout << "tupes to insert: " << tuples->size() << endl;
            set<Tuple>::iterator it;
            for (it = tuples->begin(); it != tuples->end(); ++it) {
                Tuple t = *it;
                this->relations->at(i)->tuples->insert(t);
            }
//            cout << "tuple count after: " << this->relations->at(i)->tuples->size() << endl;
            break;
        }
    }
}

set<pair<int, int>>* Database::findMatchingColumns(Relation *r1, Relation *r2){
    set<pair<int, int>>* matches = new set<pair<int, int>>();
    for (size_t i = 0; i < r1->variableNames->size(); i++) {
        string name1 = r1->variableNames->at(i);
        for (size_t j = 0; j < r2->variableNames->size(); j++) {
            if (name1 == r2->variableNames->at(j)) {
//                cout << "matches at: " << i << ", " << j << endl;
                matches->insert(pair<int, int>(i, j));
            }
        }
    }
    return matches;
}

Relation* Database::keepGoodTuples(Relation *r1, Relation *r2, set<pair<int, int>>* matches){
    Relation *r = new Relation();
    r->variableNames = new vector<string>(*r1->variableNames);// make sure this isnt broken - variable names !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    bool doVariableNames = true;
    
    set<Tuple>::iterator it2;
    for (it2 = r1->tuples->begin(); it2 != r1->tuples->end(); ++it2) {
        Tuple t1 = *it2;
        
        set<Tuple>::iterator it3;
        for (it3 = r2->tuples->begin(); it3 != r2->tuples->end(); ++it3) {
           
            Tuple t2 = *it3;
            
            set<int> ints = set<int>();
            bool keepTuple = true;
            set<pair<int, int>>::iterator it1;
            for (it1 = matches->begin(); it1 != matches->end(); ++it1) {
                pair<int, int> pair = *it1;
                
                ints.insert(pair.second);
                if (t1.at(pair.first) != t2.at(pair.second)) {
                    keepTuple = false;
                    break;
                }
            }
            if (keepTuple) {
                Tuple t = Tuple(t1);
                for (size_t i = 0; i < t2.size(); i++) {
                    if (ints.find((int)i) == ints.end()) {
                        t.push_back(t2.at(i));
//                        cout << "value kept: " << t2.at(i) << endl;
                        if (doVariableNames) {
                            r->variableNames->push_back(r2->variableNames->at(i));
//                            cout << "variable name kept: " << r2->variableNames->at(i) << endl;
                        }
                    }
                }
                r->tuples->insert(t);
//                cout << this->tupleToString(t) << endl;
                doVariableNames = false;
            }
        }
    }
//    cout << "tuples: " << r->tuples->size() << endl;
    delete r1;
    return r;
}



void Database::copyRules(){
    this->rules = new vector<DataRule*>();
    for (size_t i = 0; i < this->masterRules->size(); i++) {
        DataRule *dr = this->masterRules->at(i);
        this->rules->push_back(new DataRule(dr));
    }
}

void Database::deleteRulesCopy(){
    for (size_t i = 0; i < this->rules->size(); i++) {
        delete this->rules->at(i);
    }
    delete this->rules;
}

void Database::generateRules(DatalogProgram *dp){
    this->masterRules = new vector<DataRule*>();
    for (size_t i = 0; i < dp->ruleList->list->size(); i++) {
        DataRule *d = new DataRule();
        d->head = new Relation();
        d->head->name = dp->ruleList->list->at(i)->firstPredicate->getIdentifier();
        for (size_t j = 0; j < dp->ruleList->list->at(i)->firstPredicate->parameters->size(); j++) {
            d->head->queryParams->push_back(DParameter(dp->ruleList->list->at(i)->firstPredicate->parameters->at(j)));
            d->head->originalQueryParams->push_back(DParameter(dp->ruleList->list->at(i)->firstPredicate->parameters->at(j)));
            d->head->variableNames->push_back(dp->ruleList->list->at(i)->firstPredicate->parameters->at(j)->value);
        }
        
        for (size_t j = 0; j < dp->ruleList->list->at(i)->list->size(); j++) {
            Predicate *pred = dp->ruleList->list->at(i)->list->at(j);
            Relation *r = new Relation();
            r->name = pred->identifier;
            for (size_t k = 0; k < pred->parameters->size(); k++) {
                r->originalQueryParams->push_back(pred->parameters->at(k));
                r->queryParams->push_back(pred->parameters->at(k));
            }
            d->predicates->push_back(r);
        }
        this->masterRules->push_back(d);
//        cout << "dr head variable names size: " << d->head->variableNames->size() << endl;
    }
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
    
    for (size_t i = 0; i < this->masterRules->size(); i++) {
        delete this->masterRules->at(i);
    }
    delete this->masterRules;
    
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

bool Database::run(vector<Relation*>* list, bool doProject){
    for (size_t i = 0; i < list->size(); i++) {// run all queries
        Relation *query = list->at(i);
        this->variablePositions = new map<string, vector<int>*>();
        
        for (size_t j = 0; j < this->relations->size(); j++) {// find the relation matching this query
            Relation *relation = new Relation(this->relations->at(j));
            
            if (relation->name == query->name) {// this should only ever happen once
                cout << "run ran" << relation->name << endl;
                relation->reduceVertical(query, this);
                
                if(doProject)
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
    oss << "Schemes populated after " << this->passesThroughRules << " passes through the Rules.\n";
    for (size_t i = 0; i < this->queries->size(); i++) {
        Relation *query = this->queries->at(i);
        oss << query->name << "(" << query->toStringOriginalQueryColumns() << ")? ";
        if (query->tuples->size() > 0) {
            oss << "Yes(" << query->tuples->size() << ")" << "\n" << query->toStringRemainingTuples();;
        }else
            oss << "NO\n";
    }
    oss << "Done!";
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

string Database::tupleToString(Tuple t){
    string output = "";
    for (size_t i = 0; i < t.size(); i++) {
        output += t.at(i);
        output += " ";
    }
    return output;
}































































