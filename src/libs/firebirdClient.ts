import firebird from "node-firebird"
import 'dotenv/config'

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
  static attach(callback: firebird.DatabaseCallback) {
    firebird.attach(options, callback)
  }
}