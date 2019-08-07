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


var LocalDatastoreController = {
  fromPinWithName: function (name
  /*: string*/
  )
  /*: Array<Object>*/
  {
    var values = wx.getStorageSync(name);

    if (!values) {
      return [];
    }

    return values;
  },
  pinWithName: function (name
  /*: string*/
  , value
  /*: any*/
  ) {
    try {
      wx.setStorageSync(name, value);
    } catch (e) {// Quota exceeded
    }
  },
  unPinWithName: function (name
  /*: string*/
  ) {
    wx.removeStorageSync(name);
  },
  getAllContents: function ()
  /*: Object*/
  {
    var res = wx.getStorageInfoSync();
    var keys = res.keys;
    var LDS = {};
    var _iteratorNormalCompletion = true;
    var _didIteratorError = false;
    var _iteratorError = undefined;

    try {
      for (var _iterator = keys[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
        var key = _step.value;

        if ((0, _LocalDatastoreUtils.isLocalDatastoreKey)(key)) {
          LDS[key] = wx.getStorageSync(key);
        }
      }
    } catch (err) {
      _didIteratorError = true;
      _iteratorError = err;
    } finally {
      try {
        if (!_iteratorNormalCompletion && _iterator.return != null) {
          _iterator.return();
        }
      } finally {
        if (_didIteratorError) {
          throw _iteratorError;
        }
      }
    }

    return LDS;
  },
  getRawStorage: function ()
  /*: Object*/
  {
    var res = wx.getStorageInfoSync();
    var keys = res.keys;
    var storage = {};
    var _iteratorNormalCompletion2 = true;
    var _didIteratorError2 = false;
    var _iteratorError2 = undefined;

    try {
      for (var _iterator2 = keys[Symbol.iterator](), _step2; !(_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done); _iteratorNormalCompletion2 = true) {
        var key = _step2.value;
        storage[key] = wx.getStorageSync(key);
      }
    } catch (err) {
      _didIteratorError2 = true;
      _iteratorError2 = err;
    } finally {
      try {
        if (!_iteratorNormalCompletion2 && _iterator2.return != null) {
          _iterator2.return();
        }
      } finally {
        if (_didIteratorError2) {
          throw _iteratorError2;
        }
      }
    }

    return storage;
  },
  clear: function ()
  /*: Promise*/
  {
    var res = wx.getStorageInfoSync();
    var keys = res.keys;
    var toRemove = [];
    var _iteratorNormalCompletion3 = true;
    var _didIteratorError3 = false;
    var _iteratorError3 = undefined;

    try {
      for (var _iterator3 = keys[Symbol.iterator](), _step3; !(_iteratorNormalCompletion3 = (_step3 = _iterator3.next()).done); _iteratorNormalCompletion3 = true) {
        var key = _step3.value;

        if ((0, _LocalDatastoreUtils.isLocalDatastoreKey)(key)) {
          toRemove.push(key);
        }
      }
    } catch (err) {
      _didIteratorError3 = true;
      _iteratorError3 = err;
    } finally {
      try {
        if (!_iteratorNormalCompletion3 && _iterator3.return != null) {
          _iterator3.return();
        }
      } finally {
        if (_didIteratorError3) {
          throw _iteratorError3;
        }
      }
    }

    var promises = toRemove.map(this.unPinWithName);
    return Promise.all(promises);
  }
};
module.exports = LocalDatastoreController;