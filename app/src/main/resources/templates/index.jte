@import hexlet.code.dto.url.BuildUrlPage
@import hexlet.code.util.NamedRoutes
@param BuildUrlPage page = null

@template.layout.page(
content = @`
    <div class="container">

        @if(page != null && page.getFlash() != null)
            <div class="alert alert-${page.getFlashType()}" role="alert">
                ${page.getFlash()}
            </div>
        @endif

        <h1 class="mb-4">Welcome to Page Analyzer</h1>
        <p>Enter a URL to start analyzing web pages:</p>
        <form action="${NamedRoutes.urlsPath()}" method="post" class="mb-3">
            <div class="input-group">
                <input type="text" name="url" class="form-control form-control-lg" placeholder="https://example.com" required>
                <button type="submit" class="btn btn-primary btn-lg">Analyze</button>
            </div>
        </form>
    </div>
`)