package ar.edu.unc.famaf.redditreader.backend;

import android.util.MalformedJsonException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

public class GetTopPostsTask {

    public List<PostModel> GetTopPostsTask() throws IOException {

        List<PostModel> postModelList = new ArrayList<>();

        HttpURLConnection conn = (HttpURLConnection) new URL("https://www.reddit.com/top.json?limit=1").openConnection();
        conn.setRequestMethod("GET");
        InputStream input = conn.getInputStream();

        Parser parserJson = new Parser();

        Parser.Listing listing = null;
        try {
            listing = parserJson.readJsonStream(input);
        } catch (MalformedJsonException e) {
            e.printStackTrace();
        }

        List<Parser.Message> children = listing.children;

        for (int i=0; i < children.size(); i++){

            PostModel postModel = new PostModel();
            postModel.setTitle(children.get(i).title);
            postModel.setAuthor(children.get(i).author);
            postModel.setDate("Hace 24 horas"); /// CAMBIAR ESTO
            postModel.setComments(children.get(i).num_comments);
            postModel.setUrlString(children.get(i).thumbnail);

            postModelList.add(postModel);
        }

        return postModelList;
    }

}
