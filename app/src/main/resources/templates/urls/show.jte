@import hexlet.code.dto.url.UrlPage
@import hexlet.code.model.Url
@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter

@param UrlPage urlPage

@template.layout.page(
content = @`
    @if(urlPage != null && urlPage.getFlash() != null)
        <div class="alert alert-${urlPage.getFlashType()}" role="alert">
            ${urlPage.getFlash()}
        </div>
    @endif
    @if(urlPage.getUrl() != null)
            <h1>
                Site: ${urlPage.getUrl().getName()}
            </h1>
            <table class="table table-bordered table-hover mt-3">
                <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${urlPage.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td>${urlPage.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Date Created</td>
                        <td>${urlPage.getUrl().getFormattedCreatedAt()}</td>
                    </tr>
                </tbody>
            </table>
            <h2>
                Проверки
            </h2>
            <form method="post" action="${NamedRoutes.urlCheck(urlPage.getUrl().getId())}">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr><th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                </tr></thead>
                <tbody>
                @for(var checkUrl : urlPage.getCheckUrl())
                    <tr>
                        <td>
                            ${checkUrl.getId()}
                        </td>
                        <td>
                            ${checkUrl.getStatusCode()}
                        </td>
                        <td>
                            ${checkUrl.getTitle()}
                        </td>
                        <td>
                            ${checkUrl.getH1()}
                        </td>
                        <td>
                            ${checkUrl.getDescription()}
                        </td>
                        <td>
                            ${checkUrl.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
    @endif
`)
