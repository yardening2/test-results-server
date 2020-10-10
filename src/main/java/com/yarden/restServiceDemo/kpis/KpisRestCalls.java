package com.yarden.restServiceDemo.kpis;

import com.google.gson.Gson;
import com.yarden.restServiceDemo.Logger;
import com.yarden.restServiceDemo.RestCalls;
import com.yarden.restServiceDemo.reportService.SheetData;
import com.yarden.restServiceDemo.reportService.WriteEntireSheetsPeriodically;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class KpisRestCalls {

    @Test
    public void test() throws IOException {
        state_change("{\"team\":\"sdk\",\"state\":\"Doing\",\"sub_project\":\"javascript\",\"ticket_id\":\"nMNKaa4L1\",\"ticket_title\":\"NAB: Business mega menu page rendering incorrectly on mobile devices\",\"created_by\":\"Nikhil Nigam\",\"ticket_url\":\"https://trello.com/c/nMNKaa4L\"}");
        SheetData.writeAllTabsToSheet();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/state_update")
    public ResponseEntity state_change(@RequestBody String json) {
        synchronized (RestCalls.lock) {
            WriteEntireSheetsPeriodically.shouldStopSheetWritingTimer = false;
            WriteEntireSheetsPeriodically.start();
            newRequestPrint(json, "/state_change");
            TicketUpdateRequest ticketUpdateRequest = new Gson().fromJson(json, TicketUpdateRequest.class);
            new KpisMonitoringService(ticketUpdateRequest, TicketStates.valueOf(ticketUpdateRequest.getState())).updateStateChange();
            return new ResponseEntity(ticketUpdateRequest.toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update_ticket_type")
    public ResponseEntity update_ticket_type(@RequestBody String json) {
        synchronized (RestCalls.lock) {
            WriteEntireSheetsPeriodically.shouldStopSheetWritingTimer = false;
            WriteEntireSheetsPeriodically.start();
            newRequestPrint(json, "/update_ticket_type");
            TicketUpdateRequest ticketUpdateRequest = new Gson().fromJson(json, TicketUpdateRequest.class);
            new KpisMonitoringService(ticketUpdateRequest, TicketStates.NoState).updateTicketType();
            return new ResponseEntity(ticketUpdateRequest.toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get_all_tickets")
    public String get_all_tickets() {
        synchronized (RestCalls.lock) {
            newRequestPrint("", "/get_all_tickets");
            return KpisMonitoringService.getAllTickets().toString();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get_event_log")
    public String get_event_log() {
        synchronized (RestCalls.lock) {
            newRequestPrint("", "/get_event_log");
            return KpisMonitoringService.getEntireEventLog().toString();
        }
    }

    private void newRequestPrint(String json, String request){
        Logger.info("**********************************************************************************************");
        Logger.info("**********************************************************************************************");
        Logger.info("KPIs: New KPI request detected: " + request + " === payload: " + json);
    }
}
