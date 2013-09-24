//
//  QueryList.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__QueryList__
#define __DatalogProgram__QueryList__

#include <iostream>
#include <vector>
#include "Predicate.h"

class QueryList{
private:
    std::vector<Predicate*>* list;
public:
    QueryList();
    ~QueryList();
    void add(Predicate* predicate);
    std::string toString();
};

#endif /* defined(__DatalogProgram__QueryList__) */
