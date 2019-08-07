"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");

var _typeof2 = _interopRequireDefault(require("@babel/runtime/helpers/typeof"));

var equalObjects = require('./equals').default;

var decode = require('./decode').default;

var ParseError = require('./ParseError').default;

var ParsePolygon = require('./ParsePolygon').default;

var ParseGeoPoint = require('./ParseGeoPoint').default;
/**
 * contains -- Determines if an object is contained in a list with special handling for Parse pointers.
 */


function contains(haystack, needle) {
  if (needle && needle.__type && (needle.__type === 'Pointer' || needle.__type === 'Object')) {
    for (var i in haystack) {
      var ptr = haystack[i];

      if (typeof ptr === 'string' && ptr === needle.objectId) {
        return true;
      }

      if (ptr.className === needle.className && ptr.objectId === needle.objectId) {
        return true;
      }
    }

    return false;
  }

  return haystack.indexOf(needle) > -1;
}

function transformObject(object) {
  if (object._toFullJSON) {
    return object._toFullJSON();
  }

  return object;
}
/**
 * matchesQuery -- Determines if an object would be returned by a Parse Query
 * It's a lightweight, where-clause only implementation of a full query engine.
 * Since we find queries that match objects, rather than objects that match
 * queries, we can avoid building a full-blown query tool.
 */


function matchesQuery(className, object, objects, query) {
  if (object.className !== className) {
    return false;
  }

  var obj = object;
  var q = query;

  if (object.toJSON) {
    obj = object.toJSON();
  }

  if (query.toJSON) {
    q = query.toJSON().where;
  }

  obj.className = className;

  for (var field in q) {
    if (!matchesKeyConstraints(className, obj, objects, field, q[field])) {
      return false;
    }
  }

  return true;
}

function equalObjectsGeneric(obj, compareTo, eqlFn) {
  if (Array.isArray(obj)) {
    for (var i = 0; i < obj.length; i++) {
      if (eqlFn(obj[i], compareTo)) {
        return true;
      }
    }

    return false;
  }

  return eqlFn(obj, compareTo);
}
/**
 * Determines whether an object matches a single key's constraints
 */


