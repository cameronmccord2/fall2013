//
//  DParameter.h
//  DatalogProgram
//
//  Created by Taylor McCord on 10/30/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__DParameter__
#define __DatalogProgram__DParameter__

#include <iostream>
#include "Parameter.h"

class DParameter {
public:
    DParameter(Parameter* p);
    ~DParameter();
    std::string toString();
    std::string value;
    bool valueIsString;
    bool valueIsVariable();
};
#endif /* defined(__DatalogProgram__DParameter__) */
