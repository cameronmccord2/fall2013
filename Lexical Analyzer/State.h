#ifndef STATE_H
#define STATE_H

#include <string>

/**
 * The states of the finite state machine defined in an enumerated type.
 */
enum State {Comma, Period, Q_Mark, Left_Paren, Right_Paren, SawColon, Colon_Dash, Multiply, Add, SawAQuote, 
			ProcessingString, PossibleEndOfString, Start, End, Undefined,
			PossibleEndOfComment, ProcessingComment, ProcessingBlockComment,
			PossibleEndOfBlockComment, SawPound, ProcessingId, PossibleEndOfId, SchemesC, SchemesH,
			SchemesE1, SchemesM, SchemesE2, SchemesS2, FactsA, FactsC, FactsT, FactsS, RulesU,
			RulesL, RulesE, RulesS, QueriesU, QueriesE1, QueriesR, QueriesI, QueriesE2, QueriesS,
			PossibleEndOfFacts, PossibleEndOfSchemes, PossibleEndOfRules, PossibleEndOfQueries, Whitespace
           };

    /**
     * Converts a state to a string.
     * s
     * Parameters: state -- the state to be converted to a string.
     * PreCondition: none
     * Postcondition: result = the string representation of the state which
     *                         looks exactly like its State name.
     */
    std::string StateToString(State state);
#endif
