const { spawnSync } = require('child_process');
var packageJson = require('./package.json');

spawnSync('mvn', [ 'versions:set', '-DnewVersion=' + packageJson.version ], { stdio: 'inherit' });
