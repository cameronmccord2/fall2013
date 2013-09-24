//
//  DomainList.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/24/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DomainList.h"

DomainList::DomainList(){
    this->elements = new std::vector<std::string>();
}

DomainList::~DomainList(){
    delete this->elements;
}

void DomainList::setDomainElement(std::string element){
    for (size_t i = 0; i < this->elements->size(); i++) {
        if (elements->at(i) == element) {
            return;
        }
    }
    this->elements->push_back(element);
}

int DomainList::getCount(){
    return (int)this->elements->size();
}

std::string DomainList::toString(){
    std::string finalString = "";
    for (size_t i = 0; i < this->elements->size(); i++) {
        finalString += "\t'";
        finalString += this->elements->at(i);
        finalString += "'";
        if (i != this->elements->size() - 1) {
            finalString += "\n";
        }
    }
    return finalString;
}