package hexlet.code.controller;

import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

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

        if (url == null || url.isEmpty()) {
            ctx.sessionAttribute("flash", "URL не может быть пустым");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        URI uri;
        try {
            uri = new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String name = uri.getScheme() + "://" + uri.getHost()
                + (":" + uri.getPort());
        if (UrlRepository.findByName(name) != null) {
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
        UrlsPage page = new UrlsPage(urls);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Url url = UrlRepository.findById(id);
        if (url == null) {
            throw new NotFoundResponse("Url with ID: " + id + " not found");
        }
        UrlPage page = new UrlPage(url);
        page.setFlash(ctx.sessionAttribute("flash"));
        page.setFlashType(ctx.sessionAttribute("flashType"));
        ctx.render("urls/show.jte", Map.of("urlPage", page));
    }
}
