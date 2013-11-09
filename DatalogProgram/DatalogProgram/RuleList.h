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
#include "Rule.h"

class DatalogProgram;
class Predicate;
class RuleList{
private:
    
public:
    RuleList();
    ~RuleList();
    int getCount();
    std::string toString();
    void parseRuleList(DatalogProgram* dp);
    void parseRuleList2(DatalogProgram *dp, Rule *rule);
    std::vector<Rule*>* list;
};

#endif /* defined(__DatalogProgram__RuleList__) */
