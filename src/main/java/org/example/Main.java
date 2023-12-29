package org.example;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ParseException {

        int pageIndex = 0;
        int downloadedPics = 0;
        String keyword = "snow";
        String capitalizedKeyword = keyword.substring(0, 1).toUpperCase() + keyword.substring(1);
        File savePath = new File("C:\\Users\\phuvo\\OneDrive\\Desktop\\Crawl Unsplash\\Data Crawl Unsplash\\" + capitalizedKeyword);

        if (!Files.exists(savePath.toPath())) {
            savePath.mkdir();
        }

        while (true) {

            String url = "https://unsplash.com/napi/search/photos?page=" + pageIndex + "&per_page=20&query=" + keyword + "&xp=pricing%3Acontrol";

            HttpClient client = HttpClient.newBuilder()
                    .build();
            HttpRequest request = (HttpRequest) HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonData = (JSONObject) jsonParser.parse(response.body());
            JSONArray jsonArray = (JSONArray) jsonData.get("results");

            if (jsonArray.size() > 0) {
                for (int j = 0; j < jsonArray.size(); j++) {

                    JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                    String slug = (String) jsonObject.get("slug");
                    JSONObject urls = (JSONObject) jsonObject.get("urls");
                    URL fullUrl = new URL((String) urls.get("full"));
//                    System.out.println(jsonObject);
//                    System.out.println(jsonObject.get("sponsorship"));
                    JSONObject user = (JSONObject) jsonObject.get("user");
//                    System.out.println(sponsorship.get("sponsor"));
//                    JSONObject username = (JSONObject) sponsorship.get("username");
                    String username = (String) user.get("username");

//                    System.out.println(slug);

                    System.out.println("Downloading: " + username + "_" + slug);
                    URL picUrl = new URL((String) urls.get("full"));
                    File path = new File(savePath + "\\" + username + slug + ".jpg");
                    FileUtils.copyURLToFile(picUrl, path);
                    downloadedPics++;
                    System.out.println("Downloaded: " + username + "_" + slug + ". Total pictures downloaded: " + downloadedPics);
                }
            } else {
                break;
            }

            pageIndex++;
        }

        System.out.println("Downloaded all pictures!!!");
    }
}