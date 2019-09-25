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
        TestData("test_data"), HighLevel("high_level"), Sandbox("sandbox");

        String value;

        SheetTabsNames(String value){
            this.value = value;
        }
    }

    public enum SheetColumnNames {
        TestName("test_name"), IDRow("id");

        String value;

        SheetColumnNames(String value){
            this.value = value;
        }
    }

    public enum HighLevelSheetColumnNames {
        Sdk("sdk"), LastRun("last_run"), ID("id"), SuccessPercentage("success percentage");

        String value;

        HighLevelSheetColumnNames(String value){
            this.value = value;
        }
    }

}
