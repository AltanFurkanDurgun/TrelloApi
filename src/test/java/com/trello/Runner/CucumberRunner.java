package com.trello.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/cucumber.json",
                "html:target/default-html-reports"},
        features = "src/test/resources/Features/",
        glue = "com/trello/StepDefinitions",
        dryRun = false,
        tags = "@test"
)
public class CucumberRunner {


}
