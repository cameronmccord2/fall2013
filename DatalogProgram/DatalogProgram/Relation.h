//
//  Relation.h
//  DatalogProgram
//
//  Created by Cameron McCord on 10/25/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Relation__
#define __DatalogProgram__Relation__

#include <iostream>
#include <set>
#include "Tuple.h"
#include "Schema.h"
#include "DParameter.h"
#include <map>

class Database;

class Relation{
public:
    string name;
    vector<string>* variableNames;
    vector<DParameter>* queryParams;
    vector<DParameter>* originalQueryParams;
    set<Tuple>* tuples;
    vector<string>* keys;
    Relation();
    Relation(Relation *old);
    ~Relation();
    void generateKeysAndVariablePositions( vector<string>* keys, map<string, vector<int>*>* variablePositions, Database *db);
    Relation* project1(vector<int>* indexes);
    Relation* project2(vector<int>* indexes);
    Relation* reduceSideways(Relation *query, Database *db);
    Relation* reduceSideways2(Relation *query, Database *db);
    Relation* reduceVertical(Relation* query, Database *db);
    Relation* selectConstant(int position, string value);
    Relation* selectVariable(int position1, int position2);
    Relation* project();
    Relation* rename(int position, string toValue);
    vector<Relation*>* relationsToDelete;
    string toStringRemainingTuples();
    string toStringOriginalQueryColumns();
    Relation* deleteTupleValueAtPosition(int pos);
private:
    
};
#endif /* defined(__DatalogProgram__Relation__) */
