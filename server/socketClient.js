const socket = require('socket.io-client')('http://127.0.0.1:9141');

socket.on('connect', function () {
  socket.send('hi');
});

socket.on('news', function (data) {
  console.log(data);
});

socket.on('sensors', function (data) {
  console.log(data);
});