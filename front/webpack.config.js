var packageJSON = require('./package.json');
var path = require('path');
var webpack = require('webpack');

const PATHS = {
    build: path.join(__dirname, 'dist')
};

module.exports = {
    entry: ["@babel/polyfill", './js/example.js'],

    output: {
        path: PATHS.build,
        filename: 'main.js'
    },

    module: {
        loaders: [
            {test: /\.jsx?$/, loader: 'babel',}
        ]
    }
};