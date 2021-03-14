package com.tiobe.Gherkin;

import com.tiobe.antlr.GherkinLexer;
import com.tiobe.antlr.GherkinParser;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;

public class Rule8 {
    static void check(GherkinParser.InstructionContext ctx) {
        if (!App.RULES.contains("8")) {
            return;
        }

        // Rule Gherkin-ScenariosShouldNotBeLong: Scenario Outlines may only contain 100 steps (excluding datatables)
        if (ctx.stepInstruction() != null && (ctx.stepInstruction().scenario() != null || ctx.stepInstruction().scenarioOutline() != null)) {
            int steps = 0;
            for (GherkinParser.StepContext step : ctx.step()) {
                GherkinParser.StepItemContext item = step.stepItem();
                if (item.datatable() == null) {
                    steps++;
                }
            }
            if (steps > 100) {
                new Violation(8, ctx, "This Scenario Outline contains " + steps + " steps");
            }
        }
    }
}
