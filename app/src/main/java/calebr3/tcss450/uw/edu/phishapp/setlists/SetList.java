package calebr3.tcss450.uw.edu.phishapp.setlists;


import java.io.Serializable;

public class SetList implements Serializable {
    private final String longDate;
    private final String venue;
    private final String location;
    private final String setListData;
    private final String setListNotes;
    private final String url;

    private SetList(Builder b){
        this.location = b.location;
        this.longDate = b.longDate;
        this.venue = b.venue;
        this.setListData = b.setListData;
        this.setListNotes = b.setListNotes;
        this.url = b.url;
    }


    public String getLocation() {
        return location;
    }

    public String getVenue() {
        return venue;
    }

    public String getLongDate() {
        return longDate;
    }

    public String getSetListData() {
        return setListData;
    }

    public String getSetListNotes() {
        return setListNotes;
    }

    public String getUrl() {
        return url;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Builder {
        private final String longDate;
        private final String venue;
        private final String location;
        private final String setListData;
        private final String setListNotes;
        private final String url;

        public Builder(String longDate, String venue, String location, String setListData
                , String setListNotes, String url) {
            this.longDate = longDate;
            this.venue = venue;
            this.location = location;
            this.setListData = setListData;
            this.setListNotes = setListNotes;
            this.url = url;
        }

        public SetList build(){
            return new SetList(this);
        }
    }
}
