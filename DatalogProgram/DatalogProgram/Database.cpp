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
#include <algorithm>

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
    this->generateRules(dp);
    this->makeGraph();
//    cout << this->dependencyGraphToString();
    
    this->runQueries();
    
    
    
    
    
    
//    this->runRulesLoop();
    this->run(this->queries, true);
}

bool sortNodes(Node* n1, Node* n2){
    return (n1->identifier < n2->identifier);
}

int stringToInt(string s){
    int i;
    istringstream ( s ) >> i;
    return i;
}

void Database::runQueries(){
    // run queries one at a time
    bool runningAgain = false;
    for (size_t a = 0; a < this->nodes.size(); a++) {
        unsigned long count = this->getFactsCount();
        this->postOrderNumber = 1;
        this->cycleFound = false;
        Node* queryNode = this->nodes.at(a);
        queryNode->postOrderPairs = vector<pair<string, int> >();// reset this everytime so we dont get duplicate pairs
        queryNode->postOrderPairsRules = vector<pair<string, int> >();
        queryNode->childNodesWithBackwardsEdges = map<Node*, vector<string> >();
        this->clearPostOrderNumbers();
        queryNode->ruleEvaluationOrder = vector<string>();
        if (!runningAgain) {
            queryNode->backwardsEdges = map<string, string>();
            this->currentQuery = queryNode;
        }
        this->runRuleDepthFirstSearch(queryNode, "doesnt matter");
        if (this->cycleFound && count != this->getFactsCount()) {// rerun if cycle was found and if the number of facts changed on the last pass through
            a--;// rerun this query again
            runningAgain = true;
        }else
            runningAgain = false;
    }
}

void Database::clearPostOrderNumbers(){
    for (size_t i = 0; i < this->nodes.size(); i++) {
        this->nodes.at(i)->postOrderNumber = 0;
        this->nodes.at(i)->alreadyVisited = false;
    }
}

void Database::addToChildNodesWithBackwardsEdges(Node* node, string dependencyString){
    if (this->currentQuery->childNodesWithBackwardsEdges.find(node) == this->currentQuery->childNodesWithBackwardsEdges.end()) {
        this->currentQuery->childNodesWithBackwardsEdges.insert(pair<Node*, vector<string> >(node, vector<string>()));
    }
    this->currentQuery->childNodesWithBackwardsEdges.find(node)->second.push_back(dependencyString);
}

bool isStringInVector(vector<string> v, string s){
    for (int i =0; i < (int)v.size(); i++) {
        if (v.at(i) == s) {
            return true;
        }
    }
    return false;
}

void Database::runRuleDepthFirstSearch(Node *node, string ruleJustCameFrom){
    set<string>::iterator it;
    for (it = node->dependencies.begin(); it != node->dependencies.end(); ++it) {
        string dependencyString = *it;
//        cout << this->tupleToString(t) << endl;
//    }
//    for (size_t i = 0; i < node->dependencies.size(); i++) {
//        string dependencyString = node->dependencies.at(i);
        Node* ruleNode = this->nodeMap.find(dependencyString)->second;
        node->alreadyVisited = true;
        if (ruleNode->postOrderNumber != 0) {
//            continue;// already been seen
        }else if (ruleJustCameFrom == ruleNode->identifier) {// backwards edge found
//            this->currentQuery->backwardsEdges.insert(pair<string, string>(dependencyString, ruleJustCameFrom));
            this->addToChildNodesWithBackwardsEdges(node, dependencyString);
//            this->currentQuery->childNodesWithBackwardsEdges.insert(node);
//            node->backwardsEdgesList.push_back(dependencyString);
            node->hasBackwardEdge = true;
            node->backwardEdge = ruleNode;
            this->cycleFound = true;
        }else if(isStringInVector(node->backwardsEdgesList, dependencyString)){
//        }else if(find(node->backwardsEdgesList.begin(), node->backwardsEdgesList.end(), dependencyString) != node->backwardsEdgesList.end()){
            // found in backwards edges list, dont go there
            cout << "found: " << dependencyString;
        }else if (ruleNode->alreadyVisited) {
//            if (this->currentQuery->childNodesWithBackwardsEdges.find(node) == this->currentQuery->childNodesWithBackwardsEdges.end()) {
//                this->currentQuery->childNodesWithBackwardsEdges.insert(pair<Node*, vector<string> >(node, vector<string>()));
//            }
//            this->currentQuery->childNodesWithBackwardsEdges.find(node)->second.push_back(dependencyString);
            this->addToChildNodesWithBackwardsEdges(node, dependencyString);
            //            node->backwardsEdgesList.push_back(ruleNode->identifier);
            node->hasBackwardEdge = true;
            node->backwardEdge = ruleNode;
            this->cycleFound = true;
        }else {
            this->runRuleDepthFirstSearch(ruleNode, node->identifier);
        }
    }
    node->postOrderNumber = this->postOrderNumber;
    if (node->isQuery) {
        this->currentQuery->postOrderPairs.push_back(pair<string, int>(node->identifier, this->postOrderNumber));
    }else
        this->currentQuery->postOrderPairsRules.push_back(pair<string, int>(node->identifier, this->postOrderNumber));
    this->postOrderNumber++;
    
    // run this rule
    if (!node->isQuery) {
        this->runRulesLoop(node->dataRule, 0);
    }
}



