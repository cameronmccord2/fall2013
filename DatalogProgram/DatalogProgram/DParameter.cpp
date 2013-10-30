//
//  DParameter.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 10/30/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DParameter.h"

DParameter::DParameter(Parameter* p){
    this->value = p->value;
    this->valueIsString = p->valueIsString;
}
DParameter::~DParameter(){
    
}

std::string DParameter::toString(){
    std::string finalString = this->value;
    if (this->valueIsString) {
        finalString = "'";
        finalString += this->value;
        finalString += "'";
    }
    return finalString;
}

bool DParameter::valueIsVariable(){
    return !this->valueIsString;
}