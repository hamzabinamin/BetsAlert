package com.app.bet.Data_Models;

public class Sport {

    public int sportID;
    public String sportName;

    public Sport() {
        sportID = 0;
        sportName = "";
    }

    public Sport(int sportID, String sportName) {
        this.sportID = sportID;
        this.sportName = sportName;
    }

    @Override
    public String toString() {
        return sportName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Sport){
            Sport s = (Sport) obj;
            if(s.sportName.equals(sportName) && s.sportID == sportID ) {
                return true;
            }
        }

        return false;
    }
}
