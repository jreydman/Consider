import firebird from "node-firebird"
import 'dotenv/config'
import * as console from "console";

const options : firebird.Options = {
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

export default class FirebirdClient {
  static driver: firebird.Database
  static attach(callback: Function) : void {
    firebird.attach(options, (err, db) : void => {
      if (err) throw err
      try {
        console.log(`Database: connected!`);
        callback()
      } catch (e) { console.warn(e as Error) }
      finally {
        console.log(`Database: disconnected!`);
        db.detach()
      }
    })
  }
}