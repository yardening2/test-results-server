package com.yarden.restServiceDemo;

import com.google.gson.JsonArray;

public class SheetData {

    private static JsonArray sheetData = null;

    public synchronized static JsonArray getSheetData(String googleSheetTabName){
        if (sheetData == null) {
            try {
                try {
                    sheetData = SheetDBApiService.getService().getAllSheet(googleSheetTabName).execute().body();
                } catch (Throwable t1) {
                    sheetData = SheetDBApiService.getService().getAllSheet(googleSheetTabName).execute().body();
                }
            } catch (Throwable t) {
                System.out.println("ERROR: failed getting sheet:" + t.getMessage());
            }
        }
        return sheetData;
    }

    public synchronized static JsonArray getHighLevelSheet(){
        try {
            try {
                return SheetDBApiService.getService().getAllSheet(SdkReportService.SheetTabsNames.HighLevel.value).execute().body();
            } catch (Throwable t1) {
                return SheetDBApiService.getService().getAllSheet(SdkReportService.SheetTabsNames.HighLevel.value).execute().body();
            }
        } catch (Throwable t) {
            System.out.println("ERROR: failed getting sheet:" + t.getMessage());
        }
        return new JsonArray();
    }

    public synchronized static void clearCachedSheetData(){
        sheetData = null;
    }

}
