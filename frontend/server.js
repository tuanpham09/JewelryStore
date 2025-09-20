const express = require('express');
const path = require('path');
const app = express();

// Serve static files
app.use(express.static(__dirname + '/dist/frontend/browser'));

// Proxy API requests to backend
const { createProxyMiddleware } = require('http-proxy-middleware');
app.use('/api', createProxyMiddleware({ 
  target: 'http://localhost:8080', 
  changeOrigin: true 
}));

// Send all other requests to the Angular app
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname + '/dist/frontend/browser/index.html'));
});

// Start server
const port = process.env.PORT || 4200;
app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
