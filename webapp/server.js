var express = require('express');
var compression = require('compression');

const ENV = process.env.NODE_ENV || 'development';
const PORT = process.env.PORT || 3000;

var app = express();

console.log('Environment: ' + ENV);

if (ENV === 'production') {
	console.log('Enabling server compression');
	app.use(compression());
}

app.use('/env', function(req, res, next) {
	req.url = req.url.replace(/\$env/, ENV);
	next();
});

app.use('/env', express.static('env'));
app.use('/', express.static('dist'));

app.listen(PORT, function () {
	console.log('Server listening on port ' + PORT);
});
