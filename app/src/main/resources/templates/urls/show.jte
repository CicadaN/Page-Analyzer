@import hexlet.code.dto.url.UrlPage
@import hexlet.code.model.Url
@import hexlet.code.util.NamedRoutes

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
    @endif
`)