void Database::makeGraph(){
    this->nodes = vector<Node*>();
    this->nodeMap = map<string, Node*>();
    
    for (size_t i = 0; i < this->queries->size(); i++) {
        this->makeNodeFromQueryRelation(this->queries->at(i), (int)i);
    }
    for (size_t i = 0; i < this->masterRules->size(); i++) {
        this->makeNodeFromRule(this->masterRules->at(i), (int)i);
    }
    
}

void Database::makeNodeFromQueryRelation(Relation* query, int queryNum){
    ostringstream os;
    os << "Q" << (queryNum + 1);
    // find or make the node from the datarule
    map<string, Node*>::iterator it = this->nodeMap.find(os.str());
    if (it == this->nodeMap.end()) {// not found, make it
        Node *node = new Node(os.str());
        // resolve dependency stuff
        node->query = query;
        node->isQuery = true;
        node->dependencies = set<string>();
        // make dependencies
        vector<string> ids = this->getAllRuleIdsForName(query->name);
        for (size_t j = 0; j < ids.size(); j++) {// add them to the dependency graph for this node, may be none
            node->dependencies.insert(ids.at(j));
        }
        this->nodeMap.insert(pair<string, Node*>(os.str(), node));
        this->nodes.push_back(node);
    }else
        return; // found it so we dont have to make it. all of it's dependency stuff should have already been done
}

void Database::makeNodeFromRule(DataRule *dr, int ruleNum){
    ostringstream os;
    os << "R" << (ruleNum + 1);
    // find or make the node from the datarule
    map<string, Node*>::iterator it = this->nodeMap.find(os.str());
    if (it == this->nodeMap.end()) {// not found, make it
        Node *node = new Node(os.str());
        // resolve dependency stuff
        node->dataRule = dr;
        node->isQuery = false;
        node->dependencies = set<string>();
        // make dependencies
        for (size_t i = 0; i < dr->predicates->size(); i++) {
            Relation *predicate = dr->predicates->at(i);
            vector<string> ids = this->getAllRuleIdsForName(predicate->name);
            for (size_t j = 0; j < ids.size(); j++) {// add them to the dependency graph for this node, may be none
                node->dependencies.insert(ids.at(j));
            }
        }
        this->nodeMap.insert(pair<string, Node*>(os.str(), node));
        this->nodes.push_back(node);
    }else
        return; // found it so we dont have to make it. all of it's dependency stuff should have already been done
    
}

vector<string> Database::getAllRuleIdsForName(string name){
    vector<string> rules = vector<string>();
    for (size_t i = 0;  i < this->masterRules->size(); i++) {
        if (this->masterRules->at(i)->head->name == name) {
            ostringstream os;
            os << "R" << (i + 1);
            rules.push_back(os.str());
        }
    }
    return rules;
}

