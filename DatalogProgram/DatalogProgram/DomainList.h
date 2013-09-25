//
//  DomainList.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/24/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__DomainList__
#define __DatalogProgram__DomainList__

#include <iostream>
#include <set>

class DomainList{
public:
    DomainList();
    ~DomainList();
    int getCount();
    void setDomainElement(std::string element);
    std::string toString();
private:
    std::set<std::string>* elements;
};
#endif /* defined(__DatalogProgram__DomainList__) */
