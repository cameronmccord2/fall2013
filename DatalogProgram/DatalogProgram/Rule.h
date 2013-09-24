//
//  Rule.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/24/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Rule__
#define __DatalogProgram__Rule__

#include <iostream>
#include <vector>

class Predicate;

class Rule{
public:
    Rule();
    ~Rule();
    Predicate* firstPredicate;
    std::vector<Predicate*>* list;
    std::string toString();
};
#endif /* defined(__DatalogProgram__Rule__) */
