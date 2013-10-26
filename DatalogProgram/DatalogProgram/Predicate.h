//
//  Predicate.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Predicate__
#define __DatalogProgram__Predicate__

#include <iostream>
#include "DatalogProgram.h"
#include "Parameter.h"

class Predicate{
private:
    
public:
    Predicate(DatalogProgram *dp);
    ~Predicate();
    string toString();
    string identifier;
    bool valueIsIdentifier;
    vector<Parameter*>* parameters;
    std::string getIdentifier();
private:
    
    void readParameterList(DatalogProgram* dp);
    void readParameter(DatalogProgram* dp);
    void parsePredicate(DatalogProgram* dp);
};
#endif /* defined(__DatalogProgram__Predicate__) */
