//
//  SchemeList.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__SchemeList__
#define __DatalogProgram__SchemeList__

#include <iostream>
#include <vector>

class Predicate;
class DatalogProgram;

class SchemeList{
private:
    
public:
    std::vector<Predicate*>* list;
    Predicate* getPredicateAtIndex(int index);
    SchemeList();
    ~SchemeList();
    int getCount();
    std::string toString();
    void parseSchemeList(DatalogProgram* dp);
};

#endif /* defined(__DatalogProgram__SchemeList__) */