Node* Database::getOrMakeNodeForDataRule(DataRule *dr, int ruleNum){
    ostringstream os;
    os << "R" << ruleNum;
    string nodeIdentifier = os.str();
    for (size_t i = 0; i < this->nodes.size(); i++) {
        Node* node = this->nodes.at(i);
        if (node->identifier == nodeIdentifier) {
            return node;
        }
    }
    return new Node(nodeIdentifier);
}

void Database::runRulesLoop(DataRule *rule, int ruleNum){
//	unsigned long count = this->getFactsCount();
//    this->passesThroughRules = 0;
//    do {
//        this->passesThroughRules++;
//        count = this->getFactsCount();
        this->runRules(rule, ruleNum);
//        if (count != this->getFactsCount()) {
//            this->currentQuery->rulesEvaluated.push_back(rule->toString());
//        }
//    } while (count != this->getFactsCount());// run until no more facts are generated
}

void Database::printTuplesForRelation(vector<Relation*>* list, string schemaName){
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

void Database::runRules(DataRule * rule, int ruleNum){
//    this->copyRules(); only for lab 4
//    for (size_t i = 0; i < rules->size(); i++) {
//        this->run(this->rules->at(i)->predicates, false);
//        this->joinRule(this->rules->at(i));
//    }
//    
//    this->deleteRulesCopy();
    ostringstream os;
    os << "R" << rule->ruleNum;
    this->currentQuery->ruleEvaluationOrder.push_back(os.str());
    this->currentQuery->rulesEvaluated.push_back(rule->toString()); // this isnt correct yet, either I am printing incorrectly or the rules are running too many times
    this->run(rule->predicates, false);
    this->joinRule(rule);
}

void Database::joinRule(DataRule *dr){
    Relation *r1 = new Relation(dr->predicates->at(0));
    for (size_t i = 1; i < dr->predicates->size(); i++) {
        Relation *r2 = new Relation(dr->predicates->at(i));
        // generate matching columns
        set<pair<int, int> >* matches = this->findMatchingColumns(r1, r2);
        // keep only the columns that match in the tuples
        r1 = this->keepGoodTuples(r1, r2, matches);
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
    for (size_t i = 0; i < dr->head->variableNames->size(); i++) {
        for (size_t j = 0; j < r->variableNames->size(); j++) {
            if (dr->head->variableNames->at(i) == r->variableNames->at(j)) {
                indexes.push_back(j);
                indexesSet.insert(j);
                break;
            }
        }
    }
    set<Tuple>* newTuples = new set<Tuple>();
    set<Tuple>::iterator it;
    for (it = r->tuples->begin(); it != r->tuples->end(); ++it) {
        Tuple t = *it;
        Tuple newTuple = Tuple();
        
        for (size_t i = 0; i < indexes.size(); i++) {
            newTuple.push_back(t.at(indexes.at(i)));
        }
        newTuples->insert(newTuple);
    }
    this->insertTuplesIntoRelation(dr->head, newTuples);
    newTuples->empty();
    delete newTuples;
}

void Database::insertTuplesIntoRelation(Relation *r, set<Tuple>* tuples){
    for (size_t i = 0; i < this->relations->size(); i++) {
        if (this->relations->at(i)->name == r->name) {// find the relation/schema these have to go to
            set<Tuple>::iterator it;
            for (it = tuples->begin(); it != tuples->end(); ++it) {
                Tuple t = *it;
                this->relations->at(i)->tuples->insert(t);
            }
            break;
        }
    }
}

set<pair<int, int> >* Database::findMatchingColumns(Relation *r1, Relation *r2){
    set<pair<int, int> >* matches = new set<pair<int, int> >();
    for (size_t i = 0; i < r1->variableNames->size(); i++) {
        string name1 = r1->variableNames->at(i);
        for (size_t j = 0; j < r2->variableNames->size(); j++) {
            if (name1 == r2->variableNames->at(j)) {
                matches->insert(pair<int, int>(i, j));
            }
        }
    }
    return matches;
}

Relation* Database::keepGoodTuples(Relation *r1, Relation *r2, set<pair<int, int> >* matches){
    Relation *r = new Relation();
    delete r->variableNames;
    r->variableNames = new vector<string>(*r1->variableNames);// make sure this isnt broken - variable names !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    this->doVariableNames = true;
    
    set<Tuple>::iterator it2;
    for (it2 = r1->tuples->begin(); it2 != r1->tuples->end(); ++it2) {
        Tuple t1 = *it2;
        
        set<Tuple>::iterator it3;
        for (it3 = r2->tuples->begin(); it3 != r2->tuples->end(); ++it3) {
            
            Tuple t2 = *it3;
            
            this->keepGoodTuples2(matches, r, t2, t1, r2);
            
        }
    }
    delete r1;
    return r;
}

void Database::keepGoodTuples2(set<pair<int, int> >* matches, Relation *r, Tuple t2, Tuple t1, Relation *r2){
	set<int> ints = set<int>();
    bool keepTuple = true;
    set<pair<int, int> >::iterator it1;
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
            if (ints.find((int)i) == ints.end()) {// == here means not found
                t.push_back(t2.at(i));
                if (this->doVariableNames) {
                    r->variableNames->push_back(r2->variableNames->at(i));
                }
            }
        }
        r->tuples->insert(t);
 		this->doVariableNames = false;
    }
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
        d->ruleNum = i + 1;
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
    
    for (size_t i = 0; i < this->nodes.size(); i++) {
        delete this->nodes.at(i);
    }
    
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
    }
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
            oss << "Yes(" << query->tuples->size() << ")" << "\n" << query->toStringRemainingTuples();
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
        myfile << this->lab5Output();
        myfile.close();
    }catch(exception& e){
        e.what();
    }
}

