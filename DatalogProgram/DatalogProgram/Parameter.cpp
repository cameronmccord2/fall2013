//
//  Parameter.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "Parameter.h"
#include "DatalogProgram.h"

Parameter::Parameter(DatalogProgram *dp){
    this->value = "";
    this->valueIsString = false;
    this->readParameter(dp);
}

Parameter::~Parameter(){
    
}

void Parameter::readParameter(DatalogProgram *dp){
    if(dp->getCurrentToken()->getTokenType() == STRING || dp->getCurrentToken()->getTokenType() == ID){
        if(dp->getCurrentToken()->getTokenType() == STRING){
            this->valueIsString = true;
            dp->setDomainElement(dp->getCurrentToken()->getTokensValue());
        }
        this->value = dp->getCurrentToken()->getTokensValue();
        return;
    }else
        dp->setError(dp->getCurrentToken());
}

std::string Parameter::toString(){
    std::string finalString = this->value;
    if (this->valueIsString) {
        finalString = "'";
        finalString += this->value;
        finalString += "'";
    }
    return finalString;
}

bool Parameter::valueIsVariable(){
    return !this->valueIsString;
}