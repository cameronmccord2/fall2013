//
//  FactList.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__FactList__
#define __DatalogProgram__FactList__

#include <iostream>
#include <vector>
#include "Predicate.h"

class FactList{
private:
    std::vector<Predicate*>* list;
public:
    FactList();
    ~FactList();
    void add(Predicate* predicate);
    std::string toString();
};

#endif /* defined(__DatalogProgram__FactList__) */
