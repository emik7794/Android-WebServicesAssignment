package ar.edu.unc.famaf.redditreader.backend;

import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public class Listing{

        String before;
        String after;
        List<Message> children = new ArrayList<>();
    }

    public class Message {

        String thumbnail = null;
        String title = null;
        String author = null;
        long num_comments = -1;

        Message(String thumbnail, String title, String author, long num_comments){
            this.thumbnail = thumbnail;
            this.title = title;
            this.author = author;
            this.num_comments = num_comments;
        }
    }

    public Listing readJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readFirstDataJson(reader);
        } finally {
            reader.close();
        }
    }


    public Listing readFirstDataJson(JsonReader reader) throws IOException {

        Listing listing = new Listing();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("children")) {
                listing.children = readChildrenArray(reader);
            }
        }
        reader.endObject();

        return listing;
    }

    public List<Message> readChildrenArray (JsonReader reader)throws IOException {

        List<Message> children = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            children.add(readMessage(reader));
        }
        reader.endArray();

        return children;
    }


    public Message readMessage(JsonReader reader) throws IOException {

        String thumbnail = null;
        String title = null;
        String author = null;
        long num_comments = -1;
        ////////////////////// VER COMO OBTENER EL DATE QUE NO APARECE EN EL JSON


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("thumbnail")) {
                thumbnail = reader.nextString();
            } else if (name.equals("text")) {
                title = reader.nextString();
            } else if (name.equals("author")) {
                author = reader.nextString();
            } else if (name.equals("num_comments")) {
                num_comments = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        Message message = new Message(thumbnail, title, author, num_comments);

        return message;
    }

}
