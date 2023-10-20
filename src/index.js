"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
require("dotenv/config");
var firebirdClient_1 = require("src/libs/firebirdClient");
console.log("START APP");
firebirdClient_1.default.attach(function (err, db) {
    if (err)
        throw err;
});
