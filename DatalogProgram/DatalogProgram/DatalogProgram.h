//
//  DatalogProgram.h
//  DatalogProgram
//
//  Created by Taylor McCord on 9/23/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__DatalogProgram__
#define __DatalogProgram__DatalogProgram__

#include <iostream>
#include "Lex.h"
#include "SchemeList.h"
#include "FactList.h"
#include "RuleList.h"
#include "QueryList.h"
#include "DomainList.h"

class DatalogProgram{
private:
    
public:
    DatalogProgram(std::vector<Token*>* tokens);
    ~DatalogProgram();
    string toString();
    Token* nextToken();
    Token* getCurrentToken();
    void setError(Token* token);
    bool isSuccessful();
    void setDomainElement(string element);
    
private:
    bool successful;
    Token* offendingToken;
    void parseTokens();
    std::vector<Token*>* tokens;
    size_t currentToken;
    SchemeList* schemeList;
    FactList* factList;
    RuleList* ruleList;
    QueryList* queryList;
    DomainList* domainList;
};
#endif /* defined(__DatalogProgram__DatalogProgram__) */
