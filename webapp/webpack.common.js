const webpack = require('webpack');

const merge = require('webpack-merge');
const path = require('path');

const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');

// const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
// const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');

const CopyPlugin = require('copy-webpack-plugin');
const FaviconsWebpackPlugin = require('favicons-webpack-plugin');

var HtmlReplaceWebpackPlugin = require('html-replace-webpack-plugin');

// Podemos usar o DevServer tanto em DEV como em PROD, não usar essa variável para separar os ambientes
const isDevServer = process.env.WEBPACK_DEV_SERVER;

const config = {
	entry: {
		'app': './app.js'
	},
	externals: {
		'$env': 'AppConfig'
	},
	plugins: [
		new CleanWebpackPlugin(),
		new HtmlWebpackPlugin({
			title: 'Dezi',
			template: 'index.html',
			chunks: ['app']
		}),
		new MiniCssExtractPlugin({
			filename: '[name].[hash:16].css',
			chunkFilename: '[id].[hash:16].css'
		}),
		new webpack.ProvidePlugin({
			$: 'jquery',
			jQuery: 'jquery',
			'window.jQuery': 'jquery'
		}),
		new CopyPlugin([
			{ from: 'partials/i18n/*.json', to: 'i18n/root/[1].json', test: /partials\/i18n\/(.+).json$/ },
			{ from: 'partials/**/i18n/*.json', to: 'i18n/[1]/[2].json', test: /partials\/(.+)\/i18n\/(.+)\.json$/ },
			{ from: 'base/*.js', to: 'base/[1].js', test: /base\/(.+).js$/ }
		]),
		new FaviconsWebpackPlugin({
			logo: './img/logos/logo-dezi-sq.png',
			prefix: 'icons-[hash:16]/',
			inject: true,
			persistCache: true
		}),
		new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
		new webpack.ContextReplacementPlugin(/moment[/\\]locale$/, /pt|pt-br/),
	],
	output: {
		chunkFilename: '[name].[chunkhash].js',
		filename: '[name].[hash:16].js',
		path: path.resolve(__dirname, 'dist')
	},
	resolve: {
		alias: {
			env: path.resolve(__dirname, 'env'),
			actions: path.resolve(__dirname, 'actions'),
			components: path.resolve(__dirname, 'components'),
			lib: path.resolve(__dirname, 'lib'),
			img: path.resolve(__dirname, 'img'),
			style: path.resolve(__dirname, 'style'),
			font: path.resolve(__dirname, 'font'),
			fuse: path.resolve(__dirname, 'lib/fuse'),
			partials: path.resolve(__dirname, 'partials'),
			services: path.resolve(__dirname, 'services'),
			model: path.resolve(__dirname, 'partials/model'),
			utils: path.resolve(__dirname, 'utils'),
		},
		extensions: ['.ts', '.js']
	},
	module: {
		rules: [
			{
				test: /\.ts$/,
				use: [
					'ts-loader',
					'eslint-loader'
				],
				exclude: /node_modules/
			},
			{
				test: /\.js$/,
				use: [
					'babel-loader?cacheDirectory',
					'eslint-loader'
				],
				exclude: /node_modules/
			},
			{
				test: /\.html$/,
				use: [
					// 'html-loader',
					'ng-cache-loader?prefix=',
				],
				exclude: [
					/node_modules/,
					path.resolve(__dirname, 'index.html')
				]
			},
			{
				test: /\.css$/,
				use: [
					MiniCssExtractPlugin.loader,
					'css-loader'
				]
			},
			{
				//TODO Separar prod e dev
				test: /\.scss$/,
				use: [
					MiniCssExtractPlugin.loader,
					{
						loader: 'css-loader',
						options: { sourceMap: true }
					},
					{
						loader: 'sass-loader',
						options: { sourceMap: true }
					}
				],
				exclude: /node_modules/
			},
			{
				test: /\.(png|svg|jpg|gif)$/,
				use: {
					loader: 'file-loader?limit=100000',
					options: {
						name: 'images/[hash:16].[ext]'
					}
				}
			},
			{
				test: /\.(woff|woff2|eot|ttf|otf)$/,
				use: {
					loader: 'file-loader',
					options: {
						name: 'fonts/[hash:16].[ext]'
					}
				}
			}
		]
	},
	// optimization: {
	// 	splitChunks: {
	// 		chunks: 'all',
	// 		minSize: 30000,
	// 		maxSize: 0,
	// 		minChunks: 1,
	// 		maxAsyncRequests: 5,
	// 		maxInitialRequests: 3,
	// 		automaticNameDelimiter: '~',
	// 		name: true,
	// 		cacheGroups: {
	// 			vendors: {
	// 				test: /[\\/]node_modules[\\/]/,
	// 				priority: -10
	// 			},
	// 			default: {
	// 				minChunks: 2,
	// 				priority: -20,
	// 				reuseExistingChunk: true
	// 			}
	// 		}
	// 	}
	// },
	devServer: {
		port: 3000,
		contentBase: './dist'
	}
};

module.exports = function(MODE) {
	const envConfig = {
		mode: MODE,
		plugins: []
	};

	if (isDevServer) {
		envConfig.plugins.push(
			new HtmlReplaceWebpackPlugin([{
				pattern: '$env',
				replacement: MODE
			}])
		);
	}

	return merge(config, envConfig);
};
