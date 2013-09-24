//
//  RuleList.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__RuleList__
#define __DatalogProgram__RuleList__

#include <iostream>
#include <vector>
#include "Predicate.h"

class RuleList{
private:
    std::vector<Predicate*>* list;
public:
    RuleList();
    ~RuleList();
    void add(Predicate* predicate);
    std::string toString();
};

#endif /* defined(__DatalogProgram__RuleList__) */
