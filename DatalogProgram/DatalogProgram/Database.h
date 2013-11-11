//
//  Database.h
//  DatalogProgram
//
//  Created by Cameron McCord on 10/25/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Database__
#define __DatalogProgram__Database__

#include <iostream>
#include "DatalogProgram.h"
#include "Relation.h"
#include "DataRule.h"
#include <map>
#include <utility>

class Database {
public:
    Database(DatalogProgram *dp);
    ~Database();
    int passesThroughRules;
    bool run(vector<Relation*>* list, bool doProject);
    void writeToFile(char* filename);
    void runRules();
    void generateRules(DatalogProgram *dp);
    string toString();
    vector<Relation*>* relations;
    vector<Relation*>* queries;
    vector<DataRule*>* masterRules;
    vector<DataRule*>* rules;
    vector<Relation*>* results;
    void addVariableToSet(int position, string value);
    map<string, vector<int>*>* variablePositions;
    void addVariableToDoneKeys(int position, string value);
    void copyRules();
    void deleteRulesCopy();
    void joinRule(DataRule *dr);
    set<pair<int, int> >* findMatchingColumns(Relation *r1, Relation *r2);
    void simplifyToHeadAndAddToFacts(DataRule *dr, Relation *r);
    Relation* keepGoodTuples(Relation *r1, Relation *r2, set<pair<int, int> >* matches);
    string tupleToString(Tuple t);
    void insertTuplesIntoRelation(Relation *r, set<Tuple>* tuples);
    unsigned long getFactsCount();
    void printTuplesForRelation(vector<Relation*>* list, string schemaName);
    void runRulesLoop();
	bool keepGoodTuples2(set<pair<int, int> >* matches, Relation *r, bool doVariableNames, Tuple t2, Tuple t1, Relation *r2);
private:
    
};
#endif /* defined(__DatalogProgram__Database__) */
