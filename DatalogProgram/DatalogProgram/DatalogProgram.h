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
#include "Database.h"
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
    void writeToFile(char* filename);
    SchemeList* schemeList;
    FactList* factList;
    RuleList* ruleList;
    QueryList* queryList;
    DomainList* domainList;
private:
    bool successful;
    Token* offendingToken;
    void parseTokens();
    std::vector<Token*>* tokens;
    size_t currentToken;
    
};
#endif /* defined(__DatalogProgram__DatalogProgram__) */
