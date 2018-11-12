const dgram = require('dgram');
const client = dgram.createSocket('udp4');

setInterval(function () {
  client.send([Buffer.from(`
  {
    "item": "Goldfish Light sensor",
    "data": [
      {
        "name": "time",
        "value": "1541655075208"
      },
      {
        "name": "x",
        "value": ${Math.random() * 1000}
      },
      {
        "name": "y",
        "value": ${Math.random() * 1000}
      },
      {
        "name": "z",
        "value": ${Math.random() * 1000}
      }
    ],
    "time": "1541655075208"
  }
  `)], 8141, '127.0.0.1', (err) => {
    console.log(err);
  });
},100);

client.on('close', function () {
  client.close();
});
