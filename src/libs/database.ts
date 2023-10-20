import firebird from "node-firebird"
import 'dotenv/config'
import * as console from "console";
import * as process from "process";

const dbOptions : firebird.Options = {
  host : process.env["DB_HOST"],
  port : Number(process.env["DB_PORT"]),
  database: process.env["DB_PATH"],
  user: process.env["DB_LOGIN"],
  password: process.env["DB_PASSWORD"],
  // lowercase_keys: false,
  // role: undefined,
  // pageSize: 4096,
  // retryConnectionInterval: 1000,
  // blobAsText: false
};

function executeQuery(prompt: string, params: any[], callback: firebird.QueryCallback){

  firebird.attach(dbOptions, function(err, db) {
    if (err) return callback(err, []);
    console.log(`Database:\tconnected!`);
    
    db.query(prompt, params, (err, result) => {
      db.detach();
      console.log(`Database:\tdisconnected!`);

      if (err) return callback(err, []);
      else return callback(undefined, result);
    });

  });
}

export {executeQuery};