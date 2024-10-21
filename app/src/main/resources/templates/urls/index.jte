@import hexlet.code.dto.url.UrlsPage
@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter
@import static hexlet.code.util.NamedRoutes.urlCheck
@import static hexlet.code.util.NamedRoutes.urlPath
@param UrlsPage page

@template.layout.page(
content = @`
    @if(page.getFlash() != null)
        <div class="alert alert-${page.getFlashType()}" role="alert">
            ${page.getFlash()}
        </div>
    @endif
    <h1>All urls page</h1>
    <table class="table">
        <thead>
        <tr>
            <th class="col-1">ID</th>
            <th>Name</th>
            <th class="col-2">Latest Check</th>
            <th class="col-1">Status Code</th>
        </tr>
        </thead>
        @for(Url url : page.getUrls())
            !{var urlCheck = page.getLatestCheckUrl().get(url.getId());}
            <tr>
                <td>
                    ${url.getId()}
                </td>
                <td>
                    <a href="${urlPath(url.getId())}">${url.getName()}</a>
                </td>
                <td>
                    @if(urlCheck != null)
                        ${urlCheck.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                    @endif
                </td>
                <td>
                    @if(urlCheck != null)
                        ${urlCheck.getStatusCode()}
                    @endif
                </td>
            </tr>
        @endfor
    </table>
    @if(page.getUrls().isEmpty())
        <div class="alert alert-info" role="alert">
            There is no any URLs added yet.
        </div>
        <a class="btn btn-primary" href="${NamedRoutes.ROOT_PATH}" role="button">Add some URL</a>
    @endif
`)
