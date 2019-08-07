 The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

 The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

var Parse = require('parse');
var express = require('express');
var ParseServer = require('parse-server').ParseServer;
var path = require('path');

Parse.initialize("ejsfbu-money");
Parse.serverURL = 'https://youth-financial-planning.herokuapp.com/parse'

var server = new ParseServer({
  databaseURI: databaseUri || 'mongodb://<dbuser>:<dbpassword>@ds151876.mlab.com:51876/heroku_b8sd9l2t',
  cloud: process.env.CLOUD_CODE_MAIN || __dirname + '/cloud/main.js',
  appId: process.env.APP_ID || 'ejsfbu_money',
  masterKey: process.env.MASTER_KEY || 'dfdfLHJFKNpjaqljrfg7836495ysdflkgjnlhoBJU4895hJFjrhkbnskdjhkdogkladjshfcat',
  push: {
    android: {
      apiKey: 'AAAAtVWOmx8:APA91bEcEZJRHp5992KUGR8GeWYdCsGHEabLih-DOW9VIZX4jt76pVYWMr6iaDpaRlRR_ISEZWri1W10USK0Z20FUMcXOVpnmr6WVbk-gvri2X2Suy9J9ReAM7bjAhIihTWug_yu7sMU'
    }
  }
});

var app = express();

var mountPath = process.env.PARSE_MOUNT || '/parse';
