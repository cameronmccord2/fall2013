//
//  Parameter.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Parameter__
#define __DatalogProgram__Parameter__

#include <iostream>

class DatalogProgram;
class Parameter{
private:
    
public:
    Parameter(DatalogProgram *dp);
    ~Parameter();
    std::string toString();
private:
    void readParameter(DatalogProgram *dp);
    std::string value;
    bool valueIsString;
};

#endif /* defined(__DatalogProgram__Parameter__) */
