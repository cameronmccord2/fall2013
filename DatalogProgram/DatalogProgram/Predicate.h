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

class Predicate{
private:
    
public:
    Predicate();
    ~Predicate();
    void parsePredicate(DatalogProgram* dp);
};
#endif /* defined(__DatalogProgram__Predicate__) */
