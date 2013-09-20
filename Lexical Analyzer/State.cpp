#include "State.h"

using namespace std;

string StateToString(State tokenType){
    string result = "";
    switch(tokenType){
        case Comma:                      result = "Comma"; break;
        case Period:                     result = "Period"; break;
        case SawColon:                   result = "SawColon"; break;
        case Colon_Dash:                 result = "Colon_Dash"; break;
        case SawAQuote:                  result = "SawAQuote"; break;
        case ProcessingString:           result = "ProcessingString"; break;
        case PossibleEndOfString:        result = "PossibleEndOfString"; break;
        case Start:                      result = "Start"; break;
        case End:                        result = "End"; break;
        case Q_Mark:					result = "Q_Mark"; break;
        case Left_Paren:				result = "Left_Paren"; break;
        case Right_Paren:				result = "Right_Paren"; break;
        case Multiply:					result = "Multiply"; break;
        case Add:						result = "Add"; break;
        case Undefined:					result = "Undefined"; break;
        case PossibleEndOfComment:		result = "PossibleEndOfComment"; break;
        case ProcessingComment:			result = "ProcessingComment"; break;
        case ProcessingBlockComment:	result = "ProcessingBlockComment"; break;
        case PossibleEndOfBlockComment:	result = "PossibleEndOfBlockComment"; break;
        case SawPound:					result = "SawPound"; break;
        case ProcessingId:				result = "ProcessingId"; break;
        case PossibleEndOfId:			result = "PossibleEndOfId"; break;
        case SchemesC:					result = "SchemesC"; break;
        case SchemesH:					result = "SchemesH"; break;
        case SchemesE1:					result = "SchemesE1"; break;
        case SchemesM:					result = "SchemesM"; break;
        case SchemesE2:					result = "SchemesE2"; break;
        case SchemesS2:					result = "SchemesS2"; break;
        case FactsA:					result = "FactsA"; break;
        case FactsC:					result = "FactsC"; break;
        case FactsT:					result = "FactsT"; break;
        case FactsS:					result = "FactsS"; break;
        case QueriesU:					result = "QueriesU"; break;
        case QueriesE1:					result = "QueriesE1"; break;
        case QueriesR:					result = "QueriesR"; break;
        case QueriesI:					result = "QueriesI"; break;
        case QueriesE2:					result = "QueriesE2"; break;
        case QueriesS:					result = "QueriesS"; break;
        case RulesU:					result = "RulesU"; break;
        case RulesL:					result = "RulesL"; break;
        case RulesE:					result = "RulesE"; break;
        case RulesS:					result = "RulesS"; break;
        case PossibleEndOfFacts:		result = "PossibleEndOfFacts"; break;
        case PossibleEndOfQueries:		result = "PossibleEndOfQueries"; break;
        case PossibleEndOfRules:		result = "PossibleEndOfRules"; break;
        case PossibleEndOfSchemes:		result = "PossibleEndOfSchemes"; break;
        case Whitespace:				 result = "Whitespace"; break;
    }
    return result;//m
};
