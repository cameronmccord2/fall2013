//
//  DataRule.h
//  DatalogProgram
//
//  Created by Cameron McCord on 11/8/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__DataRule__
#define __DatalogProgram__DataRule__

#include <iostream>
#include "Relation.h"

class DataRule {
public:
    Relation* head;
    vector<Relation*>* predicates;
    DataRule();
    ~DataRule();
    DataRule(DataRule *dr);
    int ruleNum;
    string toString();
private:
};

#endif /* defined(__DatalogProgram__DataRule__) */
