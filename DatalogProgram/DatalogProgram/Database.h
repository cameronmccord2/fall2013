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
#include <map>
#include <utility>

class Database {
public:
    Database(DatalogProgram *dp);
    ~Database();
    bool run();
    void writeToFile(char* filename);
    string toString();
    vector<Relation*>* relations;
    vector<Relation*>* queries;
    vector<Relation*>* results;
    void addVariableToSet(int position, string value);
    map<string, vector<int>*>* variablePositions;
    void addVariableToDoneKeys(int position, string value);
private:
    
};
#endif /* defined(__DatalogProgram__Database__) */
