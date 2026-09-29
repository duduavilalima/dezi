const merge = require('webpack-merge');

const MODE = 'development';

const common = require('./webpack.common.js')(MODE);

module.exports = merge(common, {
	devtool: 'inline-source-map',
});
