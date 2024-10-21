package hexlet.code.util;

public class NamedRoutes {
    public static final String ROOT_PATH = "/";
    public static final String URLS_PATH = "/urls";
    public static final String CHECK_PATH = "/checks";

    public static String rootPath() {
        return ROOT_PATH;
    }

    public static String urlsPath() {
        return URLS_PATH;
    }

    public static String urlPath(int id) {
        return URLS_PATH + "/" + id;
    }

    public static String urlPath(String id) {
        return URLS_PATH + "/" + id;
    }

    public static String urlCheck(int id) {
        return URLS_PATH + "/" + id + CHECK_PATH;
    }

    public static String urlCheck(String id) {
        return URLS_PATH + "/" + id + CHECK_PATH;
    }
}
