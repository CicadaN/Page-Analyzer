package hexlet.code.controller;

import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class UrlController {

    public static void create(Context ctx) throws SQLException {
        String url = ctx.formParam("url");
        URI uri;
        try {
            uri = new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }
        String name = uri.getScheme() + "://" + uri.getHost();
        if (uri.getPort() != -1) {
            name += ":" + uri.getPort();
        }
        if (UrlRepository.findByName(name).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }
        Url urlObj = new Url(name);
        UrlRepository.save(urlObj);
        ctx.sessionAttribute("flash", "Успешно добавлено");
        ctx.sessionAttribute("flashType", "success");
        ctx.redirect(NamedRoutes.rootPath());
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.findAll();
        var latestCheckUrl = UrlCheckRepository.findLatestCheckUrl();
        UrlsPage page = new UrlsPage(urls, latestCheckUrl);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Url url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with ID: " + id + " not found"));
        var urlCheck = UrlCheckRepository.findbyId(id);
        UrlPage page = new UrlPage(url, urlCheck);
        page.setFlash(ctx.sessionAttribute("flash"));
        page.setFlashType(ctx.sessionAttribute("flashType"));
        ctx.render("urls/show.jte", Map.of("urlPage", page));
    }

    public static void check(Context ctx) throws SQLException {
        int urlId = Integer.parseInt(ctx.pathParam("id"));
        Url url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url with ID: " + urlId + " not found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());

            int statusCode = response.getStatus();
            String title = doc.title();
            String h1 = doc.select("h1").isEmpty() ? "" : doc.select("h1").get(0).text();
            String description = doc.select("meta[name=description]").attr("content");

            UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description);
            urlCheck.setUrlId(urlId);
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute("flash", "URL check was successful!");
            ctx.sessionAttribute("flashType", "success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Failed to check the URL");
            ctx.sessionAttribute("flashType", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
