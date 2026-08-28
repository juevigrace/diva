// Fix for Webpack 5 no longer including polyfills for Node.js core modules
// sql.js uses these modules for Node.js support, but they aren't needed in the browser.
config.resolve = config.resolve || {};
config.resolve.fallback = config.resolve.fallback || {};
Object.assign(config.resolve.fallback, {
    fs: false,
    path: false,
    crypto: false,
});
