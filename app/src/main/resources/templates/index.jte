@template.layout.page(
content = @`
    <div class="container">
        <h1 class="mb-4">Welcome to Page Analyzer</h1>
        <p>Enter a URL to start analyzing web pages:</p>
        <form action="/urls" method="post" class="mb-3">
            <div class="input-group">
                <input type="text" name="url" class="form-control form-control-lg" placeholder="https://example.com" required>
                <button type="submit" class="btn btn-primary btn-lg">Analyze</button>
            </div>
        </form>
    </div>
`)