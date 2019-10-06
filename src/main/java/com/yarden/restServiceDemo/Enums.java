package com.yarden.restServiceDemo;

public class Enums {

    public enum TestResults{
        Passed("1"), Failed("-1");

        String value;

        TestResults(String value){
            this.value = value;
        }
    }

    public enum SheetTabsNames {
        Report("Coverage comparing"), HighLevel("Results history"), Sandbox("sandbox");

        String value;

        SheetTabsNames(String value){
            this.value = value;
        }
    }

    public enum SheetColumnNames {
        TestName("Test name"), IDRow("id"), Fail("_fail"), Pass("_pass");

        String value;

        SheetColumnNames(String value){
            this.value = value;
        }
    }

    public enum HighLevelSheetColumnNames {
        Sdk("sdk"), StartTimestamp("start_timestamp"), LastUpdate("last_update"), ID("id"), SuccessPercentage("success percentage"), AmountOfTests("amount of tests");

        String value;

        HighLevelSheetColumnNames(String value){
            this.value = value;
        }
    }

}
