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
	Parameter(Parameter* p);
    ~Parameter();
    std::string toString();
    std::string value;
    bool valueIsString;
    bool valueIsVariable();
private:
    void readParameter(DatalogProgram *dp);
    
};

#endif /* defined(__DatalogProgram__Parameter__) */