function matchesKeyConstraints(className, object, objects, key, constraints) {
  if (constraints === null) {
    return false;
  }

  if (key.indexOf('.') >= 0) {
    // Key references a subobject
    var keyComponents = key.split('.');
    var subObjectKey = keyComponents[0];
    var keyRemainder = keyComponents.slice(1).join('.');
    return matchesKeyConstraints(className, object[subObjectKey] || {}, objects, keyRemainder, constraints);
  }

  var i;

  if (key === '$or') {
    for (i = 0; i < constraints.length; i++) {
      if (matchesQuery(className, object, objects, constraints[i])) {
        return true;
      }
    }

    return false;
  }

  if (key === '$and') {
    for (i = 0; i < constraints.length; i++) {
      if (!matchesQuery(className, object, objects, constraints[i])) {
        return false;
      }
    }

    return true;
  }

  if (key === '$nor') {
    for (i = 0; i < constraints.length; i++) {
      if (matchesQuery(className, object, objects, constraints[i])) {
        return false;
      }
    }

    return true;
  }

  if (key === '$relatedTo') {
    // Bail! We can't handle relational queries locally
    return false;
  }

  if (!/^[A-Za-z][0-9A-Za-z_]*$/.test(key)) {
    throw new ParseError(ParseError.INVALID_KEY_NAME, "Invalid Key: ".concat(key));
  } // Equality (or Array contains) cases


  if ((0, _typeof2.default)(constraints) !== 'object') {
    if (Array.isArray(object[key])) {
      return object[key].indexOf(constraints) > -1;
    }

    return object[key] === constraints;
  }

  var compareTo;

  if (constraints.__type) {
    if (constraints.__type === 'Pointer') {
      return equalObjectsGeneric(object[key], constraints, function (obj, ptr) {
        return typeof obj !== 'undefined' && ptr.className === obj.className && ptr.objectId === obj.objectId;
      });
    }

    return equalObjectsGeneric(decode(object[key]), decode(constraints), equalObjects);
  } // More complex cases


  for (var condition in constraints) {
    compareTo = constraints[condition];

    if (compareTo.__type) {
      compareTo = decode(compareTo);
    } // Compare Date Object or Date String


    if (toString.call(compareTo) === '[object Date]' || typeof compareTo === 'string' && new Date(compareTo) !== 'Invalid Date' && !isNaN(new Date(compareTo))) {
      object[key] = new Date(object[key].iso ? object[key].iso : object[key]);
    }

    switch (condition) {
      case '$lt':
        if (object[key] >= compareTo) {
          return false;
        }

        break;

      case '$lte':
        if (object[key] > compareTo) {
          return false;
        }

        break;

      case '$gt':
        if (object[key] <= compareTo) {
          return false;
        }

        break;

      case '$gte':
        if (object[key] < compareTo) {
          return false;
        }

        break;

      case '$ne':
        if (equalObjects(object[key], compareTo)) {
          return false;
        }

        break;

      case '$in':
        if (!contains(compareTo, object[key])) {
          return false;
        }

        break;

      case '$nin':
        if (contains(compareTo, object[key])) {
          return false;
        }

        break;

      case '$all':
        for (i = 0; i < compareTo.length; i++) {
          if (object[key].indexOf(compareTo[i]) < 0) {
            return false;
          }
        }

        break;

      case '$exists':
        {
          var propertyExists = typeof object[key] !== 'undefined';
          var existenceIsRequired = constraints['$exists'];

          if (typeof constraints['$exists'] !== 'boolean') {
            // The SDK will never submit a non-boolean for $exists, but if someone
            // tries to submit a non-boolean for $exits outside the SDKs, just ignore it.
            break;
          }

          if (!propertyExists && existenceIsRequired || propertyExists && !existenceIsRequired) {
            return false;
          }

          break;
        }

      case '$regex':
        {
          if ((0, _typeof2.default)(compareTo) === 'object') {
            return compareTo.test(object[key]);
          } // JS doesn't support perl-style escaping


          var expString = '';
          var escapeEnd = -2;
          var escapeStart = compareTo.indexOf('\\Q');

          while (escapeStart > -1) {
            // Add the unescaped portion
            expString += compareTo.substring(escapeEnd + 2, escapeStart);
            escapeEnd = compareTo.indexOf('\\E', escapeStart);

            if (escapeEnd > -1) {
              expString += compareTo.substring(escapeStart + 2, escapeEnd).replace(/\\\\\\\\E/g, '\\E').replace(/\W/g, '\\$&');
            }

            escapeStart = compareTo.indexOf('\\Q', escapeEnd);
          }

          expString += compareTo.substring(Math.max(escapeStart, escapeEnd + 2));
          var modifiers = constraints.$options || '';
          modifiers = modifiers.replace('x', '').replace('s', ''); // Parse Server / Mongo support x and s modifiers but JS RegExp doesn't

          var exp = new RegExp(expString, modifiers);

          if (!exp.test(object[key])) {
            return false;
          }

          break;
        }

      case '$nearSphere':
        {
          if (!compareTo || !object[key]) {
            return false;
          }

          var distance = compareTo.radiansTo(object[key]);
          var max = constraints.$maxDistance || Infinity;
          return distance <= max;
        }

      case '$within':
        {
          if (!compareTo || !object[key]) {
            return false;
          }

          var southWest = compareTo.$box[0];
          var northEast = compareTo.$box[1];

          if (southWest.latitude > northEast.latitude || southWest.longitude > northEast.longitude) {
            // Invalid box, crosses the date line
            return false;
          }

          return object[key].latitude > southWest.latitude && object[key].latitude < northEast.latitude && object[key].longitude > southWest.longitude && object[key].longitude < northEast.longitude;
        }

      case '$options':
        // Not a query type, but a way to add options to $regex. Ignore and
        // avoid the default
        break;

      case '$maxDistance':
        // Not a query type, but a way to add a cap to $nearSphere. Ignore and
        // avoid the default
        break;

      case '$select':
        {
          var subQueryObjects = objects.filter(function (obj, index, arr) {
            return matchesQuery(compareTo.query.className, obj, arr, compareTo.query.where);
          });

          for (var _i = 0; _i < subQueryObjects.length; _i += 1) {
            var subObject = transformObject(subQueryObjects[_i]);
            return equalObjects(object[key], subObject[compareTo.key]);
          }

          return false;
        }

      case '$dontSelect':
        {
          var _subQueryObjects = objects.filter(function (obj, index, arr) {
            return matchesQuery(compareTo.query.className, obj, arr, compareTo.query.where);
          });

          for (var _i2 = 0; _i2 < _subQueryObjects.length; _i2 += 1) {
            var _subObject = transformObject(_subQueryObjects[_i2]);

            return !equalObjects(object[key], _subObject[compareTo.key]);
          }

          return false;
        }

      case '$inQuery':
        {
          var _subQueryObjects2 = objects.filter(function (obj, index, arr) {
            return matchesQuery(compareTo.className, obj, arr, compareTo.where);
          });

          for (var _i3 = 0; _i3 < _subQueryObjects2.length; _i3 += 1) {
            var _subObject2 = transformObject(_subQueryObjects2[_i3]);

            if (object[key].className === _subObject2.className && object[key].objectId === _subObject2.objectId) {
              return true;
            }
          }

          return false;
        }

      case '$notInQuery':
        {
          var _subQueryObjects3 = objects.filter(function (obj, index, arr) {
            return matchesQuery(compareTo.className, obj, arr, compareTo.where);
          });

          for (var _i4 = 0; _i4 < _subQueryObjects3.length; _i4 += 1) {
            var _subObject3 = transformObject(_subQueryObjects3[_i4]);

            if (object[key].className === _subObject3.className && object[key].objectId === _subObject3.objectId) {
              return false;
            }
          }

          return true;
        }

      case '$containedBy':
        {
          var _iteratorNormalCompletion = true;
          var _didIteratorError = false;
          var _iteratorError = undefined;

          try {
            for (var _iterator = object[key][Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
              var value = _step.value;

              if (!contains(compareTo, value)) {
                return false;
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

          return true;
        }

      case '$geoWithin':
        {
          var points = compareTo.$polygon.map(function (geoPoint) {
            return [geoPoint.latitude, geoPoint.longitude];
          });
          var polygon = new ParsePolygon(points);
          return polygon.containsPoint(object[key]);
        }

      case '$geoIntersects':
        {
          var _polygon = new ParsePolygon(object[key].coordinates);

          var point = new ParseGeoPoint(compareTo.$point);
          return _polygon.containsPoint(point);
        }

      default:
        return false;
    }
  }

  return true;
}

function validateQuery(query
/*: any*/
) {
  var q = query;

  if (query.toJSON) {
    q = query.toJSON().where;
  }

  var specialQuerykeys = ['$and', '$or', '$nor', '_rperm', '_wperm', '_perishable_token', '_email_verify_token', '_email_verify_token_expires_at', '_account_lockout_expires_at', '_failed_login_count'];
  Object.keys(q).forEach(function (key) {
    if (q && q[key] && q[key].$regex) {
      if (typeof q[key].$options === 'string') {
        if (!q[key].$options.match(/^[imxs]+$/)) {
          throw new ParseError(ParseError.INVALID_QUERY, "Bad $options value for query: ".concat(q[key].$options));
        }
      }
    }

    if (specialQuerykeys.indexOf(key) < 0 && !key.match(/^[a-zA-Z][a-zA-Z0-9_\.]*$/)) {
      throw new ParseError(ParseError.INVALID_KEY_NAME, "Invalid key name: ".concat(key));
    }
  });
}

var OfflineQuery = {
  matchesQuery: matchesQuery,
  validateQuery: validateQuery
};
module.exports = OfflineQuery;