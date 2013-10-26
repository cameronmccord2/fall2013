//
//  Relation.h
//  DatalogProgram
//
//  Created by Cameron McCord on 10/25/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Relation__
#define __DatalogProgram__Relation__

using namespace std;

#include <iostream>
#include <set>
#include "Tuple.h"
#include "Schema.h"
#include "Parameter.h"

class Relation{
public:
    string name;
    vector<string>* variableNames;
    vector<Parameter*>* queryParams;
    set<Tuple*>* tuples;
    Relation *result;
    vector<string>* keys;
    Relation();
    ~Relation();
    Relation* selectConstant(int position, string value);
    Relation* selectVariable(int position1, int position2);
    Relation* project(int position, string toValue);
    Relation* rename(int position, string toValue);
    vector<Relation*>* relationsToDelete;
private:
    
};
#endif /* defined(__DatalogProgram__Relation__) */
