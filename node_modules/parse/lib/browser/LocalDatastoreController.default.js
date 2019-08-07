"use strict";

var _LocalDatastoreUtils = require("./LocalDatastoreUtils");
/**
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 *
 * @flow
 */


var memMap = {};
var LocalDatastoreController = {
  fromPinWithName: function (name
  /*: string*/
  )
  /*: Array<Object>*/
  {
    if (!memMap.hasOwnProperty(name)) {
      return [];
    }

    var objects = JSON.parse(memMap[name]);
    return objects;
  },
  pinWithName: function (name
  /*: string*/
  , value
  /*: any*/
  ) {
    var values = JSON.stringify(value);
    memMap[name] = values;
  },
  unPinWithName: function (name
  /*: string*/
  ) {
    delete memMap[name];
  },
  getAllContents: function () {
    var LDS = {};

    for (var key in memMap) {
      if (memMap.hasOwnProperty(key) && (0, _LocalDatastoreUtils.isLocalDatastoreKey)(key)) {
        LDS[key] = JSON.parse(memMap[key]);
      }
    }

    return LDS;
  },
  getRawStorage: function () {
    return memMap;
  },
  clear: function () {
    for (var key in memMap) {
      if (memMap.hasOwnProperty(key) && (0, _LocalDatastoreUtils.isLocalDatastoreKey)(key)) {
        delete memMap[key];
      }
    }
  }
};
module.exports = LocalDatastoreController;