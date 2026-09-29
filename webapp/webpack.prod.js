const merge = require('webpack-merge');

const MODE = 'production';

const common = require('./webpack.common.js')(MODE);

module.exports = merge(common, {
	// devtool: 'inline-source-map',
	output: {
		publicPath: '/'
	}
});
