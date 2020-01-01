package com.swivel;

import org.codehaus.jackson.JsonNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;

public class organizations {
    //Variables
    private String tdata;
    private Scanner fieldName;
    private JSONArray users, tickets, organizations;
    private JSONParser UjsonParser, TjsonParser, OjsonParser;
    private CommandLineTable st, lt, at;

    public void orgField(String ofields) {
        //initialize jsonparser
        UjsonParser = new JSONParser();
        TjsonParser = new JSONParser();
        OjsonParser = new JSONParser();
        fieldName = new Scanner(System.in);

        try {
            //Read JSON Internal files
            InputStream uin = getClass().getResourceAsStream("users.json");
            BufferedReader ureader = new BufferedReader(new InputStreamReader(uin));
            InputStream tin = getClass().getResourceAsStream("tickets.json");
            BufferedReader treader = new BufferedReader(new InputStreamReader(tin));
            InputStream oin = getClass().getResourceAsStream("organizations.json");
            BufferedReader oreader = new BufferedReader(new InputStreamReader(oin));

            Object usr = UjsonParser.parse(ureader);
            Object tkt = TjsonParser.parse(treader);
            Object org = OjsonParser.parse(oreader);

            users = (JSONArray) usr;
            tickets = (JSONArray) tkt;
            organizations = (JSONArray) org;

            // Initialize print layout
            lt = new CommandLineTable();
            st = new CommandLineTable();

            //Layout Hearders
            lt.setShowVerticalLines(true);
            lt.setHeaders("organization name", "ticket subjects", "submitter user", "assignee user");
            st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
            st.setHeaders(
                    "id",
                    "name",
                    "details",
                    "url",
                    "domain_names",
                    "tags",
                    "shared_tickets",
                    "external_id",
                    "created_at"
            );//optional - if not used then there will be no header and horizontal lines

            //Search Filters
            if (ofields.equals("all")) {
                organizations.forEach(emp -> printall((JSONObject) emp, (CommandLineTable) st));
                st.print();
            } else {
                System.out.println("Search in Field " + ofields);
                tdata = fieldName.nextLine();
                organizations.forEach(emp -> {

                    if (emp instanceof JSONObject) {
                        JSONObject mp = (JSONObject) emp;
                        if (tdata.equals("empty")) {
                            if (mp.get(ofields) == null) {
                                printall((JSONObject) mp, (CommandLineTable) st);
                                st.print();
                            }
                        } else if (mp.get(ofields).toString().equals(tdata)) {
                            printall((JSONObject) mp, (CommandLineTable) st);
                            st.print();
                            tickets.forEach(pmp -> {
                                if (pmp instanceof JSONObject) {
                                    JSONObject rp = (JSONObject) pmp;
                                    if (mp.get("_id").equals(rp.get("organization_id"))) {
                                        ticketanduserPickup((JSONObject) rp, (JSONObject) mp);
                                        lt.print();
                                    }

                                }
                            });
                        }
                    }
                });
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void ticketanduserPickup(JSONObject rpp, JSONObject mpp) {
        StringBuilder abuilder = new StringBuilder();
        StringBuilder sbuilder = new StringBuilder();

        users.forEach(em ->{
            if (em instanceof JSONObject) {
                JSONObject mp = (JSONObject) em;
                if (rpp.get("assignee_id") == mp.get("_id")){
                    abuilder.append(mp.get("name"));
                }
                if (rpp.get("submitter_id") == mp.get("_id")){
                    sbuilder.append(mp.get("name"));
                }
            }
        });
        try {
            lt.addRow(
                    mpp.get("name").toString(),
                    rpp.get("subject").toString(),
                    sbuilder == null ? ("") : (sbuilder.toString()),
                    abuilder == null ? ("") : (abuilder.toString())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printall(JSONObject orgen, CommandLineTable st) {
        try {

            st.addRow(
                    orgen.get("_id").toString(),
                    orgen.get("name") == null ? ("") : (orgen.get("name").toString()),
                    orgen.get("details") == null ? ("") : (orgen.get("details").toString()),
                    orgen.get("url").toString(),
                    orgen.get("domain_names") == null ? ("") : (orgen.get("domain_names").toString()),
                    orgen.get("tags") == null ? ("") : (orgen.get("tags").toString()),
                    orgen.get("shared_tickets").toString(),
                    orgen.get("external_id").toString(),
                    orgen.get("created_at").toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
