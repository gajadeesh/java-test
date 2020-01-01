package com.swivel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;

public class tickets {
    //Variables
    private String  tdata;
    private Scanner fieldName;
    private JSONArray users, tickets, organizations ;
    private JSONParser UjsonParser, TjsonParser, OjsonParser;
    private CommandLineTable st, lt, at;

    public void ticketField(String tfield) {
        //initialize jsonparser
        UjsonParser = new JSONParser();
        TjsonParser = new JSONParser();
        OjsonParser = new JSONParser();
        fieldName = new Scanner(System.in);

        try {
            //Read JSON file
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
            st = new CommandLineTable();

            //Layout Hearders
            st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
            st.setHeaders(
                    "id",
                    "subject",
                    "description",
                    "url",
                    "priority",
                    "status",
                    "external_id",
                    "created_at",
                    "submitter_id",
                    "assignee_id",
                    "organization_id",
                    "tags",
                    "has_incidents",
                    "due_at",
                    "via"
            );//optional - if not used then there will be no header and horizontal lines

            //Search Filters
            if (tfield.equals("all")) {
                tickets.forEach(emp -> printall((JSONObject) emp, (CommandLineTable) st));
                st.print();
            }else{
                System.out.println("Search in Field " + tfield);
                tdata = fieldName.nextLine();
                tickets.forEach(emp ->{
                    if (emp instanceof JSONObject) {
                        JSONObject mp = (JSONObject) emp;
                        if (tdata.equals("empty")){
                            if (mp.get(tfield) == null) {
//                                System.out.println(mp.get("_id"));
                                printall((JSONObject) mp, (CommandLineTable) st);
                                st.print();
                            }
                        }else if(mp.get(tfield).toString().equals(tdata)){
                            printall((JSONObject) mp, (CommandLineTable) st);
                            st.print();
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

    private void printall(JSONObject tic, CommandLineTable st) {
        StringBuilder obuilder = new StringBuilder();
        StringBuilder abuilder = new StringBuilder();
        StringBuilder sbuilder = new StringBuilder();

        organizations.forEach(em ->{
            if (em instanceof JSONObject) {
                JSONObject mp = (JSONObject) em;
                if (tic.get("organization_id") == mp.get("_id")){
                    System.out.println(mp.get("name"));
                    obuilder.append(mp.get("name"));
                }
            }
        });
        users.forEach(em ->{
            if (em instanceof JSONObject) {
                JSONObject mp = (JSONObject) em;
                if (tic.get("assignee_id") == mp.get("_id")){
//                    System.out.println(mp.get("name"));
                    abuilder.append(mp.get("name"));
                }
                if (tic.get("submitter_id") == mp.get("_id")){
//                    System.out.println(mp.get("name"));
                    sbuilder.append(mp.get("name"));
                }
            }
        });
        try {

            st.addRow(
                    tic.get("_id").toString(),
                    tic.get("subject").toString(),
                    tic.get("description") == null ? ("") : (tic.get("description").toString()),
                    tic.get("url").toString(),
                    tic.get("priority").toString(),
                    tic.get("status").toString(),
                    tic.get("external_id").toString(),
                    tic.get("created_at").toString(),
                    sbuilder == null ? ("") : (sbuilder.toString()),
                    abuilder == null ? ("") : (abuilder.toString()),
                    obuilder  == null ? ("") : (obuilder.toString()),
                    tic.get("tags").toString(),
                    tic.get("has_incidents").toString(),
                    tic.get("due_at") == null ? ("") : (tic.get("due_at").toString()),
                    tic.get("via").toString()
            );

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
