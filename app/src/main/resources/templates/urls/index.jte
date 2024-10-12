@import hexlet.code.dto.url.UrlsPage
@import hexlet.code.model.Url
@import hexlet.code.util.NamedRoutes
@import static hexlet.code.util.NamedRoutes.urlPath
@param UrlsPage page

@template.layout.page(
content = @`
    <h1>All urls page</h1>
    <table class="table">
        <thead>
        <tr>
            <th class="col-1">ID</th>
            <th>Name</th>
            <th class="col-2">Create Date</th>
        </tr>
        </thead>
        @for(Url url : page.getUrls())
            <tr>
                <td>
                    ${url.getId()}
                </td>
                <td>
                    <a href="${urlPath(url.getId())}">${url.getName()}</a>
                </td>
                <td>
                    ${url.getFormattedCreatedAt()}
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