void Database::writeToFileS(string filename){
    try{
        ofstream myfile;
        myfile.open(filename);
        myfile << this->lab5Output();
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



string Database::dependencyGraphToString(){
    vector<string> v;
    ostringstream os;
    os << "Dependency Graph\n";
//    cout << this->nodeMap.size();
//    for(map<string, Node*>::iterator it = this->nodeMap.begin(); it != this->nodeMap.end(); ++it) {
//        v.push_back(it->first);
//        cout << it->first << "\n";
//    }
    sort(this->nodes.begin(), this->nodes.end(), sortNodes);
    for (size_t j = 0; j < this->nodes.size(); j++) {
        Node *n = this->nodes.at(j);
        string s = n->identifier;
        os << "  " << s << ":";
        Node *node = this->nodeMap.at(s);
//        sort(node->dependencies.begin(), node->dependencies.end());
        
//        for (size_t i = 0; i < node->dependencies.size(); i++) {
        int count = 0;
        set<string>::iterator it2;
        for (it2 = node->dependencies.begin(); it2 != node->dependencies.end(); ++it2) {
            count++;
            string dependency = *it2;
            if (count == 1) {
                os << " ";
            }
                os << dependency;
            if (count != node->dependencies.size()) {
                os << " ";
            }
        }
        os << "\n";
    }
    os << "\n";
    return os.str();
}

bool sortNodesNormal(Node* n1, Node* n2){
    int a = stringToInt(n1->identifier.substr(1));
    int b = stringToInt(n2->identifier.substr(1));
    //    cout << "rules: " << p1.first << " " << p2.first << endl;
    return (a < b);
}

string Database::lab5Output(){
    ostringstream os;
    os << this->dependencyGraphToString();
    sort(this->nodes.begin(), this->nodes.end(), sortNodesNormal);
    for (size_t i = 0; i < this->nodes.size(); i++) {
        Node *node = this->nodes.at(i);
        if (node->isQuery) {
            Relation *query = node->query;
            os << query->name << "(" << query->toStringOriginalQueryColumns() << ")?\n\n";
            os << this->postOrderNumbersToString(node);
            os << this->ruleEvaluationOrderToString(node);
            os << this->backwardsEdgesToString(node);
            os << this->ruleEvaluationToString(node);
            os << query->name << "(" << query->toStringOriginalQueryColumns() << ")? ";
            if (query->tuples->size() > 0) {
                os << "Yes(" << query->tuples->size() << ")" << "\n" << query->toStringRemainingTuples();
            }else
                os << "NO\n";
            os << "\n";
        }
    }
    return os.str();
}

bool sortPostOrderPairs(pair<string, int> p1, pair<string, int> p2){
    int a = stringToInt(p1.first.substr(1));
    int b = stringToInt(p2.first.substr(1));
//    cout << "rules: " << p1.first << " " << p2.first << endl;
    return (a > b);
}

bool sortThesePostOrderPairsQueries(pair<string, int> p1, pair<string, int> p2){
//    cout << "queries: " << p1.first << " " << p2.first << endl;
    return (p1.first > p2.first);
}

string Database::postOrderNumbersToString(Node *node){
    ostringstream os;
    os << "  Postorder Numbers\n";
    // sort thing
//    sort(node->postOrderPairs.begin(), node->postOrderPairs.end(), sortThesePostOrderPairsQueries);
    sort(node->postOrderPairsRules.begin(), node->postOrderPairsRules.end(), sortThesePostOrderPairsQueries);
    
    for (int i = (int)node->postOrderPairs.size() - 1; i >= 0; i--) {// do queries first
        pair<string, int> pair = node->postOrderPairs.at(i);
        os << "    " << pair.first << ": " << pair.second << "\n";
//        cout << pair.first << endl;
    }
    for (int i = (int)node->postOrderPairsRules.size() - 1; i >= 0; i--) {// do rules second
        pair<string, int> pair = node->postOrderPairsRules.at(i);
        os << "    " << pair.first << ": " << pair.second << "\n";
    }
    os << "\n";
    return os.str();
}



string Database::ruleEvaluationOrderToString(Node *node){
    ostringstream os;
    os << "  Rule Evaluation Order\n";
    for (int i = 0; i < (int)node->ruleEvaluationOrder.size(); i++) {
        os << "    " << node->ruleEvaluationOrder.at(i) << "\n";
    }
    os << "\n";
    return os.str();
}

string Database::backwardsEdgesToString(Node *node){
    ostringstream os;
//    os << "  Backward Edges\n";
//    typedef std::map<string, string>::iterator it_type;
//    for(it_type it = node->backwardsEdges.begin(); it != node->backwardsEdges.end(); it++) {
//        os << "    " << it->first << ": " << it->second << "\n";
//    }
//    os << "\n";

    os << "  Backward Edges\n";
    vector<Node*> keys = vector<Node*>();
    typedef std::map<Node*, vector<string> >::iterator it_type;
    for(it_type it = node->childNodesWithBackwardsEdges.begin(); it != node->childNodesWithBackwardsEdges.end(); it++) {
        Node* node = it->first;
        keys.push_back(node);
    }
    int asdf = (int)keys.size();
    int lkj = (int)node->childNodesWithBackwardsEdges.size();
    sort(keys.begin(), keys.end(), sortNodes);
    for (size_t i = 0; i < keys.size(); i++) {
        Node* node2 = keys.at(i);
//        map<Node*, vector<string> >::iterator = node->childNodesWithBackwardsEdges.find(node);
        vector<string> backwardsEdges = node->childNodesWithBackwardsEdges.find(node2)->second;
        os << "    " << node2->identifier << ":";
        sort(backwardsEdges.begin(), backwardsEdges.end());
        for (size_t i = 0; i < backwardsEdges.size(); i++) {
            os << " " << backwardsEdges.at(i);
        }
        os << "\n";
    }
    
    os << "\n";
    
    return os.str();
}

string Database::ruleEvaluationToString(Node *node){
    ostringstream os;
    os << "  Rule Evaluation\n";
    for (size_t i = 0; i < node->rulesEvaluated.size(); i++) {
        os << "    " << node->rulesEvaluated.at(i) << "\n";
    }
    os << "\n";
    return os.str();
}





























































