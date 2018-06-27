window.onload = function() {
      var pathArray = location.href.split( '/' );
      var protocol = pathArray[0];
      var host = pathArray[2];
      var basePath = protocol + '//' + host;

      // Build a system
      const ui = SwaggerUIBundle({
        url: basePath +"/swagger.json",
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
          SwaggerUIBundle.presets.apis,
          SwaggerUIStandalonePreset
        ],
        plugins: [
          SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
      })

      window.ui = ui
    }