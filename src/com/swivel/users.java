package com.swivel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.json.simple.*;
import org.json.simple.parser.*;



public class users {
    //variables
    private  String  Tfield;
    private Scanner fieldName;
    private JSONArray users, tickets, organizations ;
    private JSONParser UjsonParser, TjsonParser, OjsonParser;
    private CommandLineTable st, lt, at;


    public void userField(String ufields) {
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
            lt = new CommandLineTable();
            st = new CommandLineTable();
            at = new CommandLineTable();

            //Layout Hearders
            lt.setShowVerticalLines(true);
            lt.setHeaders("organization name","submitter ticket subject");
            at.setShowVerticalLines(true);
            at.setHeaders("organization name","assignee ticket subject");
            st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
            st.setHeaders(
                    "id",
                    "name",
                    "url",
                    "external_id",
                    "alias",
                    "created_at",
                    "active",
                    "verified",
                    "shared",
                    "locale",
                    "timezone",
                    "last_login_at",
                    "email",
                    "phone",
                    "signature",
                    "tags",
                    "suspended",
                    "role"
            );//optional - if not used then there will be no header and horizontal lines

            //Search Filters
            if (ufields.equals("all")) {
                users.forEach(emp -> printall((JSONObject) emp, (CommandLineTable) st));

                st.print();
            }else{
                    System.out.println("Search in Field " + ufields);
                    Tfield = fieldName.nextLine();
                    users.forEach(emp -> {
                        if (emp instanceof JSONObject) {

                            JSONObject up = (JSONObject) emp;

                            if (Tfield.equals("empty")) {
                                if (up.get(ufields) == null) {
                                    printall((JSONObject) up, (CommandLineTable) st);
                                    tickets.forEach(ep -> ticketPickup((JSONObject) ep, up.get("_id"), (CommandLineTable) lt, (CommandLineTable) at));
                                    st.print();
                                }

                            } else if (up.get(ufields).toString().equals(Tfield)) {
                                printall((JSONObject) up, (CommandLineTable) st);
                                st.print();
                                tickets.forEach(ep -> ticketPickup((JSONObject) ep, up.get("_id"), (CommandLineTable) lt, (CommandLineTable) at));
                                lt.print();
                                at.print();

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
    private void printall(JSONObject users, CommandLineTable st){
            try
            {
                st.addRow(
                            users.get("_id").toString(),
                            users.get("name").toString(),
                            users.get("url").toString(),
                            users.get("external_id").toString(),
                            users.get("alias") == null ? ("") : (users.get("alias").toString()),
                            users.get("created_at").toString(),
                            users.get("active").toString(),
                            users.get("verified") == null ? ("") : (users.get("verified").toString()),
                            users.get("shared").toString(),
                            users.get("locale") == null ? ("") : (users.get("locale").toString()),
                            users.get("timezone") == null ? ("") : (users.get("timezone").toString()),
                            users.get("last_login_at") == null ? ("") : (users.get("last_login_at").toString()),
                            users.get("email") == null ? ("") : (users.get("email").toString()),
                            users.get("phone").toString(),
                            users.get("signature").toString(),
                            users.get("tags").toString(),
                            users.get("suspended").toString(),
                            users.get("role").toString()
                    );
            }catch(Exception e) {
//                e.printStackTrace();
                System.out.println(e);
            }
        }
        public void ticketPickup(JSONObject tickets, Object id, CommandLineTable lt, CommandLineTable at) {
        StringBuilder sbuilder = new StringBuilder();
        StringBuilder abuilder = new StringBuilder();
        if (tickets.get("submitter_id") == id) {
            organizations.forEach(dm-> {
                if (dm instanceof JSONObject) {
                    JSONObject crt = (JSONObject) dm;
                    if ((long)crt.get("_id") == (long) tickets.get("organization_id")){
                        sbuilder.append(crt.get("name"));
                    }
                }
            });
            lt.addRow(
                    sbuilder.toString(),
                    String.valueOf(tickets.get("subject"))
            );
        }
        if (tickets.get("assignee_id") ==  id){
            organizations.forEach(dm-> {
                if (dm instanceof JSONObject) {
                    JSONObject crt = (JSONObject) dm;
                    if ((long)crt.get("_id") == (long) tickets.get("organization_id")){
                        abuilder.append(crt.get("name"));
                    }

                }
            });
            at.addRow(
                    abuilder.toString(),
                    String.valueOf(tickets.get("subject"))
            );

        }
    }
}
