"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");

var _regenerator = _interopRequireDefault(require("@babel/runtime/regenerator"));

var _toConsumableArray2 = _interopRequireDefault(require("@babel/runtime/helpers/toConsumableArray"));

var _slicedToArray2 = _interopRequireDefault(require("@babel/runtime/helpers/slicedToArray"));

var _asyncToGenerator2 = _interopRequireDefault(require("@babel/runtime/helpers/asyncToGenerator"));

var _CoreManager = _interopRequireDefault(require("./CoreManager"));

var _ParseQuery = _interopRequireDefault(require("./ParseQuery"));

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

/**
 * Provides a local datastore which can be used to store and retrieve <code>Parse.Object</code>. <br />
 * To enable this functionality, call <code>Parse.enableLocalDatastore()</code>.
 *
 * Pin object to add to local datastore
 *
 * <pre>await object.pin();</pre>
 * <pre>await object.pinWithName('pinName');</pre>
 *
 * Query pinned objects
 *
 * <pre>query.fromLocalDatastore();</pre>
 * <pre>query.fromPin();</pre>
 * <pre>query.fromPinWithName();</pre>
 *
 * <pre>const localObjects = await query.find();</pre>
 *
 * @class Parse.LocalDatastore
 * @static
 */


var LocalDatastore = {
  isEnabled: false,
  isSyncing: false,
  fromPinWithName: function (name
  /*: string*/
  )
  /*: Promise<Array<Object>>*/
  {
    var controller = _CoreManager.default.getLocalDatastoreController();

    return controller.fromPinWithName(name);
  },
  pinWithName: function (name
  /*: string*/
  , value
  /*: any*/
  )
  /*: Promise<void>*/
  {
    var controller = _CoreManager.default.getLocalDatastoreController();

    return controller.pinWithName(name, value);
  },
  unPinWithName: function (name
  /*: string*/
  )
  /*: Promise<void>*/
  {
    var controller = _CoreManager.default.getLocalDatastoreController();

    return controller.unPinWithName(name);
  },
  _getAllContents: function ()
  /*: Promise<Object>*/
  {
    var controller = _CoreManager.default.getLocalDatastoreController();

    return controller.getAllContents();
  },
  // Use for testing
  _getRawStorage: function ()
  /*: Promise<Object>*/
  {
    var controller = _CoreManager.default.getLocalDatastoreController();

    return controller.getRawStorage();
  },
  _clear: function ()
  /*: Promise<void>*/
  {
    var controller = _CoreManager.default.getLocalDatastoreController();

    return controller.clear();
  },
  // Pin the object and children recursively
  // Saves the object and children key to Pin Name
  _handlePinAllWithName: function () {
    var _handlePinAllWithName2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee(name
    /*: string*/
    , objects
    /*: Array<ParseObject>*/
    ) {
      var pinName, toPinPromises, objectKeys, _iteratorNormalCompletion, _didIteratorError, _iteratorError, _iterator, _step, parent, children, parentKey, json, objectKey, fromPinPromise, _ref, _ref2, pinned, toPin;

      return _regenerator.default.wrap(function (_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              pinName = this.getPinName(name);
              toPinPromises = [];
              objectKeys = [];
              _iteratorNormalCompletion = true;
              _didIteratorError = false;
              _iteratorError = undefined;
              _context.prev = 6;

              for (_iterator = objects[Symbol.iterator](); !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                parent = _step.value;
                children = this._getChildren(parent);
                parentKey = this.getKeyForObject(parent);
                json = parent._toFullJSON();

                if (parent._localId) {
                  json._localId = parent._localId;
                }

                children[parentKey] = json;

                for (objectKey in children) {
                  objectKeys.push(objectKey);
                  toPinPromises.push(this.pinWithName(objectKey, [children[objectKey]]));
                }
              }

              _context.next = 14;
              break;

            case 10:
              _context.prev = 10;
              _context.t0 = _context["catch"](6);
              _didIteratorError = true;
              _iteratorError = _context.t0;

            case 14:
              _context.prev = 14;
              _context.prev = 15;

              if (!_iteratorNormalCompletion && _iterator.return != null) {
                _iterator.return();
              }

            case 17:
              _context.prev = 17;

              if (!_didIteratorError) {
                _context.next = 20;
                break;
              }

              throw _iteratorError;

            case 20:
              return _context.finish(17);

            case 21:
              return _context.finish(14);

            case 22:
              fromPinPromise = this.fromPinWithName(pinName);
              _context.next = 25;
              return Promise.all([fromPinPromise, toPinPromises]);

            case 25:
              _ref = _context.sent;
              _ref2 = (0, _slicedToArray2.default)(_ref, 1);
              pinned = _ref2[0];
              toPin = (0, _toConsumableArray2.default)(new Set([].concat((0, _toConsumableArray2.default)(pinned || []), objectKeys)));
              return _context.abrupt("return", this.pinWithName(pinName, toPin));

            case 30:
            case "end":
              return _context.stop();
          }
        }
      }, _callee, this, [[6, 10, 14, 22], [15,, 17, 21]]);
    }));

    return function () {
      return _handlePinAllWithName2.apply(this, arguments);
    };
  }(),
  // Removes object and children keys from pin name
  // Keeps the object and children pinned
  _handleUnPinAllWithName: function () {
    var _handleUnPinAllWithName2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee2(name
    /*: string*/
    , objects
    /*: Array<ParseObject>*/
    ) {
      var localDatastore, pinName, promises, objectKeys, _iteratorNormalCompletion2, _didIteratorError2, _iteratorError2, _iterator2, _step2, _objectKeys, parent, children, parentKey, pinned, _iteratorNormalCompletion3, _didIteratorError3, _iteratorError3, _iterator3, _step3, objectKey, hasReference, key, pinnedObjects;

      return _regenerator.default.wrap(function (_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              _context2.next = 2;
              return this._getAllContents();

            case 2:
              localDatastore = _context2.sent;
              pinName = this.getPinName(name);
              promises = [];
              objectKeys = [];
              _iteratorNormalCompletion2 = true;
              _didIteratorError2 = false;
              _iteratorError2 = undefined;
              _context2.prev = 9;

              for (_iterator2 = objects[Symbol.iterator](); !(_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done); _iteratorNormalCompletion2 = true) {
                parent = _step2.value;
                children = this._getChildren(parent);
                parentKey = this.getKeyForObject(parent);

                (_objectKeys = objectKeys).push.apply(_objectKeys, [parentKey].concat((0, _toConsumableArray2.default)(Object.keys(children))));
              }

              _context2.next = 17;
              break;

            case 13:
              _context2.prev = 13;
              _context2.t0 = _context2["catch"](9);
              _didIteratorError2 = true;
              _iteratorError2 = _context2.t0;

            case 17:
              _context2.prev = 17;
              _context2.prev = 18;

              if (!_iteratorNormalCompletion2 && _iterator2.return != null) {
                _iterator2.return();
              }

            case 20:
              _context2.prev = 20;

              if (!_didIteratorError2) {
                _context2.next = 23;
                break;
              }

              throw _iteratorError2;

            case 23:
              return _context2.finish(20);

            case 24:
              return _context2.finish(17);

            case 25:
              objectKeys = (0, _toConsumableArray2.default)(new Set(objectKeys));
              pinned = localDatastore[pinName] || [];
              pinned = pinned.filter(function (item) {
                return !objectKeys.includes(item);
              });

              if (pinned.length == 0) {
                promises.push(this.unPinWithName(pinName));
                delete localDatastore[pinName];
              } else {
                promises.push(this.pinWithName(pinName, pinned));
                localDatastore[pinName] = pinned;
              }

              _iteratorNormalCompletion3 = true;
              _didIteratorError3 = false;
              _iteratorError3 = undefined;
              _context2.prev = 32;
              _iterator3 = objectKeys[Symbol.iterator]();

            case 34:
              if (_iteratorNormalCompletion3 = (_step3 = _iterator3.next()).done) {
                _context2.next = 51;
                break;
              }

              objectKey = _step3.value;
              hasReference = false;
              _context2.t1 = _regenerator.default.keys(localDatastore);

            case 38:
              if ((_context2.t2 = _context2.t1()).done) {
                _context2.next = 47;
                break;
              }

              key = _context2.t2.value;

              if (!(key === _LocalDatastoreUtils.DEFAULT_PIN || key.startsWith(_LocalDatastoreUtils.PIN_PREFIX))) {
                _context2.next = 45;
                break;
              }

              pinnedObjects = localDatastore[key] || [];

              if (!pinnedObjects.includes(objectKey)) {
                _context2.next = 45;
                break;
              }

              hasReference = true;
              return _context2.abrupt("break", 47);

            case 45:
              _context2.next = 38;
              break;

            case 47:
              if (!hasReference) {
                promises.push(this.unPinWithName(objectKey));
              }

            case 48:
              _iteratorNormalCompletion3 = true;
              _context2.next = 34;
              break;

            case 51:
              _context2.next = 57;
              break;

            case 53:
              _context2.prev = 53;
              _context2.t3 = _context2["catch"](32);
              _didIteratorError3 = true;
              _iteratorError3 = _context2.t3;

            case 57:
              _context2.prev = 57;
              _context2.prev = 58;

              if (!_iteratorNormalCompletion3 && _iterator3.return != null) {
                _iterator3.return();
              }

            case 60:
              _context2.prev = 60;

              if (!_didIteratorError3) {
                _context2.next = 63;
                break;
              }

              throw _iteratorError3;

            case 63:
              return _context2.finish(60);

            case 64:
              return _context2.finish(57);

            case 65:
              return _context2.abrupt("return", Promise.all(promises));

            case 66:
            case "end":
              return _context2.stop();
          }
        }
      }, _callee2, this, [[9, 13, 17, 25], [18,, 20, 24], [32, 53, 57, 65], [58,, 60, 64]]);
    }));

    return function () {
      return _handleUnPinAllWithName2.apply(this, arguments);
    };
  }(),
  // Retrieve all pointer fields from object recursively
  _getChildren: function (object
  /*: ParseObject*/
  ) {
    var encountered = {};

    var json = object._toFullJSON();

    for (var key in json) {
      if (json[key] && json[key].__type && json[key].__type === 'Object') {
        this._traverse(json[key], encountered);
      }
    }

    return encountered;
  },
  _traverse: function (object
  /*: any*/
  , encountered
  /*: any*/
  ) {
    if (!object.objectId) {
      return;
    } else {
      var objectKey = this.getKeyForObject(object);

      if (encountered[objectKey]) {
        return;
      }

      encountered[objectKey] = object;
    }

    for (var key in object) {
      var json = object[key];

      if (!object[key]) {
        json = object;
      }

      if (json.__type && json.__type === 'Object') {
        this._traverse(json, encountered);
      }
    }
  },
  // Transform keys in pin name to objects
  _serializeObjectsFromPinName: function () {
    var _serializeObjectsFromPinName2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee3(name
    /*: string*/
    ) {
      var _this = this,
          _ref3;

      var localDatastore, allObjects, key, pinName, pinned, promises, objects;
      return _regenerator.default.wrap(function (_context3) {
        while (1) {
          switch (_context3.prev = _context3.next) {
            case 0:
              _context3.next = 2;
              return this._getAllContents();

            case 2:
              localDatastore = _context3.sent;
              allObjects = [];

              for (key in localDatastore) {
                if (key.startsWith(_LocalDatastoreUtils.OBJECT_PREFIX)) {
                  allObjects.push(localDatastore[key][0]);
                }
              }

              if (name) {
                _context3.next = 7;
                break;
              }

              return _context3.abrupt("return", allObjects);

            case 7:
              pinName = this.getPinName(name);
              pinned = localDatastore[pinName];

              if (Array.isArray(pinned)) {
                _context3.next = 11;
                break;
              }

              return _context3.abrupt("return", []);

            case 11:
              promises = pinned.map(function (objectKey) {
                return _this.fromPinWithName(objectKey);
              });
              _context3.next = 14;
              return Promise.all(promises);

            case 14:
              objects = _context3.sent;
              objects = (_ref3 = []).concat.apply(_ref3, (0, _toConsumableArray2.default)(objects));
              return _context3.abrupt("return", objects.filter(function (object) {
                return object != null;
              }));

            case 17:
            case "end":
              return _context3.stop();
          }
        }
      }, _callee3, this);
    }));

    return function () {
      return _serializeObjectsFromPinName2.apply(this, arguments);
    };
  }(),
  // Replaces object pointers with pinned pointers
  // The object pointers may contain old data
  // Uses Breadth First Search Algorithm
  _serializeObject: function () {
    var _serializeObject2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee4(objectKey
    /*: string*/
    , localDatastore
    /*: any*/
    ) {
      var LDS, root, queue, meta, uniqueId, nodeId, subTreeRoot, field, value, key, pointer;
      return _regenerator.default.wrap(function (_context4) {
        while (1) {
          switch (_context4.prev = _context4.next) {
            case 0:
              LDS = localDatastore;

              if (LDS) {
                _context4.next = 5;
                break;
              }

              _context4.next = 4;
              return this._getAllContents();

            case 4:
              LDS = _context4.sent;

            case 5:
              if (!(!LDS[objectKey] || LDS[objectKey].length === 0)) {
                _context4.next = 7;
                break;
              }

              return _context4.abrupt("return", null);

            case 7:
              root = LDS[objectKey][0];
              queue = [];
              meta = {};
              uniqueId = 0;
              meta[uniqueId] = root;
              queue.push(uniqueId);

              while (queue.length !== 0) {
                nodeId = queue.shift();
                subTreeRoot = meta[nodeId];

                for (field in subTreeRoot) {
                  value = subTreeRoot[field];

                  if (value.__type && value.__type === 'Object') {
                    key = this.getKeyForObject(value);

                    if (LDS[key] && LDS[key].length > 0) {
                      pointer = LDS[key][0];
                      uniqueId++;
                      meta[uniqueId] = pointer;
                      subTreeRoot[field] = pointer;
                      queue.push(uniqueId);
                    }
                  }
                }
              }

              return _context4.abrupt("return", root);

            case 15:
            case "end":
              return _context4.stop();
          }
        }
      }, _callee4, this);
    }));

    return function () {
      return _serializeObject2.apply(this, arguments);
    };
  }(),
  // Called when an object is save / fetched
  // Update object pin value
  _updateObjectIfPinned: function () {
    var _updateObjectIfPinned2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee5(object
    /*: ParseObject*/
    ) {
      var objectKey, pinned;
      return _regenerator.default.wrap(function (_context5) {
        while (1) {
          switch (_context5.prev = _context5.next) {
            case 0:
              if (this.isEnabled) {
                _context5.next = 2;
                break;
              }

              return _context5.abrupt("return");

            case 2:
              objectKey = this.getKeyForObject(object);
              _context5.next = 5;
              return this.fromPinWithName(objectKey);

            case 5:
              pinned = _context5.sent;

              if (!(!pinned || pinned.length === 0)) {
                _context5.next = 8;
                break;
              }

              return _context5.abrupt("return");

            case 8:
              return _context5.abrupt("return", this.pinWithName(objectKey, [object._toFullJSON()]));

            case 9:
            case "end":
              return _context5.stop();
          }
        }
      }, _callee5, this);
    }));

    return function () {
      return _updateObjectIfPinned2.apply(this, arguments);
    };
  }(),
  // Called when object is destroyed
  // Unpin object and remove all references from pin names
  // TODO: Destroy children?
  _destroyObjectIfPinned: function () {
    var _destroyObjectIfPinned2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee6(object
    /*: ParseObject*/
    ) {
      var localDatastore, objectKey, pin, promises, key, pinned;
      return _regenerator.default.wrap(function (_context6) {
        while (1) {
          switch (_context6.prev = _context6.next) {
            case 0:
              if (this.isEnabled) {
                _context6.next = 2;
                break;
              }

              return _context6.abrupt("return");

            case 2:
              _context6.next = 4;
              return this._getAllContents();

            case 4:
              localDatastore = _context6.sent;
              objectKey = this.getKeyForObject(object);
              pin = localDatastore[objectKey];

              if (pin) {
                _context6.next = 9;
                break;
              }

              return _context6.abrupt("return");

            case 9:
              promises = [this.unPinWithName(objectKey)];
              delete localDatastore[objectKey];

              for (key in localDatastore) {
                if (key === _LocalDatastoreUtils.DEFAULT_PIN || key.startsWith(_LocalDatastoreUtils.PIN_PREFIX)) {
                  pinned = localDatastore[key] || [];

                  if (pinned.includes(objectKey)) {
                    pinned = pinned.filter(function (item) {
                      return item !== objectKey;
                    });

                    if (pinned.length == 0) {
                      promises.push(this.unPinWithName(key));
                      delete localDatastore[key];
                    } else {
                      promises.push(this.pinWithName(key, pinned));
                      localDatastore[key] = pinned;
                    }
                  }
                }
              }

              return _context6.abrupt("return", Promise.all(promises));

            case 13:
            case "end":
              return _context6.stop();
          }
        }
      }, _callee6, this);
    }));

    return function () {
      return _destroyObjectIfPinned2.apply(this, arguments);
    };
  }(),
  // Update pin and references of the unsaved object
  _updateLocalIdForObject: function () {
    var _updateLocalIdForObject2 = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee7(localId
    /*: string*/
    , object
    /*: ParseObject*/
    ) {
      var localKey, objectKey, unsaved, promises, localDatastore, key, pinned;
      return _regenerator.default.wrap(function (_context7) {
        while (1) {
          switch (_context7.prev = _context7.next) {
            case 0:
              if (this.isEnabled) {
                _context7.next = 2;
                break;
              }

              return _context7.abrupt("return");

            case 2:
              localKey = "".concat(_LocalDatastoreUtils.OBJECT_PREFIX).concat(object.className, "_").concat(localId);
              objectKey = this.getKeyForObject(object);
              _context7.next = 6;
              return this.fromPinWithName(localKey);

            case 6:
              unsaved = _context7.sent;

              if (!(!unsaved || unsaved.length === 0)) {
                _context7.next = 9;
                break;
              }

              return _context7.abrupt("return");

            case 9:
              promises = [this.unPinWithName(localKey), this.pinWithName(objectKey, unsaved)];
              _context7.next = 12;
              return this._getAllContents();

            case 12:
              localDatastore = _context7.sent;

              for (key in localDatastore) {
                if (key === _LocalDatastoreUtils.DEFAULT_PIN || key.startsWith(_LocalDatastoreUtils.PIN_PREFIX)) {
                  pinned = localDatastore[key] || [];

                  if (pinned.includes(localKey)) {
                    pinned = pinned.filter(function (item) {
                      return item !== localKey;
                    });
                    pinned.push(objectKey);
                    promises.push(this.pinWithName(key, pinned));
                    localDatastore[key] = pinned;
                  }
                }
              }

              return _context7.abrupt("return", Promise.all(promises));

            case 15:
            case "end":
              return _context7.stop();
          }
        }
      }, _callee7, this);
    }));

    return function () {
      return _updateLocalIdForObject2.apply(this, arguments);
    };
  }(),

  /**
   * Updates Local Datastore from Server
   *
   * <pre>
   * await Parse.LocalDatastore.updateFromServer();
   * </pre>
   * @method updateFromServer
   * @name Parse.LocalDatastore.updateFromServer
   * @static
   */
  updateFromServer: function () {
    var _updateFromServer = (0, _asyncToGenerator2.default)(
    /*#__PURE__*/
    _regenerator.default.mark(function _callee8() {
      var _this2 = this;

      var localDatastore, keys, key, pointersHash, _i, _keys, _key, _key$split, _key$split2, className, objectId, queryPromises, responses, objects, pinPromises;

      return _regenerator.default.wrap(function (_context8) {
        while (1) {
          switch (_context8.prev = _context8.next) {
            case 0:
              if (!(!this.checkIfEnabled() || this.isSyncing)) {
                _context8.next = 2;
                break;
              }

              return _context8.abrupt("return");

            case 2:
              _context8.next = 4;
              return this._getAllContents();

            case 4:
              localDatastore = _context8.sent;
              keys = [];

              for (key in localDatastore) {
                if (key.startsWith(_LocalDatastoreUtils.OBJECT_PREFIX)) {
                  keys.push(key);
                }
              }

              if (!(keys.length === 0)) {
                _context8.next = 9;
                break;
              }

              return _context8.abrupt("return");

            case 9:
              this.isSyncing = true;
              pointersHash = {};
              _i = 0, _keys = keys;

            case 12:
              if (!(_i < _keys.length)) {
                _context8.next = 23;
                break;
              }

              _key = _keys[_i]; // Ignore the OBJECT_PREFIX

              _key$split = _key.split('_'), _key$split2 = (0, _slicedToArray2.default)(_key$split, 4), className = _key$split2[2], objectId = _key$split2[3]; // User key is split into [ 'Parse', 'LDS', '', 'User', 'objectId' ]

              if (_key.split('_').length === 5 && _key.split('_')[3] === 'User') {
                className = '_User';
                objectId = _key.split('_')[4];
              }

              if (!objectId.startsWith('local')) {
                _context8.next = 18;
                break;
              }

              return _context8.abrupt("continue", 20);

            case 18:
              if (!(className in pointersHash)) {
                pointersHash[className] = new Set();
              }

              pointersHash[className].add(objectId);

            case 20:
              _i++;
              _context8.next = 12;
              break;

            case 23:
              queryPromises = Object.keys(pointersHash).map(function (className) {
                var objectIds = Array.from(pointersHash[className]);
                var query = new _ParseQuery.default(className);
                query.limit(objectIds.length);

                if (objectIds.length === 1) {
                  query.equalTo('objectId', objectIds[0]);
                } else {
                  query.containedIn('objectId', objectIds);
                }

                return query.find();
              });
              _context8.prev = 24;
              _context8.next = 27;
              return Promise.all(queryPromises);

            case 27:
              responses = _context8.sent;
              objects = [].concat.apply([], responses);
              pinPromises = objects.map(function (object) {
                var objectKey = _this2.getKeyForObject(object);

                return _this2.pinWithName(objectKey, object._toFullJSON());
              });
              _context8.next = 32;
              return Promise.all(pinPromises);

            case 32:
              this.isSyncing = false;
              _context8.next = 39;
              break;

            case 35:
              _context8.prev = 35;
              _context8.t0 = _context8["catch"](24);
              console.error('Error syncing LocalDatastore: ', _context8.t0);
              this.isSyncing = false;

            case 39:
            case "end":
              return _context8.stop();
          }
        }
      }, _callee8, this, [[24, 35]]);
    }));

    return function () {
      return _updateFromServer.apply(this, arguments);
    };
  }(),
  getKeyForObject: function (object
  /*: any*/
  ) {
    var objectId = object.objectId || object._getId();

    return "".concat(_LocalDatastoreUtils.OBJECT_PREFIX).concat(object.className, "_").concat(objectId);
  },
  getPinName: function (pinName
  /*: ?string*/
  ) {
    if (!pinName || pinName === _LocalDatastoreUtils.DEFAULT_PIN) {
      return _LocalDatastoreUtils.DEFAULT_PIN;
    }

    return _LocalDatastoreUtils.PIN_PREFIX + pinName;
  },
  checkIfEnabled: function () {
    if (!this.isEnabled) {
      console.error('Parse.enableLocalDatastore() must be called first');
    }

    return this.isEnabled;
  }
};
module.exports = LocalDatastore;

_CoreManager.default.setLocalDatastoreController(require('./LocalDatastoreController.browser'));

_CoreManager.default.setLocalDatastore(LocalDatastore);