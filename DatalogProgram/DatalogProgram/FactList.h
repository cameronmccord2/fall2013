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

class Predicate;
class DatalogProgram;

class FactList{
private:
    std::vector<Predicate*>* list;
public:
    FactList();
    ~FactList();
    int getCount();
    std::string toString();
    void parseFactList(DatalogProgram* dp);
};

#endif /* defined(__DatalogProgram__FactList__) */
