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

/* global localStorage */


var LocalDatastoreController = {
  fromPinWithName: function (name
  /*: string*/
  )
  /*: Array<Object>*/
  {
    var values = localStorage.getItem(name);

    if (!values) {
      return [];
    }

    var objects = JSON.parse(values);
    return objects;
  },
  pinWithName: function (name
  /*: string*/
  , value
  /*: any*/
  ) {
    try {
      var values = JSON.stringify(value);
      localStorage.setItem(name, values);
    } catch (e) {
      // Quota exceeded, possibly due to Safari Private Browsing mode
      console.log(e.message);
    }
  },
  unPinWithName: function (name
  /*: string*/
  ) {
    localStorage.removeItem(name);
  },
  getAllContents: function ()
  /*: Object*/
  {
    var LDS = {};

    for (var i = 0; i < localStorage.length; i += 1) {
      var key = localStorage.key(i);

      if ((0, _LocalDatastoreUtils.isLocalDatastoreKey)(key)) {
        var value = localStorage.getItem(key);

        try {
          LDS[key] = JSON.parse(value);
        } catch (error) {
          console.error('Error getAllContents: ', error);
        }
      }
    }

    return LDS;
  },
  getRawStorage: function ()
  /*: Object*/
  {
    var storage = {};

    for (var i = 0; i < localStorage.length; i += 1) {
      var key = localStorage.key(i);
      var value = localStorage.getItem(key);
      storage[key] = value;
    }

    return storage;
  },
  clear: function ()
  /*: Promise*/
  {
    var toRemove = [];

    for (var i = 0; i < localStorage.length; i += 1) {
      var key = localStorage.key(i);

      if ((0, _LocalDatastoreUtils.isLocalDatastoreKey)(key)) {
        toRemove.push(key);
      }
    }

    var promises = toRemove.map(this.unPinWithName);
    return Promise.all(promises);
  }
};
module.exports = LocalDatastoreController;