const dgram = require('dgram');
const server = dgram.createSocket('udp4');

const { Pool } = require('pg');

const io = require('socket.io')(9141);

const conf = {
  user: 'postgres',
  host: '127.0.0.1',
  database: 'mydb',
  password: 'postgres',
  port: 5432
};

const pool = new Pool(conf);

pool.connect();

pool.query('SELECT NOW()', (err, res) => {
  console.log(err, res);
});

server.on('error',(err) => {
  console.log('udp server error');
});

server.on('message', (msg, rinfo) => {
  if (Math.random() > 0.1) return;
  console.log(`${rinfo.address}:${rinfo.port}:${msg}`);
  let pck = JSON.parse(msg);
  pool.query('INSERT INTO sensors(ITEM, STAMP, JSONSTR) VALUES ($1, $2, $3)',
    [pck.item.toString(), pck.time * 1, msg.toString()],
    (err, res) => {
      console.log(err, res);
  });
  io.emit('sensors', msg.toString());
});

server.on('listening', () => {
  console.log(`listening ${server.address().address}:${server.address().port}`);
});

server.bind({
  address: '0.0.0.0',
  port: 8141
});

io.on('connection', (socket) => {
  console.log('a user connect');
  socket.emit('hi');
  io.emit('news', 'a user connect');
});

io.on('connect', () => {
  console.log('connect');
});

io.on('hi', () => {
  console.log('hi');
});

